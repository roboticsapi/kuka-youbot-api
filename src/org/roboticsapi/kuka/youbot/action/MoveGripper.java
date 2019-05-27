/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2010-2017 ISSE, University of Augsburg 
 */

package org.roboticsapi.kuka.youbot.action;

import org.roboticsapi.core.Action;

public class MoveGripper extends Action {

	private double destValue;

	public MoveGripper(double destValue) {
		super(0);
		this.setDestValue(destValue);
	}

	public double getDestValue() {
		return destValue;
	}

	public void setDestValue(double destValue) {
		this.destValue = destValue;
	}

}
