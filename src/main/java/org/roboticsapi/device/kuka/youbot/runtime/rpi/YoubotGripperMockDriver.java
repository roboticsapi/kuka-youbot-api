/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2013-2019 ISSE, University of Augsburg 
 */

package org.roboticsapi.device.kuka.youbot.runtime.rpi;

import org.roboticsapi.device.kuka.youbot.YoubotArm;
import org.roboticsapi.device.kuka.youbot.YoubotGripper;
import org.roboticsapi.facet.runtime.rpi.mapping.RpiRuntime;
import org.roboticsapi.framework.robot.RobotArmDriver;

/**
 * {@link RobotArmDriver} for a simulated {@link YoubotArm}.
 */
public final class YoubotGripperMockDriver extends YoubotGripperGenericDriver {

	private static final String DEVICE_TYPE = "kuka_youbot_gripper_sim";

	public YoubotGripperMockDriver() {
	}

	public YoubotGripperMockDriver(YoubotGripper gripper, RpiRuntime runtime) {
		this();
		setDevice(gripper);
		setRuntime(runtime);
	}

	@Override
	public String getRpiDeviceType() {
		return DEVICE_TYPE;
	}

	@Override
	protected boolean checkDeviceType(String deviceType) {
		return DEVICE_TYPE.equals(deviceType);
	}

}