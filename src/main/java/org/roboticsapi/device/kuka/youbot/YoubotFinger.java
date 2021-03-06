/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2013-2019 ISSE, University of Augsburg 
 */

package org.roboticsapi.device.kuka.youbot;

import org.roboticsapi.framework.gripper.AbstractGrippingFinger;
import org.roboticsapi.framework.gripper.GrippingFinger;

/**
 * Default class for youBot {@link GrippingFinger}s. May be sub-classed.
 */
public class YoubotFinger extends AbstractGrippingFinger {

	@Override
	public double getLength() {
		return 0.03d;
	}

	@Override
	public double getOffset() {
		return 0;
	}

}
