/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2013-2019 ISSE, University of Augsburg 
 */

package org.roboticsapi.device.kuka.youbot.simulation;

import java.rmi.RemoteException;

import org.roboticsapi.core.world.mutable.MutableTransformation;
import org.roboticsapi.device.kuka.youbot.javarcc.devices.SIYoubotGripper;
import org.roboticsapi.facet.javarcc.simulation.AbstractSimulationItem;
import org.roboticsapi.facet.javarcc.simulation.SimulationHelper;
import org.roboticsapi.facet.simulation.SEntity;

public class SYoubotGripper extends SEntity {
	double pos = 0;
	double goal = 0;
	private SGripperJaw leftJaw = new SGripperJaw(this), rightJaw = new SGripperJaw(this);

	private class YoubotGripper extends AbstractSimulationItem implements SIYoubotGripper {
		protected YoubotGripper() throws RemoteException {
			super();
		}

		private static final long serialVersionUID = -295121373804069332L;

		@Override
		public boolean setPosition(double distance) throws RemoteException {
			return SYoubotGripper.this.setPosition(distance);
		}

		@Override
		public boolean isBusy() throws RemoteException {
			return SYoubotGripper.this.isBusy();
		}

	}

	@Override
	protected void afterInitialization() {
		super.afterInitialization();
		getWorld().addEntity(leftJaw);
		getWorld().addEntity(rightJaw);
		SimulationHelper.registerSimuationItem(getIdentifier(), () -> new YoubotGripper());
	}

	@Override
	protected void beforeUninitialization() {
		SimulationHelper.unregisterSimulationItem(getIdentifier());
		getWorld().removeEntity(leftJaw);
		getWorld().removeEntity(rightJaw);
		super.beforeUninitialization();
	}

	private MutableTransformation relativePosition = new MutableTransformation();

	@Override
	public MutableTransformation getRelativePosition() {
		return relativePosition;
	}

	@Override
	public double getSimulationHz() {
		return 20;
	}

	@Override
	public void simulateStep(Long time) {
		double dt = 1 / getSimulationHz();
		pos += Math.signum(goal - pos) * dt * 0.02;
		if (Math.abs(goal - pos) < 0.001)
			goal = pos;
		leftJaw.setPos(pos / 2);
		rightJaw.setPos(-pos / 2);

		relativePosition.setVectorEuler(0.008d, 0d, 0d, 0d, 0d, 0d);
	}

	public boolean setPosition(double distance) {
		if (!isBusy()) {
			goal = distance;
			return true;
		} else {
			return false;
		}
	}

	public boolean isBusy() {
		return pos != goal;
	}

}
