/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2010-2017 ISSE, University of Augsburg 
 */

package org.roboticsapi.kuka.youbot.javarcc.primitives;

import java.util.Set;

import org.roboticsapi.kuka.youbot.javarcc.devices.JYoubotGripper;
import org.roboticsapi.runtime.core.javarcc.devices.JDevice;
import org.roboticsapi.runtime.core.types.RPIdouble;
import org.roboticsapi.runtime.core.types.RPIstring;
import org.roboticsapi.runtime.javarcc.JOutPort;
import org.roboticsapi.runtime.javarcc.JParameter;
import org.roboticsapi.runtime.javarcc.JPrimitive;

public class JGripperMonitor extends JPrimitive {
	private JOutPort<RPIdouble> outDistance = add("outDistance", new JOutPort<RPIdouble>());
	private JParameter<RPIstring> propRobot = add("Robot", new JParameter<RPIstring>());

	private JYoubotGripper dev;
	private double distance;

	@Override
	public void checkParameters() throws IllegalArgumentException {
		dev = device(propRobot, JYoubotGripper.class);
	}

	@Override
	public Set<JDevice> getSensors() {
		return deviceSet(dev);
	}

	@Override
	public void readSensor() {
		distance = dev.getGripperPosition();
	}

	@Override
	public void updateData() {
		outDistance.set(new RPIdouble(distance));
	}
}
