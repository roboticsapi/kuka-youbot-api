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
import org.roboticsapi.kuka.youbot.sensor.PalletizingFrameProjectionSensor;
import org.roboticsapi.world.Frame;
import org.roboticsapi.world.TransformationException;
import org.roboticsapi.world.sensor.RelationSensor;

public class YoubotArmPalletizingFrameProjector implements FrameProjector {

	private final YoubotArm youbot;

	public YoubotArmPalletizingFrameProjector(YoubotArm youbot) {
		this.youbot = youbot;

	}

	@Override
	public RelationSensor project(RelationSensor toProject, DeviceParameterBag parameters)
			throws TransformationException {

		Frame flange = youbot.getFlange();
		Frame motionCenter = parameters.get(MotionCenterParameter.class).getMotionCenter();
		RelationSensor flangeToMotionCenter = flange.getRelationSensor(motionCenter);
		if (flangeToMotionCenter == null)
			throw new TransformationException("No connection between Frames", flange, motionCenter);

		return new RelationSensor(
				new PalletizingFrameProjectionSensor(youbot.getDriver().getRuntime(), toProject, flangeToMotionCenter),
				youbot.getBase(), youbot.getFlange());
	}
}
