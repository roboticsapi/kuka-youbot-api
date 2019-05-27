/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2010-2017 ISSE, University of Augsburg 
 */

package org.roboticsapi.kuka.youbot.runtime;

import java.util.List;

import org.roboticsapi.core.actuator.ActuatorDriverRealtimeException;
import org.roboticsapi.core.sensor.DeviceBasedDoubleSensor;
import org.roboticsapi.core.sensor.DoubleSensor;
import org.roboticsapi.kuka.youbot.YoubotGripper;
import org.roboticsapi.kuka.youbot.YoubotGripperDriver;
import org.roboticsapi.runtime.AbstractSoftRobotActuatorDriver;
import org.roboticsapi.runtime.driver.DeviceBasedLoadable;
import org.roboticsapi.tool.gripper.GripperException;

public class SoftRobotYoubotGripperDriver extends AbstractSoftRobotActuatorDriver
		implements YoubotGripperDriver, DeviceBasedLoadable<YoubotGripper> {

	@Override
	protected boolean checkDeviceType(String deviceType) {
		// TODO: Hack! Change to interfaces for setting controller parameters
		return "kuka_youbot_gripper_sim".equals(deviceType) || "kuka_youbot_gripper_ec".equals(deviceType);
	}

	@Override
	protected boolean checkDeviceInterfaces(List<String> interfaces) {
		return true;
	}

	@Override
	public List<ActuatorDriverRealtimeException> defineActuatorDriverExceptions() {
		List<ActuatorDriverRealtimeException> exceptions = super.defineActuatorDriverExceptions();

		exceptions.add(new GripperException(this));

		return exceptions;
	}

	@Override
	public boolean build(YoubotGripper t) {
		// do nothing...
		return false;
	}

	@Override
	public void delete() {
		// do nothing...
	}

	@Override
	public DoubleSensor getOpeningWidthSensor() {
		return new OpeningWidthSensor(this);
	}

	public static final class OpeningWidthSensor extends DeviceBasedDoubleSensor<SoftRobotYoubotGripperDriver> {
		public OpeningWidthSensor(SoftRobotYoubotGripperDriver gripper) {
			super(gripper);
		}
	}

}
