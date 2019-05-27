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
import org.roboticsapi.kuka.youbot.sensor.OrientationPreservingFrameProjectionSensor;
import org.roboticsapi.world.sensor.RelationSensor;

public class OrientationPreservingFrameProjector implements FrameProjector {

	private YoubotArm youbotArm;

	public OrientationPreservingFrameProjector(YoubotArm youbotArm) {
		this.youbotArm = youbotArm;
	}

	@Override
	public RelationSensor project(RelationSensor toProject, DeviceParameterBag parameters) {
		RelationSensor flangeToMotionCenter = youbotArm.getFlange()
				.getRelationSensor(parameters.get(MotionCenterParameter.class).getMotionCenter());

		return new RelationSensor(new OrientationPreservingFrameProjectionSensor(youbotArm.getDriver().getRuntime(),
				toProject, flangeToMotionCenter), youbotArm.getBase(), youbotArm.getFlange());
	}

}
