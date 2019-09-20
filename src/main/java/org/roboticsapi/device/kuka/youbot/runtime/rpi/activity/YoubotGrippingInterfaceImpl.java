/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2013-2019 ISSE, University of Augsburg 
 */

package org.roboticsapi.device.kuka.youbot.runtime.rpi.activity;

import org.roboticsapi.core.activity.Activity;
import org.roboticsapi.core.activity.runtime.ActuatorInterfaceImpl;
import org.roboticsapi.core.activity.runtime.FromCommandActivity;
import org.roboticsapi.core.exception.RoboticsException;
import org.roboticsapi.device.kuka.youbot.action.MoveGripper;
import org.roboticsapi.device.kuka.youbot.runtime.rpi.YoubotGripperGenericDriver;
import org.roboticsapi.framework.gripper.GripperParameters;
import org.roboticsapi.framework.gripper.activity.GrippingInterface;

public class YoubotGrippingInterfaceImpl extends ActuatorInterfaceImpl implements GrippingInterface {

	public YoubotGrippingInterfaceImpl(YoubotGripperGenericDriver driver) {
		super(driver);
	}

	@Override
	public Activity open(GripperParameters... parameters) throws RoboticsException {
		return new FromCommandActivity(
				() -> createRuntimeCommand(new MoveGripper(0.022), getDefaultParameters().withParameters(parameters)),
				getDevice());
	}

	@Override
	public Activity close(GripperParameters... parameters) throws RoboticsException {
		return new FromCommandActivity(
				() -> createRuntimeCommand(new MoveGripper(0), getDefaultParameters().withParameters(parameters)),
				getDevice());
	}

}
