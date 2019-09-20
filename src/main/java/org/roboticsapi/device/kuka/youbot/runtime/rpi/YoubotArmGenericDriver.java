/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2013-2019 ISSE, University of Augsburg 
 */

package org.roboticsapi.device.kuka.youbot.runtime.rpi;

import org.roboticsapi.device.kuka.youbot.YoubotArm;
import org.roboticsapi.facet.runtime.rpi.mapping.RpiRuntime;
import org.roboticsapi.framework.multijoint.activity.JointControllerInterface;
import org.roboticsapi.framework.multijoint.runtime.rpi.activity.JointControllerInterfaceImpl;
import org.roboticsapi.framework.robot.RobotArmDriver;
import org.roboticsapi.framework.robot.runtime.rpi.driver.RobotArmGenericDriver;

/**
 * Generic {@link RobotArmDriver} for the {@link YoubotArm}.
 */
public class YoubotArmGenericDriver extends RobotArmGenericDriver<YoubotArm> {

	public YoubotArmGenericDriver() {
	}

	public YoubotArmGenericDriver(YoubotArm arm, RpiRuntime runtime) {
		setDevice(arm);
		setRuntime(runtime);
	}

	@Override
	protected boolean checkDeviceType(String deviceType) {
		// TODO: Hack! Change to interfaces for setting controller parameters
		return "kuka_youbot_arm_sim".equals(deviceType) || "kuka_youbot_arm_ec".equals(deviceType);
	}

	@Override
	protected void onPresent() {
		super.onPresent();
		getDevice().addInterfaceFactory(JointControllerInterface.class, () -> new JointControllerInterfaceImpl(this));
	}

}