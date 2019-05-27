/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2010-2017 ISSE, University of Augsburg 
 */

package org.roboticsapi.kuka.youbot.javarcc.primitives;

import org.roboticsapi.kuka.youbot.javarcc.devices.JYoubotGripper;
import org.roboticsapi.runtime.core.types.RPIbool;
import org.roboticsapi.runtime.core.types.RPIdouble;
import org.roboticsapi.runtime.core.types.RPIstring;
import org.roboticsapi.runtime.javarcc.JOutPort;
import org.roboticsapi.runtime.javarcc.JParameter;
import org.roboticsapi.runtime.javarcc.JPrimitive;

public class JGripper extends JPrimitive {
	private JOutPort<RPIbool> outCompleted = add("outCompleted", new JOutPort<RPIbool>());
	private JParameter<RPIdouble> propDistance = add("Distance", new JParameter<RPIdouble>());
	private JParameter<RPIstring> propRobot = add("Robot", new JParameter<RPIstring>());

	private JYoubotGripper dev;
	private boolean gripperPositionSend;

	@Override
	public void checkParameters() throws IllegalArgumentException {
		dev = device(propRobot, JYoubotGripper.class);

		if ((propDistance.get().get() < 0) || (Math.ceil(propDistance.get().get() * 1000) / 1000) > 0.023)
			throw new IllegalArgumentException("Distance");
	}

	@Override
	public void updateData() {
		if (gripperPositionSend)
			outCompleted.set(new RPIbool(!dev.gripperBusy()));
		else {
			gripperPositionSend = dev.setGripperPosition(propDistance.get().get());
			outCompleted.set(new RPIbool(false));
		}
	}
}
