/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2013-2019 ISSE, University of Augsburg 
 */

package org.roboticsapi.device.kuka.youbot.sensor;

import org.roboticsapi.core.world.realtimevalue.realtimetransformation.RealtimeTransformation;

public class OrientationPreservingFrameProjectionSensor extends YoubotArmFrameProjectionRealtimeTransformation {

	public OrientationPreservingFrameProjectionSensor(RealtimeTransformation toProject,
			RealtimeTransformation flangeToMotionCenter) {
		super(toProject, flangeToMotionCenter);
	}

}
