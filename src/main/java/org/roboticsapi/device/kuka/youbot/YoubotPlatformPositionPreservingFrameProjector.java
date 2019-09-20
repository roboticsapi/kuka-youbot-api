/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2013-2019 ISSE, University of Augsburg 
 */

package org.roboticsapi.device.kuka.youbot;

import org.roboticsapi.core.DeviceParameterBag;
import org.roboticsapi.core.realtimevalue.realtimedouble.RealtimeDouble;
import org.roboticsapi.core.world.FrameTopology;
import org.roboticsapi.core.world.TransformationException;
import org.roboticsapi.core.world.World;
import org.roboticsapi.core.world.realtimevalue.RealtimePose;
import org.roboticsapi.core.world.realtimevalue.RealtimePoseCoincidence;
import org.roboticsapi.core.world.realtimevalue.realtimerotation.RealtimeRotation;
import org.roboticsapi.core.world.realtimevalue.realtimetransformation.RealtimeTransformation;
import org.roboticsapi.core.world.realtimevalue.realtimevector.RealtimeVector;
import org.roboticsapi.framework.cartesianmotion.FrameProjector;

public class YoubotPlatformPositionPreservingFrameProjector implements FrameProjector {

	private YoubotPlatform platform;

	public YoubotPlatformPositionPreservingFrameProjector(YoubotPlatform platform) {
		this.platform = platform;
	}

	private RealtimeTransformation projectOrientation(RealtimeTransformation toProject,
			RealtimeTransformation flangeToMotionCenter) {

		RealtimeTransformation flangeToMotionCenterPosition = RealtimeTransformation.createFromVectorRotation(
				flangeToMotionCenter.getTranslation(), RealtimeRotation.createFromABC(flangeToMotionCenter.getA(),
						RealtimeDouble.createFromConstant(0), RealtimeDouble.createFromConstant(0)));

		RealtimeTransformation originToMotionCenter = toProject.multiply(flangeToMotionCenter);

		RealtimeTransformation originToMotionCenterPosition = RealtimeTransformation.createFromVectorRotation(
				originToMotionCenter.getTranslation(), RealtimeRotation.createFromABC(originToMotionCenter.getA(),
						RealtimeDouble.createFromConstant(0), RealtimeDouble.createFromConstant(0)));

		RealtimeTransformation projected = originToMotionCenterPosition.multiply(flangeToMotionCenterPosition.invert());
		return projected;
	}

	private RealtimeTransformation projectPositionToXYPlane(RealtimeTransformation toProject) {
		return RealtimeTransformation.createFromVectorRotation(
				RealtimeVector.createFromXYZ(toProject.getX(), toProject.getY(), RealtimeDouble.createFromConstant(0)),
				toProject.getRotation());
	}

	@Override
	public RealtimePose project(RealtimePose toProject, RealtimePose motionCenter, DeviceParameterBag parameters)
			throws TransformationException {
		FrameTopology topology = World.getCommandedTopology().forRuntime(platform.getDriver().getRuntime());

		RealtimeTransformation flangeToMotionCenter = platform.getOdometryFrame().asRealtimePose()
				.getRealtimeTransformationTo(motionCenter, topology);
		RealtimeTransformation baseToFlange = new RealtimePoseCoincidence(toProject, motionCenter)
				.getTransformation(platform.getReferenceFrame(), platform.getMovingFrame(), topology);

		baseToFlange = projectOrientation(baseToFlange, flangeToMotionCenter);
		baseToFlange = projectPositionToXYPlane(baseToFlange);

		return baseToFlange.multiply(flangeToMotionCenter).asPose(platform.getReferenceFrame());
	}
}
