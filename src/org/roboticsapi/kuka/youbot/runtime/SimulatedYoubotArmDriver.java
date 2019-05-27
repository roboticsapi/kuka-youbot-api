/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2010-2017 ISSE, University of Augsburg 
 */

package org.roboticsapi.kuka.youbot.runtime;

import org.roboticsapi.kuka.youbot.YoubotArm;
import org.roboticsapi.robot.RobotArmDriver;

/**
 * {@link RobotArmDriver} for a simulated {@link YoubotArm}.
 */
public final class SimulatedYoubotArmDriver extends SoftRobotYoubotArmDriver {

	private static final String DEVICE_TYPE = "kuka_youbot_arm_sim";

	@Override
	public String getDeviceType() {
		return DEVICE_TYPE;
	}

	@Override
	public boolean build(YoubotArm t) {
		return loadDeviceDriver(null);
	}

	@Override
	public void delete() {
		deleteDeviceDriver();
	}

	@Override
	protected boolean checkDeviceType(String deviceType) {
		return DEVICE_TYPE.equals(deviceType);
	}

}