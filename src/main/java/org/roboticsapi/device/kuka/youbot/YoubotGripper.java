/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2013-2019 ISSE, University of Augsburg 
 */

package org.roboticsapi.device.kuka.youbot;

import org.roboticsapi.core.DeviceParameters;
import org.roboticsapi.core.InvalidParametersException;
import org.roboticsapi.core.exception.RoboticsException;
import org.roboticsapi.core.world.Transformation;
import org.roboticsapi.framework.gripper.AbstractBaseJaw;
import org.roboticsapi.framework.gripper.AbstractParallelGripper;
import org.roboticsapi.framework.gripper.BaseJaw;
import org.roboticsapi.framework.gripper.GripperDriver;
import org.roboticsapi.framework.gripper.GrippingFinger;

/**
 * Implementation for the standard parallel gripper of a KUKA youBot.
 */
public final class YoubotGripper extends AbstractParallelGripper<GripperDriver> {

	private static final double STROKE_PER_FINGER = 0.01d;
	private static final double MINIMAL_BASE_JAW_DISTANCE = 0d;
	private static final double RECOMMENDED_WORKPIECE_WEIGHT = 0.5d;

	/**
	 * Default constructor.
	 */
	public YoubotGripper() {
		super(RECOMMENDED_WORKPIECE_WEIGHT, MINIMAL_BASE_JAW_DISTANCE, STROKE_PER_FINGER);
	}

	@Override
	protected BaseJaw createBaseJaw(int index) {
		return new YoubotGripperBaseJaw();
	}

	@Override
	protected Transformation createBaseToEffectorTransformation() {
		return Transformation.IDENTITY; // TODO:
	}

	@Override
	protected Transformation createBaseToCenterOfBaseJawBasesTransformation() {
		// base and center are identical... this gripper is part of the youbot's
		// kinematic chain.
		return Transformation.IDENTITY;
	}

	@Override
	public void validateParameters(DeviceParameters parameters) throws InvalidParametersException {
	}

	@Override
	protected void defineMaximumParameters() throws InvalidParametersException {
	}

	/**
	 * Internal implementation of a youBot gripper's base jaws.
	 */
	private static final class YoubotGripperBaseJaw extends AbstractBaseJaw {

		@Override
		protected void validateFinger(GrippingFinger finger) throws RoboticsException {
			if (!(finger instanceof YoubotFinger)) {
				throw new RoboticsException("Only youBot gripping fingers can be attached!");
			}
		}

		@Override
		public Transformation getBaseMountTransformation() {
			return new Transformation(0.008d, 0d, 0d, 0d, 0d, 0d);
		}

	}

}
