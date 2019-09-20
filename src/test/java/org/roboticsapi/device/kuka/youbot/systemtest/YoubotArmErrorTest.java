/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2013-2019 ISSE, University of Augsburg 
 */

package org.roboticsapi.device.kuka.youbot.systemtest;

import org.junit.Assert;
import org.junit.Test;
import org.roboticsapi.core.RuntimeCommand;
import org.roboticsapi.core.UnhandledErrorsException;
import org.roboticsapi.core.exception.RoboticsException;
import org.roboticsapi.device.kuka.youbot.YoubotArm;
import org.roboticsapi.device.kuka.youbot.simulation.YoubotArmSimulation;
import org.roboticsapi.facet.runtime.rcc.RccRuntime;
import org.roboticsapi.facet.simulation.SEntity;
import org.roboticsapi.facet.simulation.SPositionedEntity;
import org.roboticsapi.facet.simulation.SWorld;
import org.roboticsapi.feature.runtime.javarcc.JavaRcc;
import org.roboticsapi.feature.startup.launcher.DefaultRapi;
import org.roboticsapi.feature.startup.launcher.Rapi;
import org.roboticsapi.framework.multijoint.action.FollowJointVelocity;
import org.roboticsapi.framework.multijoint.activity.JointPtpInterface;

public class YoubotArmErrorTest {

	@Test
	public void testJavaRccCommandFailsWithReason() throws RoboticsException {
		Rapi rapi = DefaultRapi.createNewEmpty();

		YoubotArm arm = new YoubotArm();
		JavaRcc rcc = new JavaRcc();

		RccRuntime runtime = new RccRuntime();
		runtime.setRcc(rcc);

		SEntity parent = new SPositionedEntity();
		parent.setWorld(new SWorld());

		YoubotArmSimulation sim = new YoubotArmSimulation();
		sim.setArm(arm);
		sim.setRuntime(runtime);
		sim.setParent(parent);
		sim.setDeviceName("youbot");
		sim.setName("simulation");
		rapi.add(sim);

		arm.use(JointPtpInterface.class).ptpHome().execute();
		try {
			runtime.createRuntimeCommand(arm.getDriver(), new FollowJointVelocity(new double[] { 1, 0, 0, 0, 0 }))
					.execute();

			Assert.fail("An exception should have been thrown");
		} catch (UnhandledErrorsException e) {
			Assert.assertFalse("An InnerException should be set", e.getInnerExceptions().isEmpty());
		}
	}

	@Test
	public void testJavaRccCommandWithListenerFailsWithReason() throws RoboticsException {
		Rapi rapi = DefaultRapi.createNewEmpty();

		YoubotArm arm = new YoubotArm();
		JavaRcc rcc = new JavaRcc();

		RccRuntime runtime = new RccRuntime();
		runtime.setRcc(rcc);

		SEntity parent = new SPositionedEntity();
		parent.setWorld(new SWorld());

		YoubotArmSimulation sim = new YoubotArmSimulation();
		sim.setArm(arm);
		sim.setRuntime(runtime);
		sim.setParent(parent);
		sim.setDeviceName("youbot");
		sim.setName("simulation");
		rapi.add(sim);

		arm.use(JointPtpInterface.class).ptp(new double[] { 0, 0, 0, 0, 0 }).execute();
		RuntimeCommand command = runtime.createRuntimeCommand(arm.getDriver(),
				new FollowJointVelocity(new double[] { 1, 0, 0, 0, 0 }));
		command.addObserver(command.getCommandExecutionTime(), t -> {
		});

		try {
			command.execute();
			Assert.fail("An exception should have been thrown");
		} catch (UnhandledErrorsException e) {
			Assert.assertFalse("An InnerException should be set", e.getInnerExceptions().isEmpty());
		}
	}

}
