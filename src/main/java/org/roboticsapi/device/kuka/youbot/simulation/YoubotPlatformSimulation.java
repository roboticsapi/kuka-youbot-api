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
import org.roboticsapi.core.Dependency.Builder;
import org.roboticsapi.device.kuka.youbot.YoubotPlatform;
import org.roboticsapi.device.kuka.youbot.runtime.rpi.YoubotPlatformSimulationDriver;
import org.roboticsapi.facet.runtime.rpi.mapping.RpiRuntime;
import org.roboticsapi.facet.simulation.SEntity;

public class YoubotPlatformSimulation extends AbstractRoboticsObject {
	private final Dependency<RpiRuntime> runtime;
	private final Dependency<YoubotPlatform> platform;
	private final Dependency<String> subjectName;

	private final Dependency<SYoubotPlatform> sPlatform;
	private final Dependency<YoubotPlatformSimulationDriver> platformDriver;

	private final Dependency<Double> startX;
	private final Dependency<Double> startY;
	private final Dependency<Double> startZ;
	private final Dependency<Double> startA;

	public YoubotPlatformSimulation() {
		platform = createDependency("platform");
		runtime = createDependency("runtime");
		subjectName = createDependency("subjectName");

		sPlatform = createDependency("sPlatform", new Builder<SYoubotPlatform>() {
			public SYoubotPlatform create() {
				SYoubotPlatform sPlatform = new SYoubotPlatform();
				sPlatform.setStartX(startX.get());
				sPlatform.setStartY(startY.get());
				sPlatform.setStartZ(startZ.get());
				sPlatform.setStartA(startA.get());
				sPlatform.setName("s_" + subjectName.get());
				return sPlatform;
			};
		});

		platformDriver = createDependency("platformDriver", new Builder<YoubotPlatformSimulationDriver>() {
			public YoubotPlatformSimulationDriver create() {
				YoubotPlatformSimulationDriver platformDriver = new YoubotPlatformSimulationDriver();
				platformDriver.setDevice(platform.get());
				platformDriver.setRuntime(runtime.get());
				platformDriver.setRpiDeviceName(subjectName.get() + "_platform");
				platformDriver.setSimulation(sPlatform.get().getIdentifier());
				return platformDriver;
			};
		});

		startX = createDependency("startX", 0.);
		startY = createDependency("startY", 0.);
		startZ = createDependency("startZ", 0.);
		startA = createDependency("startA", 0.);
	}

	public RpiRuntime getRuntime() {
		return runtime.get();
	}

	@ConfigurationProperty
	public void setRuntime(RpiRuntime runtime) {
		this.runtime.set(runtime);
	}

	public YoubotPlatform getPlatform() {
		return platform.get();
	}

	@ConfigurationProperty
	public void setPlatform(YoubotPlatform platform) {
		this.platform.set(platform);
	}

	public double getStartX() {
		return startX.get();
	}

	@ConfigurationProperty
	public void setStartX(double startX) {
		this.startX.set(startX);
	}

	public double getStartY() {
		return startY.get();
	}

	@ConfigurationProperty
	public void setStartY(double startY) {
		this.startY.set(startY);
	}

	public double getStartZ() {
		return startY.get();
	}

	@ConfigurationProperty
	public void setStartZ(double startZ) {
		this.startZ.set(startZ);
	}

	public double getStartA() {
		return startA.get();
	}

	@ConfigurationProperty
	public void setStartA(double startA) {
		this.startA.set(startA);
	}

	public String getSubjectName() {
		return subjectName.get();
	}

	@ConfigurationProperty
	public void setSubjectName(String subjectName) {
		this.subjectName.set(subjectName);
	}

	public YoubotPlatformSimulationDriver getPlatformDriver() {
		return platformDriver.get();
	}

	public SYoubotPlatform getSPlatform() {
		return sPlatform.get();
	}

	public SEntity getArmParent() {
		return sPlatform.get();
	}

}
