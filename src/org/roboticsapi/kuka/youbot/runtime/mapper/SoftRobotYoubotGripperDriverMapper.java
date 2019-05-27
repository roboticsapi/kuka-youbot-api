/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2010-2017 ISSE, University of Augsburg 
 */

package org.roboticsapi.kuka.youbot.runtime.mapper;

import org.roboticsapi.core.DeviceParameterBag;
import org.roboticsapi.kuka.youbot.runtime.SoftRobotYoubotGripperDriver;
import org.roboticsapi.kuka.youbot.runtime.primitives.Gripper;
import org.roboticsapi.runtime.SoftRobotRuntime;
import org.roboticsapi.runtime.mapping.MappingException;
import org.roboticsapi.runtime.mapping.net.DataflowOutPort;
import org.roboticsapi.runtime.mapping.net.NetFragment;
import org.roboticsapi.runtime.mapping.net.StateDataflow;
import org.roboticsapi.runtime.mapping.parts.ActuatorDriverMapper;
import org.roboticsapi.runtime.mapping.parts.DeviceMappingPorts;
import org.roboticsapi.runtime.mapping.result.ActuatorDriverMapperResult;
import org.roboticsapi.runtime.mapping.result.impl.BaseActuatorDriverMapperResult;
import org.roboticsapi.runtime.rpi.RPIException;

public class SoftRobotYoubotGripperDriverMapper implements
		ActuatorDriverMapper<org.roboticsapi.runtime.SoftRobotRuntime, org.roboticsapi.kuka.youbot.runtime.SoftRobotYoubotGripperDriver, GripperGoalActionResult> {

	@Override
	public ActuatorDriverMapperResult map(SoftRobotRuntime runtime, SoftRobotYoubotGripperDriver actuatorDriver,
			GripperGoalActionResult actionResult, DeviceParameterBag parameters, DeviceMappingPorts ports)
			throws MappingException, RPIException {

		final NetFragment outputNet = new NetFragment("YouBot Gripper");
		Gripper gripper = outputNet.add(new Gripper(actionResult.getDistance(), actuatorDriver.getDeviceName()));
		DataflowOutPort done = outputNet.addOutPort(new StateDataflow(), false, gripper.getOutCompleted());

		return new BaseActuatorDriverMapperResult(actuatorDriver, outputNet, done);
	}

}
