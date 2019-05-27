/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2010-2017 ISSE, University of Augsburg 
 */

package org.roboticsapi.kuka.youbot.runtime.mapper;

import org.roboticsapi.runtime.mapping.net.DataflowOutPort;
import org.roboticsapi.runtime.mapping.result.ActionResult;

public class ToolActionResult extends ActionResult {

	private DataflowOutPort comPort, moiPort, massPort;

	public ToolActionResult(DataflowOutPort comPort, DataflowOutPort moiPort, DataflowOutPort massPort) {
		super(null);

		this.comPort = comPort;
		this.moiPort = moiPort;
		this.massPort = massPort;
	}

	public DataflowOutPort getCOMOutPort() {
		return comPort;
	}

	public DataflowOutPort getMOIOutPort() {
		return moiPort;
	}

	public DataflowOutPort getMassOutPort() {
		return massPort;
	}
}
