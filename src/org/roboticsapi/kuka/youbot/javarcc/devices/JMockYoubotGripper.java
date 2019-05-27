/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2010-2017 ISSE, University of Augsburg 
 */

package org.roboticsapi.kuka.youbot.javarcc.devices;

public class JMockYoubotGripper extends JYoubotGripper {
	double lastPos = 0;

	public JMockYoubotGripper() {
	}

	@Override
	public boolean gripperBusy() {
		return false;
	}

	@Override
	public boolean setGripperPosition(double distance) {
		lastPos = distance;
		return true;
	}

	@Override
	public double getGripperPosition() {
		return lastPos;
	}

}
