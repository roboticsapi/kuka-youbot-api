/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2013-2019 ISSE, University of Augsburg 
 */

package org.roboticsapi.device.kuka.youbot.runtime.rpi;

import org.roboticsapi.configuration.ConfigurationProperty;
import org.roboticsapi.core.Dependency;
import org.roboticsapi.facet.runtime.rpi.RpiParameters;
import org.roboticsapi.framework.platform.runtime.rpi.driver.MecanumPlatformGenericDriver;

public class YoubotPlatformSimulationDriver extends MecanumPlatformGenericDriver {

	private static final String DEVICE_TYPE = "kuka::youbot::platform::sim";
	private final Dependency<String> simulation;

	public YoubotPlatformSimulationDriver() {
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
		return super.getRpiDeviceParameters().with("simulation", simulation.get());
	}

	@Override
	protected boolean checkDeviceType(String deviceType) {
		return DEVICE_TYPE.equals(deviceType);
	}

}
