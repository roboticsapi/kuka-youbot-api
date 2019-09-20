/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2013-2019 ISSE, University of Augsburg 
 */

package org.roboticsapi.device.kuka.youbot.runtime.rpi.mapper;

import org.roboticsapi.device.kuka.youbot.runtime.rpi.YoubotGripperOpeningWidthRealtimeDouble;
import org.roboticsapi.device.kuka.youbot.runtime.rpi.primitives.GripperMonitor;
import org.roboticsapi.facet.runtime.rpi.RpiException;
import org.roboticsapi.facet.runtime.rpi.mapping.MappingException;
import org.roboticsapi.facet.runtime.rpi.mapping.RealtimeValueFragment;
import org.roboticsapi.facet.runtime.rpi.mapping.TypedRealtimeValueFragmentFactory;
import org.roboticsapi.facet.runtime.rpi.mapping.core.RealtimeDoubleFragment;

public class YoubotGripperSensorMapper
		extends TypedRealtimeValueFragmentFactory<Double, YoubotGripperOpeningWidthRealtimeDouble> {

	public YoubotGripperSensorMapper() {
		super(YoubotGripperOpeningWidthRealtimeDouble.class);
	}

	@Override
	protected RealtimeValueFragment<Double> createFragment(YoubotGripperOpeningWidthRealtimeDouble value)
			throws MappingException, RpiException {

		return new RealtimeDoubleFragment(value,
				new GripperMonitor(value.getDriver().getRpiDeviceName()).getOutDistance());
	}
}
