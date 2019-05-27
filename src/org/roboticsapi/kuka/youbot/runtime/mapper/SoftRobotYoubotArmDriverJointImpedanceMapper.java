/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2010-2017 ISSE, University of Augsburg 
 */

package org.roboticsapi.kuka.youbot.runtime.mapper;

import java.util.ArrayList;
import java.util.List;

import org.roboticsapi.core.DeviceParameterBag;
import org.roboticsapi.kuka.youbot.runtime.SoftRobotYoubotArmDriver;
import org.roboticsapi.kuka.youbot.runtime.primitives.ArmControlStrategy;
import org.roboticsapi.kuka.youbot.runtime.primitives.JointImpParameters;
import org.roboticsapi.runtime.SoftRobotRuntime;
import org.roboticsapi.runtime.mapping.MappingException;
import org.roboticsapi.runtime.mapping.net.DataflowInPort;
import org.roboticsapi.runtime.mapping.net.DataflowOutPort;
import org.roboticsapi.runtime.mapping.net.DoubleDataflow;
import org.roboticsapi.runtime.mapping.net.NetFragment;
import org.roboticsapi.runtime.mapping.net.StateDataflow;
import org.roboticsapi.runtime.mapping.parts.ActuatorDriverMapper;
import org.roboticsapi.runtime.mapping.parts.AndFragment;
import org.roboticsapi.runtime.mapping.parts.DeviceMappingPorts;
import org.roboticsapi.runtime.mapping.result.ActuatorDriverMapperResult;
import org.roboticsapi.runtime.mapping.result.impl.BaseActuatorDriverMapperResult;
import org.roboticsapi.runtime.rpi.RPIException;

public class SoftRobotYoubotArmDriverJointImpedanceMapper
		implements ActuatorDriverMapper<SoftRobotRuntime, SoftRobotYoubotArmDriver, JointImpedanceActionResult> {

	private static final int NUM_AXIS = 5;
	private static final int IMPEDANCE_CONTROLLER_NUMBER = 1;

	@Override
	public ActuatorDriverMapperResult map(SoftRobotRuntime runtime, SoftRobotYoubotArmDriver actuatorDriver,
			JointImpedanceActionResult actionResult, DeviceParameterBag parameters, DeviceMappingPorts ports)
			throws MappingException, RPIException {

		if (actionResult.getJointCount() != NUM_AXIS)
			throw new MappingException(
					"Invalid number of joints (got " + actionResult.getJointCount() + ", expected " + NUM_AXIS + ")");

		List<DataflowOutPort> donePorts = new ArrayList<DataflowOutPort>();
		NetFragment fragment = new NetFragment("youBotImpedanceController");

		ArmControlStrategy controlStrategy = fragment
				.add(new ArmControlStrategy(actuatorDriver.getDeviceName(), IMPEDANCE_CONTROLLER_NUMBER));
		donePorts.add(fragment.addOutPort(new StateDataflow(), false, controlStrategy.getOutSuccess()));

		for (int i = 0; i < NUM_AXIS; i++) {
			JointImpParameters parameter = fragment.add(new JointImpParameters(actuatorDriver.getDeviceName(), i));
			DataflowInPort stiffnessPort = fragment.addInPort(new DoubleDataflow(), true, parameter.getInStiffness());
			DataflowInPort dampingPort = fragment.addInPort(new DoubleDataflow(), true, parameter.getInDamping());
			DataflowInPort addTorquePort = fragment.addInPort(new DoubleDataflow(), true, parameter.getInAddTorque());
			fragment.connect(actionResult.getStiffnessOutPort(i), stiffnessPort);
			fragment.connect(actionResult.getDampingOutPort(i), dampingPort);
			fragment.connect(actionResult.getAddTorqueOutPort(i), addTorquePort);

			parameter.setMaxTorque(actionResult.getMaxTorques()[i]);

			donePorts.add(fragment.addOutPort(new StateDataflow(), false, parameter.getOutSuccess()));
		}

		AndFragment allDone = fragment.add(new AndFragment(new StateDataflow(), donePorts));

		return new BaseActuatorDriverMapperResult(actuatorDriver, fragment, allDone.getAndOut());
	}
}
