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
 * Switch associated controller of the youBot
 */
public class SwitchControllerAction extends Action {

	private final Controller controller;

	/**
	 * Gets the associated Controller.
	 * 
	 * @return Controller
	 */
	public Controller getController() {
		return controller;
	}

	/**
	 * Constructor
	 * 
	 * @param controller to be setted by the youBot
	 */
	public SwitchControllerAction(Controller controller) {
		super(0);
		this.controller = controller;
	}
}
