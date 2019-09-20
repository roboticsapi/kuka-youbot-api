/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2013-2019 ISSE, University of Augsburg 
 */

package org.roboticsapi.device.kuka.youbot;

import org.roboticsapi.core.DeviceParameterBag;
import org.roboticsapi.core.world.Frame;
import org.roboticsapi.core.world.FrameTopology;
import org.roboticsapi.core.world.TransformationException;
import org.roboticsapi.core.world.World;
import org.roboticsapi.core.world.realtimevalue.RealtimePose;
import org.roboticsapi.core.world.realtimevalue.RealtimePoseCoincidence;
import org.roboticsapi.core.world.realtimevalue.realtimetransformation.RealtimeTransformation;
import org.roboticsapi.device.kuka.youbot.sensor.OrientationPreservingFrameProjectionSensor;
import org.roboticsapi.framework.cartesianmotion.FrameProjector;

public class YoubotArmOrientationPreservingFrameProjector implements FrameProjector {

	private YoubotArm youbot;

	public YoubotArmOrientationPreservingFrameProjector(YoubotArm youbotArm) {
		this.youbot = youbotArm;
	}

	@Override
	public RealtimePose project(RealtimePose toProject, RealtimePose motionCenter, DeviceParameterBag parameters)
			throws TransformationException {
		FrameTopology topology = World.getCommandedTopology().forRuntime(youbot.getDriver().getRuntime());
		Frame flange = youbot.getFlange();
		RealtimeTransformation flangeToMotionCenter = flange.asRealtimePose().getRealtimeTransformationTo(motionCenter,
				topology);
		RealtimeTransformation baseToFlange = new RealtimePoseCoincidence(toProject, motionCenter)
				.getTransformation(youbot.getReferenceFrame(), youbot.getMovingFrame(), topology);

		if (flangeToMotionCenter == null)
			throw new TransformationException("No connection between Frames", flange, motionCenter.getReference());

		return RealtimePose.createFromTransformation(youbot.getReferenceFrame(),
				new OrientationPreservingFrameProjectionSensor(baseToFlange, flangeToMotionCenter)
						.multiply(flangeToMotionCenter));
	}

}
