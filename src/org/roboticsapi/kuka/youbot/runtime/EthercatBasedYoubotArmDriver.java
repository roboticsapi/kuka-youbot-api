/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2010-2017 ISSE, University of Augsburg 
 */

package org.roboticsapi.kuka.youbot.runtime;

import java.util.HashMap;
import java.util.Map;

import org.roboticsapi.configuration.ConfigurationProperty;
import org.roboticsapi.core.exception.ConfigurationException;
import org.roboticsapi.kuka.youbot.YoubotArm;
import org.roboticsapi.robot.RobotArmDriver;
import org.roboticsapi.runtime.fieldbus.ethercat.EthercatDriver;

/**
 * EtherCAT-based {@link RobotArmDriver} for the {@link YoubotArm}.
 */
public final class EthercatBasedYoubotArmDriver extends SoftRobotYoubotArmDriver {

	private static final String DEVICE_TYPE = "kuka_youbot_arm_ec";
	private EthercatDriver ethercatDriver = null;

	public EthercatBasedYoubotArmDriver() {
		super();
	}

	public EthercatDriver getEthercatDriver() {
		return ethercatDriver;
	}

	@ConfigurationProperty(Optional = false)
	public void setEthercatDriver(EthercatDriver ethercatDriver) {
		immutableWhenInitialized();
		this.ethercatDriver = ethercatDriver;
	}

	@Override
	public String getDeviceType() {
		return DEVICE_TYPE;
	}

	@Override
	public boolean build(YoubotArm t) {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("ethercatDriver", ethercatDriver.getDeviceName());

		return loadDeviceDriver(parameters);
	}

	@Override
	public void delete() {
		deleteDeviceDriver();
	}

	@Override
	protected boolean checkDeviceType(String deviceType) {
		return DEVICE_TYPE.equals(deviceType);
	}

	@Override
	protected void validateConfigurationProperties() throws ConfigurationException {
		super.validateConfigurationProperties();

		checkNotNullAndInitialized("ethercatDriver", ethercatDriver);
	}

}