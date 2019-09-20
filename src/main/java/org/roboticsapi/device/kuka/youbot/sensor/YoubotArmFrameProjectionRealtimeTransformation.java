/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2013-2019 ISSE, University of Augsburg 
 */

package org.roboticsapi.device.kuka.youbot.sensor;

import org.roboticsapi.core.world.realtimevalue.realtimetransformation.RealtimeTransformation;

public abstract class YoubotArmFrameProjectionRealtimeTransformation extends RealtimeTransformation {

	protected final RealtimeTransformation toProject;
	protected final RealtimeTransformation flangeToMotionCenter;

	public YoubotArmFrameProjectionRealtimeTransformation(RealtimeTransformation toProject,
			RealtimeTransformation flangeToMotionCenter) {
		super(toProject, flangeToMotionCenter);
		this.toProject = toProject;
		this.flangeToMotionCenter = flangeToMotionCenter;
	}

	public RealtimeTransformation getToProject() {
		return toProject;
	}

	public RealtimeTransformation getFlangeToMotionCenter() {
		return flangeToMotionCenter;
	}

	@Override
	public boolean isAvailable() {
		return toProject.isAvailable() && flangeToMotionCenter.isAvailable();
	}

}