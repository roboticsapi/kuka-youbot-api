/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2013-2019 ISSE, University of Augsburg 
 */

package org.roboticsapi.device.kuka.youbot.simulation.extension;

import org.roboticsapi.device.kuka.youbot.simulation.SYoubotArm;
import org.roboticsapi.device.kuka.youbot.simulation.SYoubotGripper;
import org.roboticsapi.device.kuka.youbot.simulation.SYoubotPlatform;
import org.roboticsapi.device.kuka.youbot.simulation.YoubotArmSimulation;
import org.roboticsapi.device.kuka.youbot.simulation.YoubotGripperSimulation;
import org.roboticsapi.device.kuka.youbot.simulation.YoubotPlatformSimulation;
import org.roboticsapi.device.kuka.youbot.simulation.YoubotSimulation;
import org.roboticsapi.extension.AbstractRoboticsObjectBuilder;

public class YoubotSimulationExtension extends AbstractRoboticsObjectBuilder {
	public YoubotSimulationExtension() {
		super(SYoubotGripper.class, SYoubotPlatform.class, SYoubotArm.class, YoubotArmSimulation.class,
				YoubotGripperSimulation.class, YoubotPlatformSimulation.class, YoubotSimulation.class);
	}
}
