/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2013-2019 ISSE, University of Augsburg 
 */

package org.roboticsapi.device.kuka.youbot.runtime.rpi;

import org.roboticsapi.configuration.ConfigurationProperty;
import org.roboticsapi.core.AbstractRoboticsObject;
import org.roboticsapi.core.Dependency;
import org.roboticsapi.core.Driver;
import org.roboticsapi.device.kuka.youbot.Youbot;
import org.roboticsapi.facet.runtime.rpi.mapping.RpiRuntime;

public class YoubotMockDriver extends AbstractRoboticsObject implements Driver {

	Dependency<RpiRuntime> runtime = createDependency("runtime");
	Dependency<Youbot> youbot = createDependency("youbot");
	Dependency<YoubotArmMockDriver> armDriver = createDependency("armDriver",
			() -> new YoubotArmMockDriver(youbot.get().getArm(), runtime.get()));
	Dependency<YoubotPlatformMockDriver> platformDriver = createDependency("platformDriver",
			() -> new YoubotPlatformMockDriver(youbot.get().getPlatform(), runtime.get()));
	Dependency<YoubotGripperMockDriver> gripperDriver = createDependency("gripperDriver",
			() -> new YoubotGripperMockDriver(youbot.get().getGripper(), runtime.get()));

	public YoubotMockDriver() {
	}

	public YoubotMockDriver(Youbot youbot, RpiRuntime runtime) {
		this();
		setRuntime(runtime);
		setDevice(youbot);
	}

	@Override
	public RpiRuntime getRuntime() {
		return runtime.get();
	}

	@ConfigurationProperty
	public void setRuntime(RpiRuntime runtime) {
		this.runtime.set(runtime);
	}

	public Youbot getDevice() {
		return youbot.get();
	}

	@ConfigurationProperty
	public void setDevice(Youbot youbot) {
		this.youbot.set(youbot);
	}

}
