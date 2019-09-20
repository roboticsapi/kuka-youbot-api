/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2013-2019 ISSE, University of Augsburg 
 */

package org.roboticsapi.device.kuka.youbot.simulation;

import org.roboticsapi.core.world.mutable.MutableTransformation;
import org.roboticsapi.framework.multijoint.simulation.SMultijointDevice;
import org.roboticsapi.framework.multijoint.simulation.SVelocityControlledAxis;

public class SYoubotArm extends SMultijointDevice {

	final double axisWeight = 0.1;
	private final MutableTransformation relativePosition = new MutableTransformation();
	private final MutableTransformation flangeOffset = new MutableTransformation(0, 0, -0.091, 0, 0, Math.PI);

	public SYoubotArm() {
		super(5);
	}

	@Override
	public SVelocityControlledAxis createAxis(int index) {
		switch (index) {
		case 0:
			MutableTransformation pos = new MutableTransformation(0.167, 0, 0.014, 0, 0, 0);
			pos.multiply(new MutableTransformation(0, 0, 0.078, 0, 0, Math.PI));
			return new SVelocityControlledAxis(getParent(), pos, Math.toRadians(-169.0), axisWeight);
		case 1:
			return new SVelocityControlledAxis(getAxis(0),
					new MutableTransformation(0.033, 0, -0.069, 0, 0, Math.PI / 2f), Math.toRadians(-65), axisWeight);
		case 2:
			return new SVelocityControlledAxis(getAxis(1), new MutableTransformation(0, -0.155, 0, -Math.PI / 2f, 0, 0),
					Math.toRadians(146), axisWeight);
		case 3:
			return new SVelocityControlledAxis(getAxis(2), new MutableTransformation(0.135, 0, 0, 0, 0, 0),
					Math.toRadians(-102.5), axisWeight);
		case 4:
			return new SVelocityControlledAxis(getAxis(3),
					new MutableTransformation(0.08, 0, 0, Math.PI / 2f, 0, -Math.PI / 2f), Math.toRadians(-167.5),
					axisWeight);
		default:
			return null;

		}
	}

	@Override
	public MutableTransformation getRelativePosition() {
		getParent().getPosition().invertTo(relativePosition);
		relativePosition.multiply(getAxis(4).getPosition());
		relativePosition.multiply(flangeOffset);
		return relativePosition;
	}

}
