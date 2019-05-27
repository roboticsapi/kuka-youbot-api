/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2010-2017 ISSE, University of Augsburg 
 */

package org.roboticsapi.kuka.youbot.javarcc.devices;

import org.roboticsapi.runtime.core.javarcc.devices.AbstractJDevice;

public abstract class JYoubotGripper extends AbstractJDevice {

	public JYoubotGripper() {
		super();
	}

	public abstract boolean gripperBusy();

	public abstract boolean setGripperPosition(double distance);

	public abstract double getGripperPosition();

}