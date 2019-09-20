/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2013-2019 ISSE, University of Augsburg 
 */

package org.roboticsapi.device.kuka.youbot.runtime.rpi;

import org.roboticsapi.configuration.ConfigurationProperty;
import org.roboticsapi.core.Dependency;
import org.roboticsapi.device.kuka.youbot.YoubotArm;
import org.roboticsapi.facet.runtime.rpi.RpiParameters;
import org.roboticsapi.framework.robot.RobotArmDriver;

/**
 * {@link RobotArmDriver} for a simulated {@link YoubotArm}.
 */
public final class YoubotGripperSimulationDriver extends YoubotGripperGenericDriver {

	private static final String DEVICE_TYPE = "kuka::youbot::gripper::sim";
	private final Dependency<String> simulation;

	public YoubotGripperSimulationDriver() {
		simulation = createDependency("simulation");
	}

	public String getSimulation() {
		return simulation.get();
	}

	@ConfigurationProperty
	public void setSimulation(String simulation) {
		this.simulation.set(simulation);
	}

	@Override
	public String getRpiDeviceType() {
		return DEVICE_TYPE;
	}

	@Override
	protected RpiParameters getRpiDeviceParameters() {
		return super.getRpiDeviceParameters().with("simulation", getSimulation());
	}

	@Override
	protected boolean checkDeviceType(String deviceType) {
		return DEVICE_TYPE.equals(deviceType);
	}

}