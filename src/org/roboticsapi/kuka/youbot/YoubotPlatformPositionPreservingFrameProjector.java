/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2010-2017 ISSE, University of Augsburg 
 */

package org.roboticsapi.kuka.youbot;

import org.roboticsapi.cartesianmotion.FrameProjector;
import org.roboticsapi.cartesianmotion.parameter.MotionCenterParameter;
import org.roboticsapi.core.DeviceParameterBag;
import org.roboticsapi.core.sensor.DoubleSensor;
import org.roboticsapi.world.sensor.RelationSensor;
import org.roboticsapi.world.sensor.RotationSensor;
import org.roboticsapi.world.sensor.TransformationSensor;
import org.roboticsapi.world.sensor.VectorSensor;

public class YoubotPlatformPositionPreservingFrameProjector implements FrameProjector {

	private YoubotPlatform platform;

	public YoubotPlatformPositionPreservingFrameProjector(YoubotPlatform platform) {
		this.platform = platform;

	}

	@Override
	public RelationSensor project(RelationSensor toProject, DeviceParameterBag parameters) {

		TransformationSensor flangeToMotionCenter = platform.getOdometryFrame()
				.getRelationSensor(parameters.get(MotionCenterParameter.class).getMotionCenter())
				.getTransformationSensor();

		TransformationSensor trans = toProject.getTransformationSensor();
		trans = projectOrientation(trans, flangeToMotionCenter);
		trans = projectPositionToXYPlane(trans);

		return new RelationSensor(trans, toProject.getFrom(), toProject.getTo());
	}

	private TransformationSensor projectOrientation(TransformationSensor toProject,
			TransformationSensor flangeToMotionCenter) {
		TransformationSensor flangeToMotionCenterPosition = TransformationSensor
				.fromComponents(flangeToMotionCenter.getTranslationSensor(), RotationSensor
						.fromABC(flangeToMotionCenter.getA(), DoubleSensor.fromValue(0), DoubleSensor.fromValue(0)));

		TransformationSensor originToMotionCenter = toProject.multiply(flangeToMotionCenter);

		TransformationSensor originToMotionCenterPosition = TransformationSensor
				.fromComponents(originToMotionCenter.getTranslationSensor(), RotationSensor
						.fromABC(originToMotionCenter.getA(), DoubleSensor.fromValue(0), DoubleSensor.fromValue(0)));

		TransformationSensor projected = originToMotionCenterPosition.multiply(flangeToMotionCenterPosition.invert());
		return projected;
	}

	private TransformationSensor projectPositionToXYPlane(TransformationSensor toProject) {
		return TransformationSensor.fromComponents(
				VectorSensor.fromComponents(toProject.getX(), toProject.getY(), DoubleSensor.fromValue(0)),
				toProject.getRotationSensor());
	}
}
