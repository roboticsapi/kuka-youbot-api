/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2010-2017 ISSE, University of Augsburg 
 */

package org.roboticsapi.kuka.youbot.activity;

import org.roboticsapi.activity.Activity;
import org.roboticsapi.activity.ActivityNotCompletedException;
import org.roboticsapi.activity.ActuatorInterfaceImpl;
import org.roboticsapi.activity.RtActivity;
import org.roboticsapi.activity.SingleDeviceRtActivity;
import org.roboticsapi.core.RuntimeCommand;
import org.roboticsapi.core.exception.RoboticsException;
import org.roboticsapi.kuka.youbot.YoubotGripper;
import org.roboticsapi.kuka.youbot.action.MoveGripper;
import org.roboticsapi.tool.gripper.GripperParameters;
import org.roboticsapi.tool.gripper.activity.GrippingInterface;

public class YoubotGrippingInterfaceImpl extends ActuatorInterfaceImpl<YoubotGripper> implements GrippingInterface {

	public YoubotGrippingInterfaceImpl(YoubotGripper device) {
		super(device);
	}

	@Override
	public RtActivity open(GripperParameters... parameters) throws RoboticsException {
		RuntimeCommand cmd = getDevice().getDriver().getRuntime().createRuntimeCommand(getDevice(),
				new MoveGripper(0.022), getDefaultParameters().withParameters(parameters));

		return new SingleDeviceRtActivity<YoubotGripper>(getDevice(), cmd, null) {

			@Override
			protected boolean prepare(Activity prevActivity) throws RoboticsException, ActivityNotCompletedException {
				return false;
			}
		};
	}

	@Override
	public RtActivity close(GripperParameters... parameters) throws RoboticsException {
		RuntimeCommand cmd = getDevice().getDriver().getRuntime().createRuntimeCommand(getDevice(), new MoveGripper(0),
				getDefaultParameters().withParameters(parameters));

		return new SingleDeviceRtActivity<YoubotGripper>(getDevice(), cmd, null) {

			@Override
			protected boolean prepare(Activity prevActivity) throws RoboticsException, ActivityNotCompletedException {
				return false;
			}
		};
	}

}
