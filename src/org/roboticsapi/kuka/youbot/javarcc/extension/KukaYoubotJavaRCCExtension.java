/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2010-2017 ISSE, University of Augsburg 
 */

package org.roboticsapi.kuka.youbot.javarcc.extension;

import org.roboticsapi.kuka.youbot.javarcc.devices.JMockYoubotArm;
import org.roboticsapi.kuka.youbot.javarcc.devices.JMockYoubotGripper;
import org.roboticsapi.kuka.youbot.javarcc.devices.JMockYoubotPlatform;
import org.roboticsapi.kuka.youbot.javarcc.primitives.JArmControlStrategy;
import org.roboticsapi.kuka.youbot.javarcc.primitives.JGripper;
import org.roboticsapi.kuka.youbot.javarcc.primitives.JGripperMonitor;
import org.roboticsapi.kuka.youbot.javarcc.primitives.JJointImpParameters;
import org.roboticsapi.kuka.youbot.javarcc.primitives.JOrientationPreservingProject;
import org.roboticsapi.kuka.youbot.javarcc.primitives.JPositionPreservingProject;
import org.roboticsapi.runtime.javarcc.extension.JavaRCCExtension;
import org.roboticsapi.runtime.javarcc.extension.JavaRCCExtensionPoint;

public class KukaYoubotJavaRCCExtension extends JavaRCCExtension {

	@Override
	public void extend(JavaRCCExtensionPoint ep) {
		ep.registerPrimitive("KUKA::youBot::Gripper", JGripper.class);
		ep.registerPrimitive("KUKA::youBot::GripperMonitor", JGripperMonitor.class);
		ep.registerPrimitive("KUKA::youBot::PositionPreservingProject", JPositionPreservingProject.class);
		ep.registerPrimitive("KUKA::youBot::OrientationPreservingProject", JOrientationPreservingProject.class);
		ep.registerPrimitive("KUKA::youBot::ArmControlStrategy", JArmControlStrategy.class);
		ep.registerPrimitive("KUKA::youBot::JointImpParameters", JJointImpParameters.class);

		ep.registerDevice("kuka_youbot_arm_sim", (p, d) -> new JMockYoubotArm());
		ep.registerDevice("kuka_youbot_base_sim", (p, d) -> new JMockYoubotPlatform(""));
		ep.registerDevice("kuka_youbot_gripper_sim", (p, d) -> new JMockYoubotGripper());

	}
}
