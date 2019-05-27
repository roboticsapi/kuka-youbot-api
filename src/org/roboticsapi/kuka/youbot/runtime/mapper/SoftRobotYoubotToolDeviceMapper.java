/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2010-2017 ISSE, University of Augsburg 
 */

package org.roboticsapi.kuka.youbot.runtime.mapper;

import org.roboticsapi.core.DeviceParameterBag;
import org.roboticsapi.kuka.youbot.runtime.SoftRobotYoubotArmDriver;
import org.roboticsapi.runtime.world.WorldLinkBuilder;
import org.roboticsapi.runtime.world.dataflow.RelationDataflow;
import org.roboticsapi.runtime.world.dataflow.VectorDataflow;
import org.roboticsapi.runtime.world.primitives.FrameToPosRot;
import org.roboticsapi.runtime.SoftRobotRuntime;
import org.roboticsapi.runtime.mapping.MappingException;
import org.roboticsapi.runtime.mapping.net.DataflowInPort;
import org.roboticsapi.runtime.mapping.net.DataflowOutPort;
import org.roboticsapi.runtime.mapping.net.DoubleDataflow;
import org.roboticsapi.runtime.mapping.net.NetFragment;
import org.roboticsapi.runtime.mapping.net.StateDataflow;
import org.roboticsapi.runtime.mapping.parts.ActuatorDriverMapper;
import org.roboticsapi.runtime.mapping.parts.DeviceMappingPorts;
import org.roboticsapi.runtime.mapping.result.ActuatorDriverMapperResult;
import org.roboticsapi.runtime.mapping.result.impl.BaseActuatorDriverMapperResult;
import org.roboticsapi.runtime.robot.primitives.ToolParameters;
import org.roboticsapi.runtime.rpi.RPIException;

public class SoftRobotYoubotToolDeviceMapper
		implements ActuatorDriverMapper<SoftRobotRuntime, SoftRobotYoubotArmDriver, ToolActionResult> {

	@Override
	public ActuatorDriverMapperResult map(SoftRobotRuntime runtime, SoftRobotYoubotArmDriver actuatorDriver,
			ToolActionResult actionResult, DeviceParameterBag parameter, DeviceMappingPorts ports)
			throws MappingException, RPIException {

		NetFragment fragment = new NetFragment("youBotToolDevice");

		ToolParameters tp = fragment
				.add(new ToolParameters(actuatorDriver.getDeviceName(), actuatorDriver.getJointCount() - 1));

		FrameToPosRot transComFrame = fragment.add(new FrameToPosRot());
		fragment.connect(actionResult.getCOMOutPort(), transComFrame.getInValue(),
				new RelationDataflow(actuatorDriver.getFlange(), null));
		fragment.connect(transComFrame.getOutPosition(), tp.getInCOM());

		DataflowInPort massPort = fragment.addInPort(new DoubleDataflow(), true, tp.getInMass());
		fragment.connect(actionResult.getMassOutPort(), massPort);

		DataflowInPort moiPort = fragment.addInPort(new VectorDataflow(), true, tp.getInMOI());
		fragment.connect(actionResult.getMOIOutPort(), moiPort);

		DataflowOutPort success = fragment.addOutPort(new StateDataflow(), false, tp.getOutCompleted());

		fragment.addLinkBuilder(new WorldLinkBuilder(runtime));
		return new BaseActuatorDriverMapperResult(actuatorDriver, fragment, success);

	}
}
