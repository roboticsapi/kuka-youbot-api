/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2013-2019 ISSE, University of Augsburg 
 */

package org.roboticsapi.device.kuka.youbot.javarcc.devices;

import org.roboticsapi.facet.javarcc.devices.JDevice;
import org.roboticsapi.facet.runtime.rpi.world.types.RPIFrame;
import org.roboticsapi.framework.multijoint.javarcc.devices.JMockMultijointDevice;
import org.roboticsapi.framework.robot.javarcc.interfaces.JArmKinematicsInterface;

public class JMockYoubotArm extends JMockMultijointDevice implements JArmKinematicsInterface, JDevice {
	private YoubotArmKin kin = new YoubotArmKin();
	private static double[] maxAngle = { Math.toRadians(169.0), Math.toRadians(90.0), Math.toRadians(146.0),
			Math.toRadians(102.5), Math.toRadians(167.5) };
	private static double[] minAngle = { Math.toRadians(-169.0), Math.toRadians(-65.0), Math.toRadians(-151.0),
			Math.toRadians(-102.5), Math.toRadians(-167.5) };
	private static double[] initialAngle = { Math.toRadians(-169.0), Math.toRadians(-65.0), Math.toRadians(146),
			Math.toRadians(-102.5), Math.toRadians(-167.5) };
	private static double[] maxVelocity = { Math.PI / 2, Math.PI / 2, Math.PI / 2, Math.PI / 2, Math.PI / 2 };
	private static double[] maxAcceleration = { 1.25, 1.25, 1.25, 1.25, 1.25 };

	public JMockYoubotArm() {
		super(5, minAngle, maxAngle, initialAngle, maxVelocity, maxAcceleration);
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
