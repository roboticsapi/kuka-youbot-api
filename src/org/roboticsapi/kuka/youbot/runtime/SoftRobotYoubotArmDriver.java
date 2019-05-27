/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2010-2017 ISSE, University of Augsburg 
 */

package org.roboticsapi.kuka.youbot.runtime;

import org.roboticsapi.kuka.youbot.YoubotArm;
import org.roboticsapi.robot.RobotArmDriver;
import org.roboticsapi.runtime.driver.DeviceBasedLoadable;
import org.roboticsapi.runtime.robot.driver.SoftRobotRobotArmDriver;

/**
 * Generic {@link RobotArmDriver} for the {@link YoubotArm}.
 */
public class SoftRobotYoubotArmDriver extends SoftRobotRobotArmDriver implements DeviceBasedLoadable<YoubotArm> {

	@Override
	protected boolean checkDeviceType(String deviceType) {
		// TODO: Hack! Change to interfaces for setting controller parameters
		return "kuka_youbot_arm_sim".equals(deviceType) || "kuka_youbot_arm_ec".equals(deviceType);
	}

	@Override
	public boolean build(YoubotArm t) {
		return false;
	}

	@Override
	public void delete() {
	}

}