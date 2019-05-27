/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2010-2017 ISSE, University of Augsburg 
 */

package org.roboticsapi.kuka.youbot.runtime.mapper;

import org.roboticsapi.runtime.mapping.net.DataflowOutPort;
import org.roboticsapi.runtime.mapping.result.ActionResult;

public class PositionControllerActionResult extends ActionResult {

	public PositionControllerActionResult(DataflowOutPort outPort) {
		super(outPort);

	}

}
