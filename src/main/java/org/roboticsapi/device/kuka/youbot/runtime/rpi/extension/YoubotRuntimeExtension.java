/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2013-2019 ISSE, University of Augsburg 
 */

package org.roboticsapi.device.kuka.youbot.runtime.rpi.extension;

import org.roboticsapi.device.kuka.youbot.action.MoveGripper;
import org.roboticsapi.device.kuka.youbot.runtime.rpi.YoubotArmGenericDriver;
import org.roboticsapi.device.kuka.youbot.runtime.rpi.YoubotArmMockDriver;
import org.roboticsapi.device.kuka.youbot.runtime.rpi.YoubotArmSimulationDriver;
import org.roboticsapi.device.kuka.youbot.runtime.rpi.YoubotGripperGenericDriver;
import org.roboticsapi.device.kuka.youbot.runtime.rpi.YoubotGripperMockDriver;
import org.roboticsapi.device.kuka.youbot.runtime.rpi.YoubotGripperSimulationDriver;
import org.roboticsapi.device.kuka.youbot.runtime.rpi.YoubotMockDriver;
import org.roboticsapi.device.kuka.youbot.runtime.rpi.YoubotPlatformMockDriver;
import org.roboticsapi.device.kuka.youbot.runtime.rpi.YoubotPlatformSimulationDriver;
import org.roboticsapi.device.kuka.youbot.runtime.rpi.mapper.GripperGoalActionResult;
import org.roboticsapi.device.kuka.youbot.runtime.rpi.mapper.MoveGripperMapper;
import org.roboticsapi.device.kuka.youbot.runtime.rpi.mapper.YoubotGripperSensorMapper;
import org.roboticsapi.device.kuka.youbot.runtime.rpi.mapper.YoubotArmJointImpedanceMapper;
import org.roboticsapi.device.kuka.youbot.runtime.rpi.mapper.YoubotArmOrientationPreservingFrameProjectionSensorMapper;
import org.roboticsapi.device.kuka.youbot.runtime.rpi.mapper.YoubotArmPositionControllerMapper;
import org.roboticsapi.device.kuka.youbot.runtime.rpi.mapper.YoubotArmPositionPreservingFrameProjectionSensorMapper;
import org.roboticsapi.device.kuka.youbot.runtime.rpi.mapper.YoubotArmToolMapper;
import org.roboticsapi.device.kuka.youbot.runtime.rpi.mapper.YoubotGripperGripperGoalMapper;
import org.roboticsapi.facet.runtime.rpi.extension.RpiExtension;
import org.roboticsapi.facet.runtime.rpi.mapping.MapperRegistry;
import org.roboticsapi.framework.multijoint.runtime.rpi.result.JointImpedanceControllerActionResult;
import org.roboticsapi.framework.multijoint.runtime.rpi.result.JointPositionControllerActionResult;
import org.roboticsapi.framework.robot.runtime.rpi.mapper.ToolActionResult;

public class YoubotRuntimeExtension extends RpiExtension {

	public YoubotRuntimeExtension() {
		super(YoubotPlatformSimulationDriver.class, YoubotPlatformMockDriver.class,
				YoubotGripperGenericDriver.class, YoubotArmGenericDriver.class, YoubotArmMockDriver.class,
				YoubotArmSimulationDriver.class, YoubotGripperMockDriver.class,
				YoubotGripperSimulationDriver.class, YoubotMockDriver.class);
	}

	@Override
	protected void registerMappers(MapperRegistry mr) {

		mr.registerActuatorDriverMapper(YoubotGripperGenericDriver.class, GripperGoalActionResult.class,
				new YoubotGripperGripperGoalMapper());

		mr.registerActuatorDriverMapper(YoubotArmGenericDriver.class, JointImpedanceControllerActionResult.class,
				new YoubotArmJointImpedanceMapper());
		mr.registerActuatorDriverMapper(YoubotArmGenericDriver.class, JointPositionControllerActionResult.class,
				new YoubotArmPositionControllerMapper());
		mr.registerActuatorDriverMapper(YoubotArmGenericDriver.class, ToolActionResult.class,
				new YoubotArmToolMapper());

		// gripper
		mr.registerActionMapper(MoveGripper.class, new MoveGripperMapper());
		mr.registerRealtimeValueMapper(new YoubotGripperSensorMapper());

		// arm
		mr.registerRealtimeValueMapper(new YoubotArmPositionPreservingFrameProjectionSensorMapper());
		mr.registerRealtimeValueMapper(new YoubotArmOrientationPreservingFrameProjectionSensorMapper());

	}

	@Override
	protected void unregisterMappers(MapperRegistry mr) {

	}

}
