/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2010-2017 ISSE, University of Augsburg 
 */

package org.roboticsapi.kuka.youbot.sensor;

import org.roboticsapi.core.RoboticsRuntime;
import org.roboticsapi.world.sensor.RelationSensor;
import org.roboticsapi.world.sensor.TransformationSensor;

public abstract class YoubotArmFrameProjectionSensor extends TransformationSensor {

	protected final RelationSensor toProject;
	protected final RelationSensor flangeToMotionCenter;

	public YoubotArmFrameProjectionSensor(RoboticsRuntime runtime, RelationSensor toProject,
			RelationSensor flangeToMotionCenter) {
		super(runtime);
		this.toProject = toProject;
		this.flangeToMotionCenter = flangeToMotionCenter;
	}

	@Override
	public boolean isAvailable() {
		return toProject.isAvailable() && flangeToMotionCenter.isAvailable();
	}

	public RelationSensor getToProject() {
		return toProject;
	}

	public RelationSensor getFlangeToMotionCenter() {
		return flangeToMotionCenter;
	}

}