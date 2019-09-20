/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2013-2019 ISSE, University of Augsburg 
 */

package org.roboticsapi.device.kuka.youbot.javarcc.extension;

import org.roboticsapi.device.kuka.youbot.javarcc.devices.JMockYoubotArm;
import org.roboticsapi.device.kuka.youbot.javarcc.devices.JMockYoubotGripper;
import org.roboticsapi.device.kuka.youbot.javarcc.devices.JMockYoubotPlatform;
import org.roboticsapi.device.kuka.youbot.javarcc.devices.JSimulatedYoubotGripper;
import org.roboticsapi.device.kuka.youbot.javarcc.devices.JYoubotArm;
import org.roboticsapi.device.kuka.youbot.javarcc.devices.JYoubotPlatform;
import org.roboticsapi.device.kuka.youbot.javarcc.primitives.JArmControlStrategy;
import org.roboticsapi.device.kuka.youbot.javarcc.primitives.JGripper;
import org.roboticsapi.device.kuka.youbot.javarcc.primitives.JGripperMonitor;
import org.roboticsapi.device.kuka.youbot.javarcc.primitives.JJointImpParameters;
import org.roboticsapi.device.kuka.youbot.javarcc.primitives.JOrientationPreservingProject;
import org.roboticsapi.device.kuka.youbot.javarcc.primitives.JPositionPreservingProject;
import org.roboticsapi.facet.javarcc.extension.JavaRccExtension;
import org.roboticsapi.facet.javarcc.extension.JavaRccExtensionPoint;

public class YoubotJavaRccExtension extends JavaRccExtension {

	@Override
	public void extend(JavaRccExtensionPoint ep) {
		ep.registerPrimitive("KUKA::youBot::Gripper", JGripper.class);
		ep.registerPrimitive("KUKA::youBot::GripperMonitor", JGripperMonitor.class);
		ep.registerPrimitive("KUKA::youBot::PositionPreservingProject", JPositionPreservingProject.class);
		ep.registerPrimitive("KUKA::youBot::OrientationPreservingProject", JOrientationPreservingProject.class);
		ep.registerPrimitive("KUKA::youBot::ArmControlStrategy", JArmControlStrategy.class);
		ep.registerPrimitive("KUKA::youBot::JointImpParameters", JJointImpParameters.class);

		ep.registerDevice("kuka::youbot::arm::sim", (p, d) -> new JYoubotArm("", p.get("simulation")));
		ep.registerDevice("kuka::youbot::platform::sim", (p, d) -> new JYoubotPlatform("", p.get("simulation")));
		ep.registerDevice("kuka::youbot::gripper::sim", (p, d) -> new JSimulatedYoubotGripper(p.get("simulation")));

		ep.registerDevice("kuka_youbot_arm_sim", (p, d) -> new JMockYoubotArm());
		ep.registerDevice("kuka_youbot_base_sim", (p, d) -> new JMockYoubotPlatform(""));
		ep.registerDevice("kuka_youbot_gripper_sim", (p, d) -> new JMockYoubotGripper());

	}
}
