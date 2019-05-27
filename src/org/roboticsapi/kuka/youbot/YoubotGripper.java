/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2010-2017 ISSE, University of Augsburg 
 */

package org.roboticsapi.kuka.youbot;

import org.roboticsapi.core.exception.RoboticsException;
import org.roboticsapi.core.sensor.DoubleSensor;
import org.roboticsapi.tool.gripper.AbstractBaseJaw;
import org.roboticsapi.tool.gripper.AbstractParallelGripper;
import org.roboticsapi.tool.gripper.BaseJaw;
import org.roboticsapi.tool.gripper.GrippingFinger;
import org.roboticsapi.world.Frame;
import org.roboticsapi.world.Transformation;

/**
 * Implementation for the standard parallel gripper of a KUKA youBot.
 */
public final class YoubotGripper extends AbstractParallelGripper<YoubotGripperDriver> {

	private static final double MAXIMUM_FORCE = 0d;
	private static final double MINIMUM_FORCE = 0d;
	private static final double MAXIMUM_VELOCITY = 0d;
	private static final double MINIMUM_VELOCITY = 0d;
	private static final double STROKE_PER_FINGER = 0.01d;
	private static final double MINIMAL_BASE_JAW_DISTANCE = 0d;
	private static final double RECOMMENDED_WORKPIECE_WEIGHT = 0.5d;

	/**
	 * Default constructor.
	 */
	public YoubotGripper() {
		super(RECOMMENDED_WORKPIECE_WEIGHT, MINIMAL_BASE_JAW_DISTANCE, STROKE_PER_FINGER, MINIMUM_VELOCITY,
				MAXIMUM_VELOCITY, MINIMUM_FORCE, MAXIMUM_FORCE);
	}

	@Override
	public DoubleSensor getBaseJawOpeningWidthSensor() {
		return getDriver().getOpeningWidthSensor();
	}

	@Override
	protected Transformation createBase2CenterTransformation() {
		// base and center are identical...
		return new Transformation();
	}

	@Override
	protected BaseJaw createBaseJaw(int index, Frame mountFrame) {
		return new YoubotGripperBaseJaw(mountFrame);
	}

	/**
	 * Internal implementation of a youBot gripper's base jaws.
	 */
	private static final class YoubotGripperBaseJaw extends AbstractBaseJaw {

		/**
		 * Default constructor.
		 * 
		 * @param mountFrame the base jaw's mount frame
		 */
		private YoubotGripperBaseJaw(Frame mountFrame) {
			super(mountFrame);
		}

		@Override
		protected void validateFinger(GrippingFinger finger) throws RoboticsException {
			if (!(finger instanceof YoubotFinger)) {
				throw new RoboticsException("Only youBot gripping fingers can be attached!");
			}
		}

		@Override
		protected Transformation getBaseMountTransformation() {
			return new Transformation(0.008d, 0d, 0d, 0d, 0d, 0d);
		}

	}

}
