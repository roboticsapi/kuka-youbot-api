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
import org.roboticsapi.device.kuka.youbot.runtime.rpi.primitives.JointImpParameters;
import org.roboticsapi.facet.runtime.rpi.RpiException;
import org.roboticsapi.facet.runtime.rpi.mapping.ActuatorDriverMapper;
import org.roboticsapi.facet.runtime.rpi.mapping.ActuatorFragment;
import org.roboticsapi.facet.runtime.rpi.mapping.MapperRegistry;
import org.roboticsapi.facet.runtime.rpi.mapping.MappingException;
import org.roboticsapi.facet.runtime.rpi.mapping.RealtimeValueConsumerFragment;
import org.roboticsapi.framework.multijoint.IllegalJointValueException;
import org.roboticsapi.framework.multijoint.runtime.rpi.result.JointImpedanceControllerActionResult;

public class YoubotArmJointImpedanceMapper
		implements ActuatorDriverMapper<YoubotArmGenericDriver, JointImpedanceControllerActionResult> {

	private static final int NUM_AXIS = 5;
	private static final int IMPEDANCE_CONTROLLER_NUMBER = 1;

	@Override
	public RealtimeValueConsumerFragment map(YoubotArmGenericDriver actuatorDriver,
			JointImpedanceControllerActionResult actionResult, DeviceParameterBag parameters, MapperRegistry registry,
			RealtimeBoolean cancel, RealtimeDouble override, RealtimeDouble time)
			throws MappingException, RpiException {

		if (actionResult.getJointCount() != NUM_AXIS)
			throw new MappingException(
					"Invalid number of joints (got " + actionResult.getJointCount() + ", expected " + NUM_AXIS + ")");

		ArmControlStrategy controlStrategy = new ArmControlStrategy(actuatorDriver.getRpiDeviceName(),
				IMPEDANCE_CONTROLLER_NUMBER);

		ActuatorFragment ret = new ActuatorFragment(actuatorDriver, controlStrategy.getOutSuccess(), controlStrategy);

		for (int i = 0; i < NUM_AXIS; i++) {
			JointImpParameters parameter = ret.add(new JointImpParameters(actuatorDriver.getRpiDeviceName(), i));
			ret.addDependency(actionResult.getStiffness(i), "inStiffness" + i + "", parameter.getInStiffness());
			ret.addDependency(actionResult.getDamping(i), "inDamping" + i + "", parameter.getInDamping());
			ret.addDependency(actionResult.getAddTorque(i), "inAddTorque" + i + "", parameter.getInAddTorque());
			parameter.setMaxTorque(actionResult.getMaxTorques()[i]);
		}

		ret.addException(IllegalJointValueException.class, RealtimeBoolean.FALSE);

		// FIXME: these cases can occur, ArmControlStrategy should detect this and
		// provide an OutPort!
		ret.addException(ConcurrentAccessException.class, RealtimeBoolean.FALSE);
		ret.addException(ActuatorNotOperationalException.class, RealtimeBoolean.FALSE);

		for (int i = 0; i < NUM_AXIS; i++) {
			ActuatorFragment child = new ActuatorFragment(actuatorDriver.getJointDriver(i), RealtimeBoolean.TRUE);
			child.addException(ConcurrentAccessException.class, RealtimeBoolean.FALSE);
			child.addException(ActuatorNotOperationalException.class, RealtimeBoolean.FALSE);
			child.addException(IllegalJointValueException.class, RealtimeBoolean.FALSE);
			ret.addWithDependencies(child);
		}

		return ret;
	}
}
