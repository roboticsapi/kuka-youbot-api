/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2010-2017 ISSE, University of Augsburg 
 */

package org.roboticsapi.kuka.youbot.runtime.mapper;

import org.roboticsapi.kuka.youbot.sensor.OrientationPreservingFrameProjectionSensor;
import org.roboticsapi.runtime.mapping.MappingException;
import org.roboticsapi.runtime.mapping.net.DataflowOutPort;
import org.roboticsapi.runtime.mapping.net.NetFragment;

public class YoubotArmOrientationPreservingFrameProjectionSensorMapper
		extends AbstractYoubotArmFrameProjectionSensorMapper<OrientationPreservingFrameProjectionSensor> {

	@Override
	protected DataflowOutPort addProjector(NetFragment fragment, DataflowOutPort toProject,
			DataflowOutPort flangeToMotionCenter) throws MappingException {
		throw new MappingException("Not implemented");
	}
}
