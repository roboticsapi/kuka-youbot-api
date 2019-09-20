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
import org.roboticsapi.device.kuka.youbot.YoubotArm;
import org.roboticsapi.device.kuka.youbot.runtime.rpi.YoubotArmSimulationDriver;
import org.roboticsapi.facet.runtime.rpi.mapping.RpiRuntime;
import org.roboticsapi.facet.simulation.SEntity;

public class YoubotArmSimulation extends AbstractRoboticsObject {
	final Dependency<RpiRuntime> runtime = createDependency("runtime");
	final Dependency<YoubotArm> arm = createDependency("arm");
	final Dependency<String> deviceName = createDependency("deviceName");
	final Dependency<SEntity> parent = createDependency("parent");
	final Dependency<SYoubotArm> sArm = createDependency("sArm", () -> {
		SYoubotArm ret = new SYoubotArm();
		ret.setWorld(getParent().getWorld());
		ret.setParent(getParent());
		return ret;
	});
	final Dependency<YoubotArmSimulationDriver> armDriver = createDependency("armDriver", () -> {
		YoubotArmSimulationDriver ret = new YoubotArmSimulationDriver();
		ret.setDevice(arm.get());
		ret.setRuntime(runtime.get());
		ret.setRpiDeviceName(deviceName.get());
		ret.setSimulation(sArm.get().getIdentifier());
		return ret;
	});

	public YoubotArmSimulation() {
	}

	public String getDeviceName() {
		return deviceName.get();
	}

	@ConfigurationProperty
	public void setDeviceName(String deviceName) {
		this.deviceName.set(deviceName);
	}

	public YoubotArm getArm() {
		return arm.get();
	}

	@ConfigurationProperty
	public void setArm(YoubotArm arm) {
		this.arm.set(arm);
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

	public SEntity getGripperParent() {
		return sArm.get();
	}
}
