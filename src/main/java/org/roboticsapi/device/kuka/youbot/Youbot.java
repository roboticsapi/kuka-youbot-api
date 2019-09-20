/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2013-2019 ISSE, University of Augsburg 
 */

package org.roboticsapi.device.kuka.youbot;

import org.roboticsapi.core.AbstractRoboticsObject;
import org.roboticsapi.core.Dependency;
import org.roboticsapi.core.world.Frame;
import org.roboticsapi.core.world.Transformation;
import org.roboticsapi.core.world.relation.StaticConnection;
import org.roboticsapi.core.world.relation.StaticPosition;

public class Youbot extends AbstractRoboticsObject {
	private final Dependency<YoubotPlatform> platform;
	private final Dependency<YoubotArm> arm;
	private final Dependency<YoubotGripper> gripper;

	public Youbot() {
		platform = createDependency("platform", () -> {
			YoubotPlatform ret = new YoubotPlatform();
			ret.setName(getName() + " platform");
			return ret;
		});
		arm = createDependency("arm", () -> {
			YoubotArm ret = new YoubotArm();
			ret.setName(getName() + " arm");
			return ret;
		});
		gripper = createDependency("gripper", () -> {
			YoubotGripper ret = new YoubotGripper();
			ret.setName(getName() + " gripper");
			return ret;
		});
		createDependency("platformArm",
				() -> new StaticConnection(getPlatform().getFrontArmMountFrame(), getArm().getBase()));
		createDependency("platformArmPosition", () -> new StaticPosition(getPlatform().getFrontArmMountFrame(),
				getArm().getBase(), Transformation.IDENTITY));
		createDependency("armGripper", () -> new StaticConnection(getArm().getFlange(), getGripper().getBase()));
		createDependency("armGripperPosition",
				() -> new StaticPosition(getArm().getFlange(), getGripper().getBase(), Transformation.IDENTITY));
	}

	public YoubotGripper getGripper() {
		return gripper.get();
	}

	public YoubotArm getArm() {
		return arm.get();
	}

	public YoubotPlatform getPlatform() {
		return platform.get();
	}

	public Frame getOdometryOrigin() {
		return getPlatform().getOdometryOrigin();
	}

}
