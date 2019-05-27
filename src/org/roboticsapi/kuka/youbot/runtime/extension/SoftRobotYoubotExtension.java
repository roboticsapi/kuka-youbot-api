/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2010-2017 ISSE, University of Augsburg 
 */

package org.roboticsapi.kuka.youbot.runtime.extension;

import java.util.HashMap;
import java.util.Map;

import org.roboticsapi.core.RoboticsObject;
import org.roboticsapi.kuka.youbot.YoubotArm;
import org.roboticsapi.kuka.youbot.YoubotGripper;
import org.roboticsapi.kuka.youbot.YoubotPlatform;
import org.roboticsapi.kuka.youbot.action.BlendControllerAction;
import org.roboticsapi.kuka.youbot.action.MoveGripper;
import org.roboticsapi.kuka.youbot.action.SwitchControllerAction;
import org.roboticsapi.kuka.youbot.action.SwitchToolAction;
import org.roboticsapi.kuka.youbot.runtime.SimulatedYoubotArmDriver;
import org.roboticsapi.kuka.youbot.runtime.SimulatedYoubotGripperDriver;
import org.roboticsapi.kuka.youbot.runtime.SimulatedYoubotPlatformCartesianPositionDriver;
import org.roboticsapi.kuka.youbot.runtime.SimulatedYoubotPlatformDriver;
import org.roboticsapi.kuka.youbot.runtime.SoftRobotYoubotArmDriver;
import org.roboticsapi.kuka.youbot.runtime.SoftRobotYoubotGripperDriver;
import org.roboticsapi.kuka.youbot.runtime.mapper.GripperGoalActionResult;
import org.roboticsapi.kuka.youbot.runtime.mapper.JointImpedanceActionResult;
import org.roboticsapi.kuka.youbot.runtime.mapper.PositionControllerActionResult;
import org.roboticsapi.kuka.youbot.runtime.mapper.SoftRobotBlendControllerJointImpedanceMapper;
import org.roboticsapi.kuka.youbot.runtime.mapper.SoftRobotMoveGripperMapper;
import org.roboticsapi.kuka.youbot.runtime.mapper.SoftRobotSwitchControllerJointImpedanceMapper;
import org.roboticsapi.kuka.youbot.runtime.mapper.SoftRobotSwitchControllerPositionMapper;
import org.roboticsapi.kuka.youbot.runtime.mapper.SoftRobotYoubotArmDriverJointGoalMapper;
import org.roboticsapi.kuka.youbot.runtime.mapper.SoftRobotYoubotArmDriverJointImpedanceMapper;
import org.roboticsapi.kuka.youbot.runtime.mapper.SoftRobotYoubotArmDriverJointPositionMapper;
import org.roboticsapi.kuka.youbot.runtime.mapper.SoftRobotYoubotArmDriverPositionControllerMapper;
import org.roboticsapi.kuka.youbot.runtime.mapper.SoftRobotYoubotGripperDriverMapper;
import org.roboticsapi.kuka.youbot.runtime.mapper.SoftRobotYoubotGripperSensorMapper;
import org.roboticsapi.kuka.youbot.runtime.mapper.SoftRobotYoubotSwitchToolMapper;
import org.roboticsapi.kuka.youbot.runtime.mapper.SoftRobotYoubotToolDeviceMapper;
import org.roboticsapi.kuka.youbot.runtime.mapper.ToolActionResult;
import org.roboticsapi.kuka.youbot.runtime.mapper.YoubotArmOrientationPreservingFrameProjectionSensorMapper;
import org.roboticsapi.kuka.youbot.runtime.mapper.YoubotArmPalletizingFrameProjectionSensorMapper;
import org.roboticsapi.kuka.youbot.runtime.mapper.YoubotArmPositionPreservingFrameProjectionSensorMapper;
import org.roboticsapi.kuka.youbot.sensor.OrientationPreservingFrameProjectionSensor;
import org.roboticsapi.kuka.youbot.sensor.PalletizingFrameProjectionSensor;
import org.roboticsapi.kuka.youbot.sensor.PositionPreservingFrameProjectionSensor;
import org.roboticsapi.platform.PlatformDriver;
import org.roboticsapi.robot.RobotArmDriver;
import org.roboticsapi.runtime.SoftRobotRuntime;
import org.roboticsapi.runtime.cartesianmotion.mapper.CartesianGoalActionResult;
import org.roboticsapi.runtime.cartesianmotion.mapper.CartesianGoalToCartesianPositionMapper;
import org.roboticsapi.runtime.driver.DeviceBasedInstantiator;
import org.roboticsapi.runtime.extension.AbstractSoftRobotRoboticsBuilder;
import org.roboticsapi.runtime.mapping.MapperRegistry;
import org.roboticsapi.runtime.multijoint.mapper.JointGoalActionResult;
import org.roboticsapi.runtime.multijoint.mapper.JointPositionActionResult;
import org.roboticsapi.tool.gripper.GripperDriver;

public class SoftRobotYoubotExtension extends AbstractSoftRobotRoboticsBuilder {

	private static final String EXTENSION = "kuka_youbot";
	private static final String EXTENSION_SIM = "kuka_youbot_sim";

	private final Map<SimulatedYoubotPlatformCartesianPositionDriver, DeviceBasedInstantiator<YoubotPlatform>> pMap = new HashMap<SimulatedYoubotPlatformCartesianPositionDriver, DeviceBasedInstantiator<YoubotPlatform>>();
	private final Map<SimulatedYoubotPlatformDriver, DeviceBasedInstantiator<YoubotPlatform>> bMap = new HashMap<SimulatedYoubotPlatformDriver, DeviceBasedInstantiator<YoubotPlatform>>();

	private final Map<SimulatedYoubotArmDriver, DeviceBasedInstantiator<YoubotArm>> rMap = new HashMap<SimulatedYoubotArmDriver, DeviceBasedInstantiator<YoubotArm>>();
	private final Map<SimulatedYoubotGripperDriver, DeviceBasedInstantiator<YoubotGripper>> gMap = new HashMap<SimulatedYoubotGripperDriver, DeviceBasedInstantiator<YoubotGripper>>();

	public SoftRobotYoubotExtension() {
		super(SoftRobotYoubotGripperDriver.class, SoftRobotYoubotArmDriver.class, SimulatedYoubotArmDriver.class,
				SimulatedYoubotGripperDriver.class, SimulatedYoubotPlatformDriver.class,
				SimulatedYoubotPlatformCartesianPositionDriver.class);
	}

	@Override
	protected String[] getRuntimeExtensions() {
		return new String[] { EXTENSION, EXTENSION_SIM };
	}

	@Override
	protected void onRoboticsObjectAvailable(RoboticsObject object) {
		if (object instanceof YoubotArm) {
			final YoubotArm device = (YoubotArm) object;
			RobotArmDriver d = device.getDriver();

			if (d instanceof SimulatedYoubotArmDriver) {
				final SimulatedYoubotArmDriver driver = (SimulatedYoubotArmDriver) d;
				final DeviceBasedInstantiator<YoubotArm> loader = new DeviceBasedInstantiator<YoubotArm>(device,
						driver);

				rMap.put(driver, loader);
				driver.addOperationStateListener(loader);
			}
		}

		if (object instanceof YoubotGripper) {
			final YoubotGripper device = (YoubotGripper) object;
			GripperDriver d = device.getDriver();

			if (d instanceof SimulatedYoubotGripperDriver) {
				final SimulatedYoubotGripperDriver driver = (SimulatedYoubotGripperDriver) d;
				final DeviceBasedInstantiator<YoubotGripper> loader = new DeviceBasedInstantiator<YoubotGripper>(device,
						driver);

				gMap.put(driver, loader);
				driver.addOperationStateListener(loader);
			}
		}

		if (object instanceof YoubotPlatform) {
			final YoubotPlatform device = (YoubotPlatform) object;
			PlatformDriver d = device.getDriver();

			if (d instanceof SimulatedYoubotPlatformDriver) {
				final SimulatedYoubotPlatformDriver driver = (SimulatedYoubotPlatformDriver) d;
				final DeviceBasedInstantiator<YoubotPlatform> loader = new DeviceBasedInstantiator<YoubotPlatform>(
						device, driver);

				bMap.put(driver, loader);
				driver.addOperationStateListener(loader);
			}

			if (d instanceof SimulatedYoubotPlatformCartesianPositionDriver) {
				final SimulatedYoubotPlatformCartesianPositionDriver driver = (SimulatedYoubotPlatformCartesianPositionDriver) d;
				final DeviceBasedInstantiator<YoubotPlatform> loader = new DeviceBasedInstantiator<YoubotPlatform>(
						device, driver);

				pMap.put(driver, loader);
				driver.addOperationStateListener(loader);
			}
		}
	}

	@Override
	protected void onRoboticsObjectUnavailable(RoboticsObject object) {
		if (object instanceof SimulatedYoubotArmDriver) {
			final SimulatedYoubotArmDriver driver = (SimulatedYoubotArmDriver) object;
			final DeviceBasedInstantiator<YoubotArm> loader = rMap.remove(driver);

			driver.removeOperationStateListener(loader);
		}

		if (object instanceof SimulatedYoubotGripperDriver) {
			final SimulatedYoubotGripperDriver driver = (SimulatedYoubotGripperDriver) object;
			final DeviceBasedInstantiator<YoubotGripper> loader = gMap.remove(driver);

			driver.removeOperationStateListener(loader);
		}

		if (object instanceof SimulatedYoubotPlatformDriver) {
			final SimulatedYoubotPlatformDriver driver = (SimulatedYoubotPlatformDriver) object;
			final DeviceBasedInstantiator<YoubotPlatform> loader = bMap.remove(driver);

			driver.removeOperationStateListener(loader);
		}

		if (object instanceof SimulatedYoubotPlatformCartesianPositionDriver) {
			final SimulatedYoubotPlatformCartesianPositionDriver driver = (SimulatedYoubotPlatformCartesianPositionDriver) object;
			final DeviceBasedInstantiator<YoubotPlatform> loader = pMap.remove(driver);

			driver.removeOperationStateListener(loader);
		}
	}

	@Override
	protected void onRuntimeAvailable(SoftRobotRuntime runtime) {
		MapperRegistry registry = runtime.getMapperRegistry();

		registry.registerActuatorDriverMapper(SoftRobotRuntime.class, SoftRobotYoubotGripperDriver.class,
				GripperGoalActionResult.class, new SoftRobotYoubotGripperDriverMapper());

		registry.registerActionMapper(SoftRobotRuntime.class, MoveGripper.class, new SoftRobotMoveGripperMapper());

		registry.registerActionMapper(SoftRobotRuntime.class, SwitchControllerAction.class,
				new SoftRobotSwitchControllerJointImpedanceMapper());
		registry.registerActuatorDriverMapper(SoftRobotRuntime.class, SoftRobotYoubotArmDriver.class,
				JointImpedanceActionResult.class, new SoftRobotYoubotArmDriverJointImpedanceMapper());

		registry.registerActionMapper(SoftRobotRuntime.class, SwitchControllerAction.class,
				new SoftRobotSwitchControllerPositionMapper());
		registry.registerActuatorDriverMapper(SoftRobotRuntime.class, SoftRobotYoubotArmDriver.class,
				PositionControllerActionResult.class, new SoftRobotYoubotArmDriverPositionControllerMapper());

		// registry.registerActuatorDriverMapper(SoftRobotRuntime.class,
		// SoftRobotYoubotArmDriver.class, CartesianGoalActionResult.class,
		// new CartesianGoalToCartesianVelocityMapper(0.002, 0.002, 0.02,
		// 0.02));
		// registry.registerActuatorDriverMapper(SoftRobotRuntime.class,
		// SoftRobotYoubotArmDriver.class,
		// CartesianGoalActionResult.class,
		// new SoftRobotRobotArmDriverCartesianGoalMapper());

		registry.registerActuatorDriverMapper(SoftRobotRuntime.class, SoftRobotYoubotArmDriver.class,
				CartesianGoalActionResult.class, new CartesianGoalToCartesianPositionMapper(0.01, 0.1, 0.2, 0.2));

		registry.registerActuatorDriverMapper(SoftRobotRuntime.class, SoftRobotYoubotArmDriver.class,
				JointPositionActionResult.class,
				new SoftRobotYoubotArmDriverJointPositionMapper<SoftRobotYoubotArmDriver>());
		registry.registerActuatorDriverMapper(SoftRobotRuntime.class, SoftRobotYoubotArmDriver.class,
				JointGoalActionResult.class, new SoftRobotYoubotArmDriverJointGoalMapper<SoftRobotYoubotArmDriver>());

		registry.registerActionMapper(SoftRobotRuntime.class, BlendControllerAction.class,
				new SoftRobotBlendControllerJointImpedanceMapper());

		registry.registerActionMapper(SoftRobotRuntime.class, SwitchToolAction.class,
				new SoftRobotYoubotSwitchToolMapper());
		registry.registerActuatorDriverMapper(SoftRobotRuntime.class, SoftRobotYoubotArmDriver.class,
				ToolActionResult.class, new SoftRobotYoubotToolDeviceMapper());

		registry.registerSensorMapper(SoftRobotRuntime.class, PositionPreservingFrameProjectionSensor.class,
				new YoubotArmPositionPreservingFrameProjectionSensorMapper());

		registry.registerSensorMapper(SoftRobotRuntime.class, PalletizingFrameProjectionSensor.class,
				new YoubotArmPalletizingFrameProjectionSensorMapper());

		registry.registerSensorMapper(SoftRobotRuntime.class, OrientationPreservingFrameProjectionSensor.class,
				new YoubotArmOrientationPreservingFrameProjectionSensorMapper());

		registry.registerSensorMapper(SoftRobotRuntime.class, SoftRobotYoubotGripperDriver.OpeningWidthSensor.class,
				new SoftRobotYoubotGripperSensorMapper());
	}

	@Override
	protected void onRuntimeUnavailable(SoftRobotRuntime runtime) {

	}

}
