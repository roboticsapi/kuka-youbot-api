/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2010-2017 ISSE, University of Augsburg 
 */

package org.roboticsapi.kuka.youbot.javarcc.primitives;

import org.roboticsapi.runtime.core.types.RPIbool;
import org.roboticsapi.runtime.core.types.RPIdouble;
import org.roboticsapi.runtime.core.types.RPIint;
import org.roboticsapi.runtime.core.types.RPIstring;
import org.roboticsapi.runtime.javarcc.JInPort;
import org.roboticsapi.runtime.javarcc.JOutPort;
import org.roboticsapi.runtime.javarcc.JParameter;
import org.roboticsapi.runtime.javarcc.JPrimitive;

public class JJointImpParameters extends JPrimitive {
	/** Activation port */
	final JInPort<RPIbool> inActive = add("inActive", new JInPort<RPIbool>());

	/** Additional Torque in Nm */
	final JInPort<RPIdouble> inAddTorque = add("inAddTorque", new JInPort<RPIdouble>());

	/** Damping to set */
	final JInPort<RPIdouble> inDamping = add("inDamping", new JInPort<RPIdouble>());

	/**
	 * Maximum Torque caused by impedance controller in Nm, <=0 for unlimited
	 */
	final JInPort<RPIdouble> inMaxTorque = add("inMaxTorque", new JInPort<RPIdouble>());

	/** Stiffness to set */
	final JInPort<RPIdouble> inStiffness = add("inStiffness", new JInPort<RPIdouble>());

	/** Parameter setting was successful */
	final JOutPort<RPIbool> outSuccess = add("outSuccess", new JOutPort<RPIbool>());

	/** Name of youBot */
	final JParameter<RPIstring> propRobot = add("Robot", new JParameter<RPIstring>(new RPIstring("")));

	/** Stiffness Parameter in Nm/rad */
	final JParameter<RPIdouble> propStiffness = add("Stiffness", new JParameter<RPIdouble>(new RPIdouble("0")));

	/** Damping Parameter relative in 0..1 range */
	final JParameter<RPIdouble> propDamping = add("Damping", new JParameter<RPIdouble>(new RPIdouble("0")));

	/** Additional Torque in Nm */
	final JParameter<RPIdouble> propAddTorque = add("AddTorque", new JParameter<RPIdouble>(new RPIdouble("0")));

	/**
	 * Maximum Torque caused by the impedance controller in Nm, <=0 for unlimited
	 */
	final JParameter<RPIdouble> propMaxTorque = add("MaxTorque", new JParameter<RPIdouble>(new RPIdouble("0")));

	/** Joint Index to set paramters */
	final JParameter<RPIint> propJointIndex = add("JointIndex", new JParameter<RPIint>(new RPIint("0")));

	@Override
	public void checkParameters() throws IllegalArgumentException {
		// TODO: do parameter checks
	}

	@Override
	public void updateData() {
		// TODO: perform computations based on local variables and InPort
		// values,
		// write local variables and OutPorts

		// FIXME: implement correctly
		outSuccess.set(new RPIbool(true));
	}

}
