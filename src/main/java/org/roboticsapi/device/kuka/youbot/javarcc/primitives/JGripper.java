/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2013-2019 ISSE, University of Augsburg 
 */

package org.roboticsapi.device.kuka.youbot.javarcc.primitives;

import org.roboticsapi.device.kuka.youbot.javarcc.devices.JYoubotGripper;
import org.roboticsapi.facet.javarcc.JOutPort;
import org.roboticsapi.facet.javarcc.JParameter;
import org.roboticsapi.facet.javarcc.JPrimitive;
import org.roboticsapi.facet.runtime.rpi.core.types.RPIbool;
import org.roboticsapi.facet.runtime.rpi.core.types.RPIdouble;
import org.roboticsapi.facet.runtime.rpi.core.types.RPIstring;

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
