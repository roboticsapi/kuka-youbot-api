/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2010-2017 ISSE, University of Augsburg 
 */

package org.roboticsapi.kuka.youbot.sensor;

import org.roboticsapi.core.RoboticsRuntime;
import org.roboticsapi.world.sensor.RelationSensor;

public class PositionPreservingFrameProjectionSensor extends YoubotArmFrameProjectionSensor {

	public PositionPreservingFrameProjectionSensor(RoboticsRuntime runtime, RelationSensor toProject,
			RelationSensor flangeToMotionCenter) {
		super(runtime, toProject, flangeToMotionCenter);
	}

}
