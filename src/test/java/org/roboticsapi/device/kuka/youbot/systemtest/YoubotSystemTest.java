/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2013-2019 ISSE, University of Augsburg 
 */

package org.roboticsapi.device.kuka.youbot.systemtest;

import java.util.concurrent.TimeUnit;

import org.junit.ClassRule;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;
import org.roboticsapi.core.RoboticsContext;
import org.roboticsapi.core.RoboticsContextImpl;
import org.roboticsapi.core.exception.InitializationException;
import org.roboticsapi.core.exception.RoboticsException;
import org.roboticsapi.device.kuka.youbot.YoubotArm;
import org.roboticsapi.device.kuka.youbot.runtime.rpi.YoubotArmMockDriver;
import org.roboticsapi.device.kuka.youbot.runtime.rpi.YoubotArmSimulationDriver;
import org.roboticsapi.device.kuka.youbot.simulation.SYoubotArm;
import org.roboticsapi.facet.simulation.SWorld;
import org.roboticsapi.framework.multijoint.activity.JointControllerInterfaceTests;
import org.roboticsapi.framework.multijoint.activity.JointPtpInterface;
import org.roboticsapi.framework.multijoint.activity.JointPtpInterfaceTests;
import org.roboticsapi.framework.multijoint.activity.SimulatedJointMotionInterface;
import org.roboticsapi.framework.robot.activity.RobotPtpInterfaceTests;
import org.roboticsapi.systemtest.AfterUninitialization;
import org.roboticsapi.systemtest.BeforeInitialization;
import org.roboticsapi.systemtest.Prepare;
import org.roboticsapi.systemtest.RoboticsTestSuite;
import org.roboticsapi.systemtest.RoboticsTestSuite.DeviceInterfaceTests;
import org.roboticsapi.systemtest.WithDevice;
import org.roboticsapi.systemtest.WithRcc;
import org.roboticsapi.systemtest.WithRcc.Rcc;

@RunWith(RoboticsTestSuite.class)
@DeviceInterfaceTests({ JointPtpInterfaceTests.class, RobotPtpInterfaceTests.class,
		JointControllerInterfaceTests.class })
@WithDevice(device = YoubotArm.class, deviceDrivers = { YoubotArmMockDriver.class,
		YoubotArmSimulationDriver.class })
@WithRcc(Rcc.DedicatedJavaRcc)
public class YoubotSystemTest {

	@ClassRule
	public static Timeout timeout = new Timeout(1, TimeUnit.MINUTES);

	private RoboticsContext simContext;

	@BeforeInitialization(YoubotArmSimulationDriver.class)
	public void setupDriver(YoubotArmSimulationDriver driver) throws InitializationException {
		simContext = new RoboticsContextImpl("SimContext");
		SWorld sWorld = new SWorld();
		simContext.initialize(sWorld);
		SYoubotArm sYoubotArm = new SYoubotArm();
		sYoubotArm.setWorld(sWorld);
		simContext.initialize(sYoubotArm);
		driver.setSimulation(sYoubotArm.getIdentifier());
	}

	@Prepare(YoubotArm.class)
	public void prepare(YoubotArm device) throws RoboticsException {
		// Jump to home if possible
		SimulatedJointMotionInterface jump = device.use(SimulatedJointMotionInterface.class);
		if (jump != null) {
			jump.resetJoints(device.getHomePosition()).execute();
		} else {
			device.use(JointPtpInterface.class).ptpHome().execute();
		}
	}

	@AfterUninitialization(YoubotArmSimulationDriver.class)
	public void cleanupDriver(YoubotArmSimulationDriver driver) {
		simContext.destroy();
		simContext = null;
	}

}
