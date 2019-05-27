/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2010-2017 ISSE, University of Augsburg 
 */

package org.roboticsapi.kuka.youbot.action;

import org.roboticsapi.core.Action;
import org.roboticsapi.robot.parameter.RobotTool;

/**
 * This action will switch the tool of the youBot Arm
 */
public class SwitchToolAction extends Action {

	private final RobotTool tool;

	/**
	 * Gets the associated Robot Tool
	 * 
	 * @return
	 */
	public RobotTool getRobotTool() {
		return tool;
	}

	/**
	 * Constructor
	 * 
	 * @param tool Robot Tool to be setted to the youBot
	 */
	public SwitchToolAction(RobotTool tool) {
		super(0);
		this.tool = tool;
	}
}
