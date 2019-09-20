/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2013-2019 ISSE, University of Augsburg 
 */

package org.roboticsapi.device.kuka.youbot.runtime.rpi.mapper;

import org.roboticsapi.core.world.Transformation;
import org.roboticsapi.device.kuka.youbot.runtime.rpi.primitives.PositionPreservingProject;
import org.roboticsapi.device.kuka.youbot.sensor.PositionPreservingFrameProjectionSensor;
import org.roboticsapi.facet.runtime.rpi.RpiException;
import org.roboticsapi.facet.runtime.rpi.mapping.MappingException;
import org.roboticsapi.facet.runtime.rpi.mapping.RealtimeValueFragment;
import org.roboticsapi.facet.runtime.rpi.mapping.TypedRealtimeValueFragmentFactory;
import org.roboticsapi.facet.runtime.rpi.mapping.world.RealtimeTransformationFragment;

public class YoubotArmPositionPreservingFrameProjectionSensorMapper
		extends TypedRealtimeValueFragmentFactory<Transformation, PositionPreservingFrameProjectionSensor> {

	public YoubotArmPositionPreservingFrameProjectionSensorMapper() {
		super(PositionPreservingFrameProjectionSensor.class);
	}

	@Override
	protected RealtimeValueFragment<Transformation> createFragment(PositionPreservingFrameProjectionSensor value)
			throws MappingException, RpiException {

		RealtimeTransformationFragment ret = new RealtimeTransformationFragment(value);
		PositionPreservingProject project = ret.add(new PositionPreservingProject());

		ret.addDependency(value.getToProject(), "inFlange", project.getInFlange());

		ret.addDependency(value.getFlangeToMotionCenter(), "inMotionCenter", project.getInMotionCenter());

		ret.defineResult(project.getOutValue());
		return ret;
	}

}
