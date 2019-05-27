/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2010-2017 ISSE, University of Augsburg 
 */

package org.roboticsapi.kuka.youbot.runtime.mapper;

import org.roboticsapi.kuka.youbot.sensor.YoubotArmFrameProjectionSensor;
import org.roboticsapi.runtime.SoftRobotRuntime;
import org.roboticsapi.runtime.mapping.MappingException;
import org.roboticsapi.runtime.mapping.net.DataflowOutPort;
import org.roboticsapi.runtime.mapping.net.NetFragment;
import org.roboticsapi.runtime.mapping.parts.SensorMapper;
import org.roboticsapi.runtime.mapping.parts.SensorMappingContext;
import org.roboticsapi.runtime.mapping.parts.SensorMappingPorts;
import org.roboticsapi.runtime.mapping.result.SensorMapperResult;
import org.roboticsapi.runtime.world.result.TransformationSensorMapperResult;
import org.roboticsapi.world.Transformation;

public abstract class AbstractYoubotArmFrameProjectionSensorMapper<S extends YoubotArmFrameProjectionSensor>
		implements SensorMapper<SoftRobotRuntime, Transformation, S> {

	@Override
	public SensorMapperResult<Transformation> map(SoftRobotRuntime runtime, S sensor, SensorMappingPorts ports,
			SensorMappingContext context) throws MappingException {
		NetFragment fragment = new NetFragment("PositionPreservingFrameProjectionSensor");

		SensorMapperResult<Transformation> mappedToProject = runtime.getMapperRegistry().mapSensor(runtime,
				sensor.getToProject(), fragment, context);

		SensorMapperResult<Transformation> mappedFlangeToMotionCenter = runtime.getMapperRegistry().mapSensor(runtime,
				sensor.getFlangeToMotionCenter(), fragment, context);

		DataflowOutPort projected = addProjector(fragment, mappedToProject.getSensorPort(),
				mappedFlangeToMotionCenter.getSensorPort());

		return new TransformationSensorMapperResult(fragment, projected);
	}

	protected abstract DataflowOutPort addProjector(NetFragment fragment, DataflowOutPort toProject,
			DataflowOutPort flangeToMotionCenter) throws MappingException;
}
