/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2010-2017 ISSE, University of Augsburg 
 */

package org.roboticsapi.kuka.youbot.runtime.mapper;

import org.roboticsapi.runtime.mapping.net.DataflowOutPort;
import org.roboticsapi.runtime.mapping.result.ActionResult;

public class JointImpedanceActionResult extends ActionResult {

	private DataflowOutPort[] stiffnessPorts, dampingPorts, addTorquePorts;
	private final double[] maxTorques;

	public JointImpedanceActionResult(DataflowOutPort outPort, int jointCount, DataflowOutPort[] stiffnessPorts,
			DataflowOutPort[] dampingPorts, DataflowOutPort[] addTorquePorts, double[] maxTorques) {
		super(outPort);
		this.maxTorques = maxTorques;
		if (stiffnessPorts.length != jointCount)
			throw new IllegalArgumentException();
		if (dampingPorts.length != jointCount)
			throw new IllegalArgumentException();
		if (addTorquePorts.length != jointCount)
			throw new IllegalArgumentException();

		this.stiffnessPorts = stiffnessPorts;
		this.dampingPorts = dampingPorts;
		this.addTorquePorts = addTorquePorts;
	}

	public int getJointCount() {
		return stiffnessPorts.length;
	}

	DataflowOutPort getStiffnessOutPort(int joint) {
		return stiffnessPorts[joint];
	}

	DataflowOutPort getDampingOutPort(int joint) {
		return dampingPorts[joint];
	}

	DataflowOutPort getAddTorqueOutPort(int joint) {
		return addTorquePorts[joint];
	}

	public double[] getMaxTorques() {
		return maxTorques;
	}
}
