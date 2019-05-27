/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2010-2017 ISSE, University of Augsburg 
 */

package org.roboticsapi.kuka.youbot.extension;

import org.roboticsapi.core.RoboticsObject;
import org.roboticsapi.core.SingleDeviceInterfaceFactory;
import org.roboticsapi.extension.AbstractRoboticsObjectBuilder;
import org.roboticsapi.extension.RoboticsObjectListener;
import org.roboticsapi.kuka.youbot.YoubotArm;
import org.roboticsapi.kuka.youbot.YoubotFinger;
import org.roboticsapi.kuka.youbot.YoubotGripper;
import org.roboticsapi.kuka.youbot.YoubotPlatform;
import org.roboticsapi.kuka.youbot.activity.YoubotGrippingInterfaceImpl;
import org.roboticsapi.tool.gripper.activity.GrippingInterface;

public class YoubotExtension extends AbstractRoboticsObjectBuilder implements RoboticsObjectListener {

	public YoubotExtension() {
		super(YoubotArm.class, YoubotPlatform.class, YoubotGripper.class, YoubotFinger.class);
	}

	@Override
	public void onAvailable(final RoboticsObject object) {
		if (object instanceof YoubotGripper) {
			((YoubotGripper) object).addInterfaceFactory(new SingleDeviceInterfaceFactory<GrippingInterface>() {
				@Override
				protected GrippingInterface build() {
					return new YoubotGrippingInterfaceImpl((YoubotGripper) object);
				}
			});
		}
	}

	@Override
	public void onUnavailable(RoboticsObject object) {

	}

}
