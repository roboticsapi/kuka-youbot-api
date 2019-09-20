/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2013-2019 ISSE, University of Augsburg 
 */

package org.roboticsapi.device.kuka.youbot.runtime.rpi.mapper;

import org.roboticsapi.core.Action;
import org.roboticsapi.core.realtimevalue.realtimeboolean.RealtimeBoolean;
import org.roboticsapi.facet.runtime.rpi.mapping.ActionResult;

public class GripperGoalActionResult extends ActionResult {

	private double distance;

	public GripperGoalActionResult(Action action, RealtimeBoolean completion, double distance) {
		super(action, completion);
		this.distance = distance;
	}

	public double getDistance() {
		return distance;
	}
}
