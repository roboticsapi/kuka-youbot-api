/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2013-2019 ISSE, University of Augsburg 
 */

package org.roboticsapi.device.kuka.youbot.runtime.rpi;

import java.util.Map;

import org.roboticsapi.core.realtimevalue.realtimedouble.RealtimeDouble;
import org.roboticsapi.device.kuka.youbot.YoubotGripper;
import org.roboticsapi.device.kuka.youbot.runtime.rpi.activity.YoubotGrippingInterfaceImpl;
import org.roboticsapi.facet.runtime.rpi.RpiParameters;
import org.roboticsapi.framework.gripper.GripperDriver;
import org.roboticsapi.framework.gripper.activity.GrippingInterface;
import org.roboticsapi.framework.gripper.runtime.AbstractParallelGripperDriver;

public class YoubotGripperGenericDriver extends AbstractParallelGripperDriver<YoubotGripper>
		implements GripperDriver {

	@Override
	protected boolean checkDeviceType(String deviceType) {
		// TODO: Hack! Change to interfaces for setting controller parameters
		return "kuka_youbot_gripper_sim".equals(deviceType) || "kuka_youbot_gripper_ec".equals(deviceType);
	}

	@Override
	protected boolean checkRpiDeviceInterfaces(Map<String, RpiParameters> interfaces) {
		return true;
	}

	@Override
	public final RealtimeDouble getBaseJawOpeningWidth() {
		return new YoubotGripperOpeningWidthRealtimeDouble(this);
	}

	@Override
	protected void onPresent() {
		super.onPresent();
		getDevice().addInterfaceFactory(GrippingInterface.class, () -> new YoubotGrippingInterfaceImpl(this));
	}

}
