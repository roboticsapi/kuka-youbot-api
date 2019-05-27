/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2010-2017 ISSE, University of Augsburg 
 */

package org.roboticsapi.kuka.youbot.runtime.mapper;

import org.roboticsapi.kuka.youbot.runtime.primitives.PositionPreservingProject;
import org.roboticsapi.kuka.youbot.sensor.PositionPreservingFrameProjectionSensor;
import org.roboticsapi.runtime.mapping.MappingException;
import org.roboticsapi.runtime.mapping.net.DataflowOutPort;
import org.roboticsapi.runtime.mapping.net.NetFragment;
import org.roboticsapi.runtime.world.dataflow.TransformationDataflow;

public class YoubotArmPositionPreservingFrameProjectionSensorMapper
		extends AbstractYoubotArmFrameProjectionSensorMapper<PositionPreservingFrameProjectionSensor> {

	@Override
	protected DataflowOutPort addProjector(NetFragment fragment, DataflowOutPort toProject,
			DataflowOutPort flangeToMotionCenter) throws MappingException {

		PositionPreservingProject project = fragment.add(new PositionPreservingProject());

		fragment.connect(toProject, project.getInFlange(), toProject.getType());

		fragment.connect(flangeToMotionCenter, project.getInMotionCenter(), flangeToMotionCenter.getType());

		return fragment.addOutPort(new TransformationDataflow(), true, project.getOutValue());
	}

}
