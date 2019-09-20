/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2013-2019 ISSE, University of Augsburg 
 */

package org.roboticsapi.device.kuka.youbot.javarcc.devices;

import org.roboticsapi.facet.javarcc.devices.JDevice;
import org.roboticsapi.facet.javarcc.devices.PeriodicTask;
import org.roboticsapi.facet.javarcc.simulation.SimulationHelper;

public class JSimulatedYoubotGripper extends JYoubotGripper implements JDevice {
	double lastPos = 0;
	SIYoubotGripper sim;
	boolean busy = true;

	public JSimulatedYoubotGripper(String simulation) {
		sim = SimulationHelper.getSimulationItem(SIYoubotGripper.class, simulation);
		addTask(new PeriodicTask(0.05) {
			@Override
			public void doPeriodicTask() {
				busy = SimulationHelper.callSimulationItem(sim, () -> sim.isBusy(), true);
			}
		});
	}

	@Override
	public boolean gripperBusy() {
		return busy;
	}

	@Override
	public boolean setGripperPosition(double distance) {
		return SimulationHelper.callSimulationItem(sim, () -> sim.setPosition(lastPos = distance), false);
	}

	@Override
	public double getGripperPosition() {
		return lastPos;
	}

}
