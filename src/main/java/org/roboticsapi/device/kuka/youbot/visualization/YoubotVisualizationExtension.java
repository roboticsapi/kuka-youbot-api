/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2013-2019 ISSE, University of Augsburg 
 */

package org.roboticsapi.device.kuka.youbot.visualization;

import java.net.URL;

import org.roboticsapi.core.RoboticsObject;
import org.roboticsapi.core.world.PhysicalObject;
import org.roboticsapi.device.kuka.youbot.YoubotArm;
import org.roboticsapi.device.kuka.youbot.YoubotFinger;
import org.roboticsapi.device.kuka.youbot.YoubotGripper;
import org.roboticsapi.device.kuka.youbot.YoubotPlatform;
import org.roboticsapi.device.kuka.youbot.YoubotSoftGripper;
import org.roboticsapi.extension.PluginableExtension;
import org.roboticsapi.extension.RoboticsObjectListener;
import org.roboticsapi.facet.visualization.property.Visualisation;
import org.roboticsapi.facet.visualization.property.VisualizationProperty;
import org.roboticsapi.framework.multijoint.link.Link;

public class YoubotVisualizationExtension implements RoboticsObjectListener, PluginableExtension {

	private static final Visualisation[] PLATFORM_MODELS = new Visualisation[] {
			new Visualisation("COLLADA", getResource("YoubotPlatform.dae")),
			new Visualisation("COLLADA", getResource("YoubotPlatform_FrontLeftWheel.dae")),
			new Visualisation("COLLADA", getResource("YoubotPlatform_FrontRightWheel.dae")),
			new Visualisation("COLLADA", getResource("YoubotPlatform_BackLeftWheel.dae")),
			new Visualisation("COLLADA", getResource("YoubotPlatform_BackRightWheel.dae")), };

	private static final Visualisation[] ARM_MODELS = new Visualisation[] {
			new Visualisation("COLLADA", getResource("YoubotArm_Link0.dae")),
			new Visualisation("COLLADA", getResource("YoubotArm_Link1.dae")),
			new Visualisation("COLLADA", getResource("YoubotArm_Link2.dae")),
			new Visualisation("COLLADA", getResource("YoubotArm_Link3.dae")),
			new Visualisation("COLLADA", getResource("YoubotArm_Link4.dae")),
			new Visualisation("COLLADA", getResource("YoubotArm_Link5.dae")), };

	private static final Visualisation[] GRIPPER_MODELS = new Visualisation[] {
			new Visualisation("COLLADA", getResource("YoubotGripper.dae")),
			new Visualisation("COLLADA", getResource("YoubotGripper_BaseJaw.dae")),
			new Visualisation("COLLADA", getResource("YoubotGripper_LeftFinger.dae")),
			new Visualisation("COLLADA", getResource("YoubotGripper_SoftFinger.dae")),
			new Visualisation("COLLADA", getResource("YoubotGripper_SoftFingerInnerLink.dae")),
			new Visualisation("COLLADA", getResource("YoubotGripper_SoftFingerOuterLink.dae")) };

	@Override
	public void onAvailable(RoboticsObject object) {
		if (object instanceof YoubotPlatform) {
			YoubotPlatform platform = (YoubotPlatform) object;

			platform.addProperty(new VisualizationProperty(PLATFORM_MODELS[0]));
			platform.getFrontLeftWheel().addProperty(new VisualizationProperty(PLATFORM_MODELS[1]));
			platform.getFrontRightWheel().addProperty(new VisualizationProperty(PLATFORM_MODELS[2]));
			platform.getBackLeftWheel().addProperty(new VisualizationProperty(PLATFORM_MODELS[3]));
			platform.getBackRightWheel().addProperty(new VisualizationProperty(PLATFORM_MODELS[4]));
		}

		if (object instanceof YoubotArm) {
			YoubotArm arm = (YoubotArm) object;
			Link[] links = arm.getLinks();

			for (int i = 0; i < links.length; i++) {
				links[i].addProperty(new VisualizationProperty(ARM_MODELS[i]));
			}
		}

		if (object instanceof YoubotGripper) {
			YoubotGripper gripper = (YoubotGripper) object;
			gripper.getBaseJaw(0).addProperty(new VisualizationProperty(GRIPPER_MODELS[1]));
			gripper.getBaseJaw(1).addProperty(new VisualizationProperty(GRIPPER_MODELS[1]));
			gripper.addProperty(new VisualizationProperty(GRIPPER_MODELS[0]));
		}

		if (object instanceof YoubotSoftGripper) {
			YoubotSoftGripper gripper = (YoubotSoftGripper) object;
			((PhysicalObject) gripper.getLeftFinger()).addProperty(new VisualizationProperty(GRIPPER_MODELS[3]));
			((PhysicalObject) gripper.getRightFinger()).addProperty(new VisualizationProperty(GRIPPER_MODELS[3]));
			((PhysicalObject) gripper.getLinks()[0]).addProperty(new VisualizationProperty(GRIPPER_MODELS[4]));
			((PhysicalObject) gripper.getLinks()[1]).addProperty(new VisualizationProperty(GRIPPER_MODELS[5]));
			((PhysicalObject) gripper.getLinks()[2]).addProperty(new VisualizationProperty(GRIPPER_MODELS[4]));
			((PhysicalObject) gripper.getLinks()[3]).addProperty(new VisualizationProperty(GRIPPER_MODELS[5]));
		}

		if (object instanceof YoubotFinger) {
			YoubotFinger finger = (YoubotFinger) object;

			finger.addProperty(new VisualizationProperty(GRIPPER_MODELS[2]));
		}
	}

	@Override
	public void onUnavailable(RoboticsObject object) {
		// do nothing...
	}

	private static final URL getResource(String resource) {
		return YoubotVisualizationExtension.class.getClassLoader().getResource("models/" + resource);
	}

	@Override
	public void activate() {
	}

	@Override
	public void deactivate() {
	}

	@Override
	public String getName() {
		return "KUKA youBot Collada visualization model";
	}

	@Override
	public String getDescription() {
		return "Collada model of KUKA youBot to display it in Robotics API Visualization";
	}

}
