/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2010-2017 ISSE, University of Augsburg 
 */

package org.roboticsapi.kuka.youbot.javarcc.devices;

import org.roboticsapi.world.mutable.MutableTransformation;
import org.roboticsapi.world.mutable.MutableTwist;

public class YoubotPlatformKin {

	private final static double lengthBetweenFrontAndRearWheels = 0.47;
	private final static double lengthBetweenFrontWheels = 0.3;
	private final static double wheelRadius = 0.0475;
	private final static double slideRatio = 1;

	private final static double FORWARD_RAD_PER_M = 1 / wheelRadius;
	private final static double LEFT_RAD_PER_M = 1 / wheelRadius / slideRatio;
	private final static double YAW_RAD_PER_RAD = ((lengthBetweenFrontAndRearWheels + lengthBetweenFrontWheels)
			/ (2.0 * wheelRadius));

	public void posKin(double flDelta, double frDelta, double rlDelta, double rrDelta, MutableTransformation ret) {
		double forwardDelta = (flDelta + frDelta) / 2 / FORWARD_RAD_PER_M;
		double leftDelta = -(flDelta - rlDelta) / 2 / LEFT_RAD_PER_M;
		double yawDelta = -(flDelta - rrDelta) / 2 / YAW_RAD_PER_RAD;
		ret.setVectorEuler(forwardDelta, leftDelta, 0, yawDelta, 0, 0);
	}

	public void velInvKin(double forwardVel, double leftVel, double yawVel, double[] ret) {
		double forward = forwardVel * FORWARD_RAD_PER_M, left = leftVel * LEFT_RAD_PER_M,
				yaw = yawVel * YAW_RAD_PER_RAD;

		double flVel = forward - left - yaw;
		double frVel = forward + left + yaw;
		double rlVel = forward + left - yaw;
		double rrVel = forward - left + yaw;
		ret[0] = flVel;
		ret[1] = frVel;
		ret[2] = rlVel;
		ret[3] = rrVel;
	}

	public void velKin(double flVel, double frVel, double rlVel, double rrVel, MutableTwist ret) {
		double forwardVel = (flVel + frVel) / 2 / FORWARD_RAD_PER_M;
		double leftVel = -(flVel - rlVel) / 2 / LEFT_RAD_PER_M;
		double yawVel = -(flVel - rrVel) / 2 / YAW_RAD_PER_RAD;

		ret.getTranslation().set(forwardVel, leftVel, 0);
		ret.getRotation().set(0, 0, yawVel);
	}
}
