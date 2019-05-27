/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2010-2017 ISSE, University of Augsburg 
 */

package org.roboticsapi.kuka.youbot.runtime.mapper;

import org.roboticsapi.core.DeviceParameterBag;
import org.roboticsapi.kuka.youbot.action.SwitchControllerAction;
import org.roboticsapi.kuka.youbot.runtime.SoftRobotYoubotArmDriver;
import org.roboticsapi.multijoint.parameter.ControllerParameter;
import org.roboticsapi.runtime.SoftRobotRuntime;
import org.roboticsapi.runtime.mapping.MappingException;
import org.roboticsapi.runtime.mapping.net.NetFragment;
import org.roboticsapi.runtime.mapping.parts.DeviceMappingPorts;
import org.roboticsapi.runtime.mapping.result.ActionMapperResult;
import org.roboticsapi.runtime.mapping.result.ActuatorDriverMapperResult;
import org.roboticsapi.runtime.mapping.result.impl.WrappedActuatorDriverMapperResult;
import org.roboticsapi.runtime.multijoint.mapper.JointGoalActionResult;
import org.roboticsapi.runtime.robot.driver.mapper.SoftRobotRobotArmDriverJointGoalMapper;
import org.roboticsapi.runtime.rpi.RPIException;

public class SoftRobotYoubotArmDriverJointGoalMapper<DD extends SoftRobotYoubotArmDriver>
		extends SoftRobotRobotArmDriverJointGoalMapper<DD> {

	@Override
	public ActuatorDriverMapperResult map(SoftRobotRuntime runtime, final DD driver, JointGoalActionResult actionResult,
			final DeviceParameterBag parameters, DeviceMappingPorts ports) throws MappingException, RPIException {

		ActuatorDriverMapperResult result = super.map(runtime, driver, actionResult, parameters, ports);

		NetFragment fragment = new NetFragment("youBot");
		fragment.add(result.getNetFragment());

		if (parameters.get(ControllerParameter.class) != null) {
			ControllerParameter cp = parameters.get(ControllerParameter.class);
			if (cp.getController() != null) {

				ActionMapperResult switchAction = runtime.getMapperRegistry().mapAction(runtime,
						new SwitchControllerAction(cp.getController()), parameters, ports.cancel, ports.override, null);

				ActuatorDriverMapperResult switchDriver = runtime.getMapperRegistry().mapActuatorDriver(runtime, driver,
						switchAction.getActionResult(), parameters, ports.cancel, ports.override);

				fragment.add(switchAction.getNetFragment());
				fragment.add(switchDriver.getNetFragment());
			}
		}

		return new WrappedActuatorDriverMapperResult(driver, fragment, result);
	}

}
