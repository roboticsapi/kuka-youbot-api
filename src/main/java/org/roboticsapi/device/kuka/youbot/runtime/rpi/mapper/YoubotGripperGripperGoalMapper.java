/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2013-2019 ISSE, University of Augsburg 
 */

package org.roboticsapi.device.kuka.youbot.runtime.rpi.mapper;

import org.roboticsapi.core.DeviceParameterBag;
import org.roboticsapi.core.actuator.ActuatorNotOperationalException;
import org.roboticsapi.core.actuator.ConcurrentAccessException;
import org.roboticsapi.core.realtimevalue.realtimeboolean.RealtimeBoolean;
import org.roboticsapi.core.realtimevalue.realtimedouble.RealtimeDouble;
import org.roboticsapi.device.kuka.youbot.YoubotGripper;
import org.roboticsapi.device.kuka.youbot.runtime.rpi.primitives.Gripper;
import org.roboticsapi.facet.runtime.rpi.RpiException;
import org.roboticsapi.facet.runtime.rpi.mapping.ActuatorDriverMapper;
import org.roboticsapi.facet.runtime.rpi.mapping.ActuatorFragment;
import org.roboticsapi.facet.runtime.rpi.mapping.MapperRegistry;
import org.roboticsapi.facet.runtime.rpi.mapping.MappingException;
import org.roboticsapi.facet.runtime.rpi.mapping.NamedActuatorDriver;
import org.roboticsapi.facet.runtime.rpi.mapping.RealtimeValueConsumerFragment;

public class YoubotGripperGripperGoalMapper
		implements ActuatorDriverMapper<NamedActuatorDriver<YoubotGripper>, GripperGoalActionResult> {

	@Override
	public RealtimeValueConsumerFragment map(NamedActuatorDriver<YoubotGripper> actuatorDriver,
			GripperGoalActionResult actionResult, DeviceParameterBag parameters, MapperRegistry registry,
			RealtimeBoolean cancel, RealtimeDouble override, RealtimeDouble time)
			throws MappingException, RpiException {

		Gripper gripper = new Gripper(actionResult.getDistance(), actuatorDriver.getRpiDeviceName());
		ActuatorFragment ret = new ActuatorFragment(actuatorDriver, gripper.getOutCompleted());
		ret.addException(ConcurrentAccessException.class, RealtimeBoolean.FALSE);
		ret.addException(ActuatorNotOperationalException.class, RealtimeBoolean.FALSE);
		return ret;

	}

}
