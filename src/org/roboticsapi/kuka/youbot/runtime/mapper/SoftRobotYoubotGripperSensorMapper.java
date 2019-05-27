/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2010-2017 ISSE, University of Augsburg 
 */

package org.roboticsapi.kuka.youbot.runtime.mapper;

import org.roboticsapi.kuka.youbot.runtime.SoftRobotYoubotGripperDriver;
import org.roboticsapi.kuka.youbot.runtime.primitives.GripperMonitor;
import org.roboticsapi.runtime.SoftRobotRuntime;
import org.roboticsapi.runtime.mapping.MappingException;
import org.roboticsapi.runtime.mapping.net.DataflowOutPort;
import org.roboticsapi.runtime.mapping.net.DoubleDataflow;
import org.roboticsapi.runtime.mapping.net.NetFragment;
import org.roboticsapi.runtime.mapping.parts.SensorMapper;
import org.roboticsapi.runtime.mapping.parts.SensorMappingContext;
import org.roboticsapi.runtime.mapping.parts.SensorMappingPorts;
import org.roboticsapi.runtime.mapping.result.SensorMapperResult;
import org.roboticsapi.runtime.mapping.result.impl.DoubleSensorMapperResult;

public class SoftRobotYoubotGripperSensorMapper
		implements SensorMapper<SoftRobotRuntime, Double, SoftRobotYoubotGripperDriver.OpeningWidthSensor> {

	@Override
	public SensorMapperResult<Double> map(SoftRobotRuntime runtime,
			SoftRobotYoubotGripperDriver.OpeningWidthSensor sensor, SensorMappingPorts ports,
			SensorMappingContext context) throws MappingException {
		final NetFragment ret = new NetFragment("SoftRobotOpeningWidthSensor");
		final GripperMonitor m = new GripperMonitor(sensor.getDriver().getDeviceName());

		ret.add(m);

		DataflowOutPort port = ret.addOutPort(new DoubleDataflow(), true, m.getOutDistance());

		return new DoubleSensorMapperResult(ret, port);

	}
}
