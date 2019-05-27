/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2010-2017 ISSE, University of Augsburg 
 */

package org.roboticsapi.kuka.youbot.runtime.mapper;

import org.roboticsapi.core.DeviceParameterBag;
import org.roboticsapi.kuka.youbot.runtime.SoftRobotYoubotArmDriver;
import org.roboticsapi.kuka.youbot.runtime.primitives.ArmControlStrategy;
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

public class SoftRobotYoubotArmDriverPositionControllerMapper
		implements ActuatorDriverMapper<SoftRobotRuntime, SoftRobotYoubotArmDriver, PositionControllerActionResult> {

	private static final int POSITION_CONTROLLER_NUMBER = 0;

	@Override
	public ActuatorDriverMapperResult map(SoftRobotRuntime runtime, SoftRobotYoubotArmDriver actuatorDriver,
			PositionControllerActionResult actionResult, DeviceParameterBag parameters, DeviceMappingPorts ports)
			throws MappingException, RPIException {

		NetFragment fragment = new NetFragment("youBotPositionController");

		ArmControlStrategy controlStrategy = fragment
				.add(new ArmControlStrategy(actuatorDriver.getDeviceName(), POSITION_CONTROLLER_NUMBER));
		DataflowOutPort success = fragment.addOutPort(new StateDataflow(), false, controlStrategy.getOutSuccess());

		return new BaseActuatorDriverMapperResult(actuatorDriver, fragment, success);
	}

}
