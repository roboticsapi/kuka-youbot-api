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
import org.roboticsapi.device.kuka.youbot.runtime.rpi.YoubotArmGenericDriver;
import org.roboticsapi.device.kuka.youbot.runtime.rpi.primitives.ArmControlStrategy;
import org.roboticsapi.facet.runtime.rpi.RpiException;
import org.roboticsapi.facet.runtime.rpi.mapping.ActuatorDriverMapper;
import org.roboticsapi.facet.runtime.rpi.mapping.ActuatorFragment;
import org.roboticsapi.facet.runtime.rpi.mapping.MapperRegistry;
import org.roboticsapi.facet.runtime.rpi.mapping.MappingException;
import org.roboticsapi.facet.runtime.rpi.mapping.RealtimeValueConsumerFragment;
import org.roboticsapi.framework.multijoint.IllegalJointValueException;
import org.roboticsapi.framework.multijoint.runtime.rpi.result.JointPositionControllerActionResult;

public class YoubotArmPositionControllerMapper
		implements ActuatorDriverMapper<YoubotArmGenericDriver, JointPositionControllerActionResult> {

	private static final int POSITION_CONTROLLER_NUMBER = 0;

	@Override
	public RealtimeValueConsumerFragment map(YoubotArmGenericDriver actuatorDriver,
			JointPositionControllerActionResult actionResult, DeviceParameterBag parameters, MapperRegistry registry,
			RealtimeBoolean cancel, RealtimeDouble override, RealtimeDouble time)
			throws MappingException, RpiException {

		ArmControlStrategy controlStrategy = new ArmControlStrategy(actuatorDriver.getRpiDeviceName(),
				POSITION_CONTROLLER_NUMBER);
		ActuatorFragment ret = new ActuatorFragment(actuatorDriver, controlStrategy.getOutSuccess(), controlStrategy);

		ret.addException(IllegalJointValueException.class, RealtimeBoolean.FALSE);

		// FIXME: these cases can occur, ArmControlStrategy should detect this and
		// provide an OutPort!
		ret.addException(ConcurrentAccessException.class, RealtimeBoolean.FALSE);
		ret.addException(ActuatorNotOperationalException.class, RealtimeBoolean.FALSE);

		for (int i = 0; i < actuatorDriver.getJointCount(); i++) {
			ActuatorFragment child = new ActuatorFragment(actuatorDriver.getJointDriver(i), RealtimeBoolean.FALSE);
			child.addException(ConcurrentAccessException.class, RealtimeBoolean.FALSE);
			child.addException(ActuatorNotOperationalException.class, RealtimeBoolean.FALSE);
			child.addException(IllegalJointValueException.class, RealtimeBoolean.FALSE);
			ret.addWithDependencies(child);
		}

		return ret;
	}

}
