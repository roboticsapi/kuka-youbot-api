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
import org.roboticsapi.device.kuka.youbot.Youbot;
import org.roboticsapi.facet.runtime.rpi.mapping.RpiRuntime;

public class YoubotSimulation extends AbstractRoboticsObject {

	private final Dependency<YoubotArmSimulation> armSim;
	private final Dependency<YoubotPlatformSimulation> platformSim;
	private final Dependency<YoubotGripperSimulation> gripperSim;

	private final Dependency<Youbot> youbot;

	private final Dependency<RpiRuntime> runtime;

	private final Dependency<String> subjectName;

	public YoubotSimulation() {
		youbot = createDependency("youbot");
		runtime = createDependency("runtime");

		subjectName = createDependency("subjectName");

		platformSim = createDependency("platformSim", () -> {
			YoubotPlatformSimulation ret = new YoubotPlatformSimulation();
			ret.setPlatform(getYoubot().getPlatform());
			ret.setSubjectName(getSubjectName());
			ret.setRuntime(getRuntime());
			return ret;
		});

		armSim = createDependency("armSim", () -> {
			YoubotArmSimulation ret = new YoubotArmSimulation();
			ret.setArm(getYoubot().getArm());
			ret.setDeviceName(getSubjectName() + "_arm");
			ret.setRuntime(getRuntime());
			ret.setParent(platformSim.get().getArmParent());
			return ret;
		});

		gripperSim = createDependency("gripperSim", () -> {
			YoubotGripperSimulation ret = new YoubotGripperSimulation();
			ret.setGripper(getYoubot().getGripper());
			ret.setDeviceName(getSubjectName() + "_gripper");
			ret.setRuntime(getRuntime());
			ret.setParent(armSim.get().getGripperParent());
			return ret;
		});
	}

	public Youbot getYoubot() {
		return youbot.get();
	}

	@ConfigurationProperty
	public void setYoubot(Youbot youbot) {
		this.youbot.set(youbot);
	}

	public RpiRuntime getRuntime() {
		return runtime.get();
	}

	@ConfigurationProperty
	public void setRuntime(RpiRuntime runtime) {
		this.runtime.set(runtime);
	}

	public String getSubjectName() {
		return subjectName.get();
	}

	@ConfigurationProperty
	public void setSubjectName(String subjectName) {
		this.subjectName.set(subjectName);
	}
}
