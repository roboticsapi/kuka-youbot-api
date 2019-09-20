/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2013-2019 ISSE, University of Augsburg 
 */

package org.roboticsapi.device.kuka.youbot.runtime.rpi.mapper;

import org.roboticsapi.core.DeviceParameterBag;
import org.roboticsapi.core.realtimevalue.realtimeboolean.RealtimeBoolean;
import org.roboticsapi.core.realtimevalue.realtimedouble.RealtimeDouble;
import org.roboticsapi.core.world.TransformationException;
import org.roboticsapi.core.world.World;
import org.roboticsapi.device.kuka.youbot.runtime.rpi.YoubotArmGenericDriver;
import org.roboticsapi.facet.runtime.rpi.RpiException;
import org.roboticsapi.facet.runtime.rpi.mapping.ActuatorDriverMapper;
import org.roboticsapi.facet.runtime.rpi.mapping.ActuatorFragment;
import org.roboticsapi.facet.runtime.rpi.mapping.MapperRegistry;
import org.roboticsapi.facet.runtime.rpi.mapping.MappingException;
import org.roboticsapi.facet.runtime.rpi.mapping.RealtimeValueConsumerFragment;
import org.roboticsapi.framework.robot.runtime.rpi.mapper.ToolActionResult;
import org.roboticsapi.framework.robot.runtime.rpi.primitives.ToolParameters;

public class YoubotArmToolMapper implements ActuatorDriverMapper<YoubotArmGenericDriver, ToolActionResult> {

	@Override
	public RealtimeValueConsumerFragment map(YoubotArmGenericDriver actuatorDriver, ToolActionResult actionResult,
			DeviceParameterBag parameters, MapperRegistry registry, RealtimeBoolean cancel, RealtimeDouble override,
			RealtimeDouble time) throws MappingException, RpiException {
		ToolParameters tp = new ToolParameters(actuatorDriver.getRpiDeviceName(), actuatorDriver.getJointCount() - 1);
		ActuatorFragment ret = new ActuatorFragment(actuatorDriver, tp.getOutCompleted());
		try {
			ret.addDependency(
					actionResult.getCenterOfMass().getTransformationForRepresentation(
							actuatorDriver.getDevice().getFlange(), World.getCommandedTopology()).getTranslation(),
					"inCOM", tp.getInCOM());
		} catch (TransformationException e) {
			throw new MappingException(e);
		}
		ret.addDependency(actionResult.getMass(), "inMass", tp.getInMass());
		ret.addDependency(actionResult.getMomentsOfInertia(), "inMOI", tp.getInMOI());
		return ret;
	}
}
