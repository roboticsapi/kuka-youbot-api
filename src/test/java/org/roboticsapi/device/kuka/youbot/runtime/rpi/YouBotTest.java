/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2013-2019 ISSE, University of Augsburg 
 */

package org.roboticsapi.device.kuka.youbot.runtime.rpi;

import org.roboticsapi.core.RoboticsRuntime;
import org.roboticsapi.core.activity.ActivityHandle;
import org.roboticsapi.core.actuator.OverrideParameter;
import org.roboticsapi.core.actuator.OverrideParameter.Scaling;
import org.roboticsapi.core.exception.RoboticsException;
import org.roboticsapi.core.world.Pose;
import org.roboticsapi.device.kuka.youbot.YoubotArm;
import org.roboticsapi.device.kuka.youbot.YoubotGripper;
import org.roboticsapi.device.kuka.youbot.YoubotPlatform;
import org.roboticsapi.device.kuka.youbot.action.MoveGripper;
import org.roboticsapi.feature.startup.configuration.util.MultipleIllegalConfigurationsException;
import org.roboticsapi.feature.startup.launcher.DefaultRapi;
import org.roboticsapi.feature.startup.launcher.Rapi;
import org.roboticsapi.framework.cartesianmotion.activity.CartesianGoalMotionInterface;
import org.roboticsapi.framework.cartesianmotion.activity.CartesianPathMotionInterface;
import org.roboticsapi.framework.gripper.activity.GrippingInterface;
import org.roboticsapi.framework.multijoint.action.SwitchJointController;
import org.roboticsapi.framework.multijoint.controller.JointImpedanceController;
import org.roboticsapi.framework.multijoint.controller.JointPositionController;
import org.roboticsapi.framework.robot.activity.MotionInterface;

public class YouBotTest {

	public static void main(String[] args)
			throws MultipleIllegalConfigurationsException, RoboticsException, InterruptedException {

		Rapi rapi = DefaultRapi.createWithConfigFile("config/ApplicationConfig-jcc.xml");
		Thread.sleep(1000);

		YoubotPlatform platform = rapi.getSingle("Platform", YoubotPlatform.class);
		YoubotGripper gripper = rapi.getSingle("Gripper", YoubotGripper.class);
		YoubotArm arm = rapi.getSingle("Arm", YoubotArm.class);

		platform.getDriver().getRuntime().setOverride(1);

		arm.use(MotionInterface.class).ptpHome().execute();
		arm.use(MotionInterface.class).ptp(new double[] { 1, 1, 1, 1, 1 }).execute();
		arm.use(MotionInterface.class).ptpHome().execute();

		System.out.println("LIN");
		platform.use(CartesianPathMotionInterface.class)
				.lin(platform.getOdometryOrigin().asPose().plus(-1, 0, 0, 0, 0, 0)).execute();
		platform.use(CartesianPathMotionInterface.class)
				.lin(platform.getOdometryOrigin().asPose().plus(1, 0, 0, 0, 0, 0)).execute();
		platform.use(CartesianPathMotionInterface.class)
				.lin(platform.getOdometryOrigin().asPose().plus(-1, 0, 1, 0, 0, 0)).execute();
		System.out.println("Starting");
		platform.use(CartesianGoalMotionInterface.class)
				.moveToCartesianGoal(platform.getOdometryOrigin().asPose().plus(1, 0, 0, 1, 0, 0)).execute();
		System.out.println("Move 2");
		platform.use(CartesianGoalMotionInterface.class)
				.moveToCartesianGoal(platform.getOdometryOrigin().asPose().plus(0, 1, 0)).execute();

		System.out.println("Gripping");
		RoboticsRuntime runtime = gripper.getDriver().getRuntime();
		if (true)
			while (true) {
				for (double d = 0.0; d <= 0.022; d += 0.001) {
					runtime.createRuntimeCommand(gripper.getDriver(), new MoveGripper(d)).execute();
					Thread.sleep(10);
				}
			}

		arm.getDriver().getRuntime().createRuntimeCommand(arm.getDriver(),
				new SwitchJointController(new JointImpedanceController(5, 0.1, 1, 0))).execute();

		arm.getDriver().getRuntime()
				.createRuntimeCommand(arm.getDriver(), new SwitchJointController(new JointPositionController()))
				.execute();

		extracted(arm, gripper);

		if (true)
			return;
		double glassHeight = 0.115;

		MotionInterface mi = arm.use(MotionInterface.class);

		Pose glassPosition = arm.getBase().asPose().plus(-0.265, 0, glassHeight + 0.045, Math.toRadians(180),
				Math.toRadians(-92), Math.toRadians(-180));
		Pose glassPrePrePosition = glassPosition.plus(0.06, 0, -0.03, 0, 0, 0);
		Pose glassPrePosition = glassPosition.plus(0, 0, -0.03, 0, 0, 0);
		Pose glassAfterPosition = glassPosition.plus(0.06, 0, 0, 0, 0, 0);

		Pose targetHigh = arm.getBase().asPose().plus(0, -0.30, glassHeight + 0.085, Math.toRadians(90),
				Math.toRadians(-88), Math.toRadians(0));
		Pose targetLow = targetHigh.plus(-0.13, 0, 0, 0, 0, 0);

		// Hinfahren und aufnehmen
		ActivityHandle cl = gripper.use(GrippingInterface.class).close().beginExecute();
		mi.ptp(glassPrePrePosition).execute();
		cl.endExecute();
		mi.ptp(glassPrePosition).execute();
		double ov = platform.getDriver().getRuntime().getOverride();
		platform.getDriver().getRuntime().setOverride(0.4);
		mi.ptp(glassPosition, new OverrideParameter(0.01, Scaling.ABSOLUTE)).execute();
		platform.getDriver().getRuntime().setOverride(ov);
		gripper.use(GrippingInterface.class).open().execute();
		mi.ptp(glassAfterPosition).execute();

		// Bestimmte Position anfahren
		mi.ptp(targetHigh).execute();
		mi.ptp(targetLow).execute();
		mi.ptp(targetHigh).execute();

		// Zurueckfahren und absetzen
		mi.ptp(glassAfterPosition).execute();
		mi.ptp(glassPosition).execute();
		gripper.use(GrippingInterface.class).close().execute();
		platform.getDriver().getRuntime().setOverride(0.4);
		mi.ptp(glassPrePosition, new OverrideParameter(0.01, Scaling.ABSOLUTE)).execute();
		platform.getDriver().getRuntime().setOverride(ov);
		mi.ptp(glassPrePrePosition).execute();

		// CartesianPathMotionInterface cpm = youbot
		// .use(CartesianPathMotionInterface.class);

		// cpm.lin(youbot.getOdometryOrigin().plus(1, 0, 0)).execute();
		// cpm.lin(youbot.getOdometryOrigin().plus(0, 0, 0)).execute();

		// RtActivities.strictlySequential(
		// cpm.lin(youbot.getOdometryOrigin().plus(1, 0, 0)),
		// cpm.lin(youbot.getOdometryOrigin().plus(0, 0, 0)))
		// .beginExecute();
		//
		// RtActivities.strictlySequential(
		// cpm.lin(youbot.getOdometryOrigin().plus(1, 0, 0)),
		// cpm.lin(youbot.getOdometryOrigin().plus(0, 0, 0))).execute();

		// youbot.use(PlatformInterface.class).driveTo(youbot.getOdometryOrigin().plus(1,
		// 0, 0)).execute();
		// youbot.use(PlatformInterface.class).driveTo(youbot.getOdometryOrigin().plus(0,
		// 0, 0)).execute();

		rapi.destroy();

	}

	private static void extracted(YoubotArm arm, YoubotGripper gripper) throws RoboticsException, InterruptedException {
		arm.getDriver().getRuntime().setOverride(1.0);

		// while(true) {
		MotionInterface mi = arm.use(MotionInterface.class);
		double[] preConfigurationHigh = IK.ik(-0.281, -0.142, 0.21);
		double[] preConfigurationLow = IK.ik(-0.281, -0.142, 0.145);
		double[] graspConfiguration = IK.ik(-0.302, -0.15, 0.145);
		double[] postConfiguration = IK.ik(-0.302, -0.15, 0.18);

		gripper.use(GrippingInterface.class).close().execute();

		// �ber dem Glas
		mi.ptp(preConfigurationHigh).execute();

		// Vor-Greifposition
		mi.ptp(preConfigurationLow).execute();

		// Greifposition einnehmen
		// double ov = arm.getDriver().getRuntime().getOverride();
		// arm.getDriver().getRuntime().setOverride(0.3);
		mi.ptp(graspConfiguration, 0.3).execute();
		// arm.getDriver().getRuntime().setOverride(ov);

		// Greifen
		gripper.use(GrippingInterface.class).open().execute();

		mi.ptp(postConfiguration).execute();
		mi.ptp(IK.ik(0, -0.35, 0.18)).execute();
		mi.ptp(IK.ik(0, -0.42, 0.05)).execute();

		// Absetzen
		// ov = arm.getDriver().getRuntime().getOverride();
		// arm.getDriver().getRuntime().setOverride(0.3);
		mi.ptp(IK.ik(0, -0.42, 0.042), 0.3).execute();
		// arm.getDriver().getRuntime().setOverride(ov);

		// Loslassen
		gripper.use(GrippingInterface.class).close().execute();

		// Wegfahren
		mi.ptp(IK.ik(0, -0.35, 0.18)).execute();

		mi.ptp(preConfigurationHigh).execute();

		// arm.use(MotionInterface.class).ptp(preConfigurationHigh).execute();
		//
		// double[] immediateConfiguration = new double[] {Math.toRadians(90),
		// Math.toRadians(0), Math.toRadians(120), Math.toRadians(-30), 0};
		//
		// arm.use(MotionInterface.class).ptp(immediateConfiguration).execute();
		//
		// Frame temp = arm.getFlange().snapshot(arm.getBase());
		// Frame prePlacingHigh = temp.plus(0,0,0.1);
		// Frame prePlacingLow = temp.plus(-0.1,0,0.1);
		//
		// arm.use(MotionInterface.class).lin(prePlacingHigh).execute();
		// arm.use(MotionInterface.class).lin(prePlacingLow).execute();

		// }
	}
}
