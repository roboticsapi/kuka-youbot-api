/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2010-2017 ISSE, University of Augsburg 
 */

package org.roboticsapi.kuka.youbot.action;

import org.roboticsapi.core.Action;
import org.roboticsapi.multijoint.parameter.Controller;

/**
 * This Action will blend two equal Controller valuas in a given time interval.
 */
public class BlendControllerAction extends Action {

	private final Controller startController, endController;
	private final double duration;

	/**
	 * Start controller of blend controller action.
	 * 
	 * @return
	 */
	public Controller getStartController() {
		return startController;
	}

	/**
	 * End controller of blend controller action.
	 * 
	 * @return end controller
	 */
	public Controller getEndController() {
		return endController;
	}

	/**
	 * Get duration of blending.
	 * 
	 * @return Blend duration in seconds
	 */
	public double getDuration() {
		return duration;
	}

	/**
	 * This will blend values of two equal youBot controllers.
	 * 
	 * @param startController
	 * @param endController
	 * @param duration        in seconds
	 */
	public BlendControllerAction(Controller startController, Controller endController, double duration) {
		super(0);
		if (startController.getClass() != endController.getClass())
			throw new IllegalArgumentException();
		this.startController = startController;
		this.endController = endController;
		this.duration = duration;
	}
}
