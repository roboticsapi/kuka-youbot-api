/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2013-2019 ISSE, University of Augsburg 
 */

package org.roboticsapi.device.kuka.youbot.extension;

import org.roboticsapi.device.kuka.youbot.Youbot;
import org.roboticsapi.device.kuka.youbot.YoubotArm;
import org.roboticsapi.device.kuka.youbot.YoubotFinger;
import org.roboticsapi.device.kuka.youbot.YoubotGripper;
import org.roboticsapi.device.kuka.youbot.YoubotPlatform;
import org.roboticsapi.device.kuka.youbot.YoubotSoftGripper;
import org.roboticsapi.extension.AbstractRoboticsObjectBuilder;

public class YoubotExtension extends AbstractRoboticsObjectBuilder {

	public YoubotExtension() {
		super(Youbot.class, YoubotPlatform.class, YoubotGripper.class, YoubotFinger.class, YoubotArm.class,
				YoubotSoftGripper.class);
	}

}
