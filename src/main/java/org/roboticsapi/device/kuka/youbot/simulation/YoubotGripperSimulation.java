/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2013-2019 ISSE, University of Augsburg 
 */

package org.roboticsapi.device.kuka.youbot.simulation;

import org.roboticsapi.configuration.ConfigurationProperty;
import org.roboticsapi.core.AbstractRoboticsObject;
import org.roboticsapi.core.Dependency;
import org.roboticsapi.device.kuka.youbot.YoubotGripper;
import org.roboticsapi.device.kuka.youbot.runtime.rpi.YoubotGripperSimulationDriver;
import org.roboticsapi.facet.runtime.rpi.mapping.RpiRuntime;
import org.roboticsapi.facet.simulation.SEntity;

public class YoubotGripperSimulation extends AbstractRoboticsObject {
	private final Dependency<RpiRuntime> runtime;
	private final Dependency<YoubotGripper> gripper;
	private final Dependency<YoubotGripperSimulationDriver> gripperDriver;
	private final Dependency<SEntity> parent;
	private final Dependency<SYoubotGripper> sGripper;
	private final Dependency<String> deviceName;

	public YoubotGripperSimulation() {
		runtime = createDependency("runtime");
		gripper = createDependency("gripper");
		parent = createDependency("parent");
		deviceName = createDependency("deviceName");

		sGripper = createDependency("sGripper", () -> {
			SYoubotGripper ret = new SYoubotGripper();
			ret.setWorld(getParent().getWorld());
			ret.setParent(getParent());
			return ret;
		});
		gripperDriver = createDependency("gripperDriver", () -> {
			YoubotGripperSimulationDriver ret = new YoubotGripperSimulationDriver();
			ret.setDevice(gripper.get());
			ret.setRuntime(runtime.get());
			ret.setRpiDeviceName(deviceName.get());
			ret.setSimulation(sGripper.get().getIdentifier());
			return ret;
		});
	}

	public String getDeviceName() {
		return deviceName.get();
	}

	@ConfigurationProperty
	public void setDeviceName(String deviceName) {
		this.deviceName.set(deviceName);
	}

	public YoubotGripper getGripper() {
		return gripper.get();
	}

	@ConfigurationProperty
	public void setGripper(YoubotGripper gripper) {
		this.gripper.set(gripper);
	}

	public RpiRuntime getRuntime() {
		return runtime.get();
	}

	@ConfigurationProperty
	public void setRuntime(RpiRuntime runtime) {
		this.runtime.set(runtime);
	}

	public SEntity getParent() {
		return parent.get();
	}

	@ConfigurationProperty
	public void setParent(SEntity parent) {
		this.parent.set(parent);
	}

}
