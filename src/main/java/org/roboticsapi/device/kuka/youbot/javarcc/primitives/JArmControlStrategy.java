/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2013-2019 ISSE, University of Augsburg 
 */

package org.roboticsapi.device.kuka.youbot.javarcc.primitives;

import org.roboticsapi.facet.javarcc.JInPort;
import org.roboticsapi.facet.javarcc.JOutPort;
import org.roboticsapi.facet.javarcc.JParameter;
import org.roboticsapi.facet.javarcc.JPrimitive;
import org.roboticsapi.facet.runtime.rpi.core.types.RPIbool;
import org.roboticsapi.facet.runtime.rpi.core.types.RPIint;
import org.roboticsapi.facet.runtime.rpi.core.types.RPIstring;

public class JArmControlStrategy extends JPrimitive {

	/** Activation port */
	final JInPort<RPIbool> inActive = add("inActive", new JInPort<RPIbool>());

	/** Index to select Controller */
	final JInPort<RPIint> inController = add("inController", new JInPort<RPIint>());

	/** controller selection has completed */
	final JOutPort<RPIbool> outSuccess = add("outSuccess", new JOutPort<RPIbool>());

	/** Name of youBot */
	final JParameter<RPIstring> propRobot = add("Robot", new JParameter<RPIstring>(new RPIstring("")));

	/** Index of Controller to set */
	final JParameter<RPIint> propController = add("Controller", new JParameter<RPIint>(new RPIint("0")));

	@Override
	public void checkParameters() throws IllegalArgumentException {
		// TODO: do parameter checks
	}

	@Override
	public void updateData() {
		// TODO: perform computations based on local variables and InPort values,
		// write local variables and OutPorts

		// FIXME: implement correctly
		outSuccess.set(new RPIbool(true));
	}

}
