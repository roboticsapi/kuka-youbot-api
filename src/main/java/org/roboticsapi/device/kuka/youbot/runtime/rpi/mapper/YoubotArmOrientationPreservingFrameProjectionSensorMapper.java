/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2013-2019 ISSE, University of Augsburg 
 */

package org.roboticsapi.device.kuka.youbot.runtime.rpi.mapper;

import org.roboticsapi.core.world.Transformation;
import org.roboticsapi.device.kuka.youbot.runtime.rpi.primitives.OrientationPreservingProject;
import org.roboticsapi.device.kuka.youbot.sensor.OrientationPreservingFrameProjectionSensor;
import org.roboticsapi.facet.runtime.rpi.RpiException;
import org.roboticsapi.facet.runtime.rpi.mapping.MappingException;
import org.roboticsapi.facet.runtime.rpi.mapping.RealtimeValueFragment;
import org.roboticsapi.facet.runtime.rpi.mapping.TypedRealtimeValueFragmentFactory;
import org.roboticsapi.facet.runtime.rpi.mapping.world.RealtimeTransformationFragment;

public class YoubotArmOrientationPreservingFrameProjectionSensorMapper
		extends TypedRealtimeValueFragmentFactory<Transformation, OrientationPreservingFrameProjectionSensor> {

	public YoubotArmOrientationPreservingFrameProjectionSensorMapper() {
		super(OrientationPreservingFrameProjectionSensor.class);
	}

	@Override
	protected RealtimeValueFragment<Transformation> createFragment(OrientationPreservingFrameProjectionSensor value)
			throws MappingException, RpiException {

		RealtimeTransformationFragment ret = new RealtimeTransformationFragment(value);
		OrientationPreservingProject project = ret.add(new OrientationPreservingProject());

		ret.addDependency(value.getToProject(), "inFlange", project.getInFlange());

		ret.addDependency(value.getFlangeToMotionCenter(), "inMotionCenter", project.getInMotionCenter());

		ret.defineResult(project.getOutValue());
		return ret;
	}
}
