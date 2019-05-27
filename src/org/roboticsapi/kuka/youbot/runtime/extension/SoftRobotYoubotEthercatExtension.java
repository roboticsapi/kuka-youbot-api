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
import org.roboticsapi.kuka.youbot.runtime.EthercatBasedYoubotArmDriver;
import org.roboticsapi.kuka.youbot.runtime.EthercatBasedYoubotGripperDriver;
import org.roboticsapi.kuka.youbot.runtime.EthercatBasedYoubotPlatformDriver;
import org.roboticsapi.platform.PlatformDriver;
import org.roboticsapi.robot.RobotArmDriver;
import org.roboticsapi.runtime.SoftRobotRuntime;
import org.roboticsapi.runtime.driver.DeviceBasedInstantiator;
import org.roboticsapi.runtime.extension.AbstractSoftRobotRoboticsBuilder;
import org.roboticsapi.tool.gripper.GripperDriver;

public class SoftRobotYoubotEthercatExtension extends AbstractSoftRobotRoboticsBuilder {

	private static final String EXTENSION_EC = "kuka_youbot_ec";

	private final Map<EthercatBasedYoubotPlatformDriver, DeviceBasedInstantiator<YoubotPlatform>> pMap = new HashMap<EthercatBasedYoubotPlatformDriver, DeviceBasedInstantiator<YoubotPlatform>>();
	private final Map<EthercatBasedYoubotArmDriver, DeviceBasedInstantiator<YoubotArm>> rMap = new HashMap<EthercatBasedYoubotArmDriver, DeviceBasedInstantiator<YoubotArm>>();
	private final Map<EthercatBasedYoubotGripperDriver, DeviceBasedInstantiator<YoubotGripper>> gMap = new HashMap<EthercatBasedYoubotGripperDriver, DeviceBasedInstantiator<YoubotGripper>>();

	public SoftRobotYoubotEthercatExtension() {
		super(EthercatBasedYoubotArmDriver.class, EthercatBasedYoubotGripperDriver.class,
				EthercatBasedYoubotPlatformDriver.class);
	}

	@Override
	protected String[] getRuntimeExtensions() {
		return new String[] { EXTENSION_EC };
	}

	@Override
	protected void onRoboticsObjectAvailable(RoboticsObject object) {
		if (object instanceof YoubotArm) {
			final YoubotArm device = (YoubotArm) object;
			RobotArmDriver d = device.getDriver();

			if (d instanceof EthercatBasedYoubotArmDriver) {
				final EthercatBasedYoubotArmDriver driver = (EthercatBasedYoubotArmDriver) d;
				final DeviceBasedInstantiator<YoubotArm> loader = new DeviceBasedInstantiator<YoubotArm>(device,
						driver);

				rMap.put(driver, loader);
				driver.addOperationStateListener(loader);
			}
		}

		if (object instanceof YoubotGripper) {
			final YoubotGripper device = (YoubotGripper) object;
			GripperDriver d = device.getDriver();

			if (d instanceof EthercatBasedYoubotGripperDriver) {
				final EthercatBasedYoubotGripperDriver driver = (EthercatBasedYoubotGripperDriver) d;
				final DeviceBasedInstantiator<YoubotGripper> loader = new DeviceBasedInstantiator<YoubotGripper>(device,
						driver);

				gMap.put(driver, loader);
				driver.addOperationStateListener(loader);
			}
		}

		if (object instanceof YoubotPlatform) {
			final YoubotPlatform device = (YoubotPlatform) object;
			PlatformDriver d = device.getDriver();

			if (d instanceof EthercatBasedYoubotPlatformDriver) {
				final EthercatBasedYoubotPlatformDriver driver = (EthercatBasedYoubotPlatformDriver) d;
				final DeviceBasedInstantiator<YoubotPlatform> loader = new DeviceBasedInstantiator<YoubotPlatform>(
						device, driver);

				pMap.put(driver, loader);
				driver.addOperationStateListener(loader);
			}
		}
	}

	@Override
	protected void onRoboticsObjectUnavailable(RoboticsObject object) {
		if (object instanceof EthercatBasedYoubotArmDriver) {
			final EthercatBasedYoubotArmDriver driver = (EthercatBasedYoubotArmDriver) object;
			final DeviceBasedInstantiator<YoubotArm> loader = rMap.remove(driver);

			driver.removeOperationStateListener(loader);
		}

		if (object instanceof EthercatBasedYoubotGripperDriver) {
			final EthercatBasedYoubotGripperDriver driver = (EthercatBasedYoubotGripperDriver) object;
			final DeviceBasedInstantiator<YoubotGripper> loader = gMap.remove(driver);

			driver.removeOperationStateListener(loader);
		}

		if (object instanceof EthercatBasedYoubotPlatformDriver) {
			final EthercatBasedYoubotPlatformDriver driver = (EthercatBasedYoubotPlatformDriver) object;
			final DeviceBasedInstantiator<YoubotPlatform> loader = pMap.remove(driver);

			driver.removeOperationStateListener(loader);
		}
	}

	@Override
	protected void onRuntimeAvailable(SoftRobotRuntime runtime) {

	}

	@Override
	protected void onRuntimeUnavailable(SoftRobotRuntime runtime) {

	}

}
