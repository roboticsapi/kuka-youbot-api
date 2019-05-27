/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2010-2017 ISSE, University of Augsburg 
 */

package org.roboticsapi.kuka.youbot.runtime;

import org.roboticsapi.kuka.youbot.YoubotPlatform;
import org.roboticsapi.runtime.driver.DeviceBasedLoadable;
import org.roboticsapi.runtime.platform.driver.SoftRobotPlatformCartesianPositionDriver;

public class SimulatedYoubotPlatformCartesianPositionDriver extends SoftRobotPlatformCartesianPositionDriver
		implements DeviceBasedLoadable<YoubotPlatform> {

	private static final String DEVICE_TYPE = "kuka_youbot_base_sim";

	@Override
	public String getDeviceType() {
		return DEVICE_TYPE;
	}

	@Override
	public boolean build(YoubotPlatform t) {
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
