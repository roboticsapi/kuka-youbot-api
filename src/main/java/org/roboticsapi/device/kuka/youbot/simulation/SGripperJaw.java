/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2013-2019 ISSE, University of Augsburg 
 */

package org.roboticsapi.device.kuka.youbot.simulation;

import org.roboticsapi.core.world.mutable.MutableTransformation;
import org.roboticsapi.facet.simulation.SEntity;

public class SGripperJaw extends SEntity {

	protected double pos;
	private MutableTransformation relativePosition = new MutableTransformation();

	public SGripperJaw(SYoubotGripper gripper) {
		if (gripper == null)
			throw new IllegalArgumentException("gripper");
		setParent(gripper);
	}

	@Override
	public double getSimulationHz() {
		return 1;
	}

	@Override
	public void simulateStep(Long time) {
	}

	protected void setPos(double pos) {
		this.pos = pos;

		relativePosition.setVectorEuler(0, pos, 0, 0, 0, 0);
	}

	@Override
	public MutableTransformation getRelativePosition() {
		return relativePosition;
	}

}
