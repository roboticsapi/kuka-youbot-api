/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2010-2017 ISSE, University of Augsburg 
 */

package org.roboticsapi.kuka.youbot;

import org.roboticsapi.cartesianmotion.parameter.CartesianParameters;
import org.roboticsapi.cartesianmotion.parameter.FrameProjectorParameter;
import org.roboticsapi.core.DeviceParameters;
import org.roboticsapi.core.InvalidParametersException;
import org.roboticsapi.multijoint.Joint;
import org.roboticsapi.multijoint.JointDriver;
import org.roboticsapi.multijoint.link.BaseLink;
import org.roboticsapi.multijoint.link.FlangeLink;
import org.roboticsapi.multijoint.link.JointLink;
import org.roboticsapi.multijoint.link.Link;
import org.roboticsapi.robot.AbstractRobotArm;
import org.roboticsapi.robot.RobotArmDriver;
import org.roboticsapi.world.Rotation;
import org.roboticsapi.world.Transformation;

public final class YoubotArm extends AbstractRobotArm<Joint, RobotArmDriver> {

	private static final int JOINT_VELOCITY = 90;

	public YoubotArm() {
		super(5);
	}

	@Override
	protected Joint createJoint(int number, String name) {
		JointDriver driver = getDriver().createJointDriver(number);

		switch (number) {
		case 0:
			return createRevoluteJoint(driver, name, Math.toRadians(-169), Math.toRadians(169),
					Math.toRadians(JOINT_VELOCITY), 1.25);
		case 1:
			return createRevoluteJoint(driver, name, Math.toRadians(-65), Math.toRadians(90),
					Math.toRadians(JOINT_VELOCITY), 1.25);
		case 2:
			return createRevoluteJoint(driver, name, Math.toRadians(-146), Math.toRadians(151),
					Math.toRadians(JOINT_VELOCITY), 1.25);
		case 3:
			return createRevoluteJoint(driver, name, Math.toRadians(-102.5), Math.toRadians(102.5),
					Math.toRadians(JOINT_VELOCITY), 1.25);
		case 4:
			return createRevoluteJoint(driver, name, Math.toRadians(-167.5), Math.toRadians(167.5),
					Math.toRadians(JOINT_VELOCITY), 1.25);
		default:
			return null;
		}
	}

	@Override
	protected void defineMaximumParameters() throws InvalidParametersException {
		double JOINT_VEL_SAFETY_MARGIN = Math.toRadians(20d);
		double JOINT_POS_SAFETY_MARGIN = Math.toRadians(2d);

		addMaximumParameters(new CartesianParameters(0.1, 0.4, Math.PI / 4f, Math.PI));

		addMaximumParameters(getJointDeviceParameters(JOINT_POS_SAFETY_MARGIN, JOINT_VEL_SAFETY_MARGIN));
	}

	@Override
	protected Link createLink(int number) {
		switch (number) {
		case 0:
			return new BaseLink(getBase(), getJoint(0), new Transformation(0, 0, 0.078, 0, 0, Math.PI));
		case 1:
			return new JointLink(getJoint(0), getJoint(1),
					new Transformation(0.033, 0, -0.069, 0, 0, Rotation.deg2rad(90)));
		case 2:
			return new JointLink(getJoint(1), getJoint(2), new Transformation(0, -0.155, 0, -Math.PI / 2f, 0, 0));
		case 3:
			return new JointLink(getJoint(2), getJoint(3), new Transformation(0.135, 0, 0, 0, 0, 0));
		case 4:
			return new JointLink(getJoint(3), getJoint(4),
					new Transformation(0.08, 0, 0, Math.PI / 2f, 0, -Rotation.deg2rad(90)));
		case 5:
			return new FlangeLink(getJoint(4), getFlange(), new Transformation(0, 0, -0.091, 0, 0, Math.PI));
		}
		return null;
	}

	@Override
	public void validateParameters(DeviceParameters parameters) throws InvalidParametersException {

	}

	@Override
	protected void defineDefaultParameters() throws InvalidParametersException {
		super.defineDefaultParameters();

		addDefaultParameters(new FrameProjectorParameter(new YoubotArmPositionPreservingFrameProjector(this)));
	}

}
