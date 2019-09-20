/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2013-2019 ISSE, University of Augsburg 
 */

package org.roboticsapi.device.kuka.youbot.javarcc.devices;

import org.roboticsapi.facet.javarcc.devices.JDevice;
import org.roboticsapi.facet.runtime.rpi.world.types.RPIFrame;
import org.roboticsapi.framework.multijoint.javarcc.devices.JSimulatedVelocityControlledMultijointDevice;
import org.roboticsapi.framework.robot.javarcc.interfaces.JArmKinematicsInterface;

public class JYoubotArm extends JSimulatedVelocityControlledMultijointDevice
		implements JArmKinematicsInterface, JDevice {
	private YoubotArmKin kin = new YoubotArmKin();
	private static double[] maxAngle = { Math.toRadians(169.0), Math.toRadians(90.0), Math.toRadians(146.0),
			Math.toRadians(102.5), Math.toRadians(167.5) };
	private static double[] minAngle = { Math.toRadians(-169.0), Math.toRadians(-65.0), Math.toRadians(-151.0),
			Math.toRadians(-102.5), Math.toRadians(-167.5) };
	private static double[] maxVelocities = { 1.57, 1.57, 1.57, 1.57, 1.57 };
	private static double[] maxAccelerations = { 1.25, 1.25, 2, 2.5, 2.5 };
	private static double[] P = { 0.09, 0.15, 0.07, 0.1, 0.13 };

	public JYoubotArm(String name, String simulation) {
		super(name, 5, minAngle, maxAngle, maxVelocities, maxAccelerations, P, 10, simulation);
	}

	@Override
	public RPIFrame kin(double[] joints, RPIFrame ret) {
		return kin.kin(joints, ret);
	}

	@Override
	public double[] invKin(double[] hintJoints, RPIFrame frame) {
		return kin.invKin(frame, hintJoints);
	}

}
