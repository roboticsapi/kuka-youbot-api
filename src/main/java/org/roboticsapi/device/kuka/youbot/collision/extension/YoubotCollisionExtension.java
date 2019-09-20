/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2013-2019 ISSE, University of Augsburg 
 */

package org.roboticsapi.device.kuka.youbot.collision.extension;

import java.util.HashSet;
import java.util.Set;

import org.roboticsapi.core.RoboticsObject;
import org.roboticsapi.core.world.PhysicalObject;
import org.roboticsapi.core.world.Transformation;
import org.roboticsapi.device.kuka.youbot.YoubotArm;
import org.roboticsapi.device.kuka.youbot.YoubotPlatform;
import org.roboticsapi.extension.RoboticsObjectListener;
import org.roboticsapi.facet.collision.properties.CollisionFriendsProperty;
import org.roboticsapi.facet.collision.properties.CollisionShapeProperty;
import org.roboticsapi.facet.collision.shapes.BoxShape;
import org.roboticsapi.facet.collision.shapes.SphereShape;
import org.roboticsapi.framework.multijoint.Joint;
import org.roboticsapi.framework.multijoint.link.Link;
import org.roboticsapi.framework.platform.Wheel;

public class YoubotCollisionExtension implements RoboticsObjectListener {

	private static final CollisionShapeProperty[] HULLPROPERTIES = new CollisionShapeProperty[] {
			new CollisionShapeProperty("YoubotArm_Link0",
					new BoxShape(new Transformation(0, 0, 0.039, 0, 0, 0), 0.103, 0.093, 0.039)),
			new CollisionShapeProperty("YoubotArm_Link1",
					new BoxShape(new Transformation(0.00, 0.042, -0.066, 0, 0, 0), 0.086, 0.042, 0.066)),
			new CollisionShapeProperty("YoubotArm_Link2",
					new BoxShape(new Transformation(0, -0.075, 0.045, 0, 0, 0), 0.037, 0.110, 0.045)),
			new CollisionShapeProperty("YoubotArm_Link3",
					new BoxShape(new Transformation(0.068, 0.000, -0.041, 0, 0, 0), 0.096, 0.032, 0.041)),
			new CollisionShapeProperty("YoubotArm_Link4",
					new BoxShape(new Transformation(0.037, 0, 0.025, 0, 0, 0), 0.058, 0.033, 0.025)),
			new CollisionShapeProperty("YoubotArm_Link5",
					new BoxShape(new Transformation(0, 0, -0.013, 0, 0, 0), 0.026, 0.048, 0.013)) };

	@Override
	public void onAvailable(RoboticsObject object) {
		if (object instanceof YoubotPlatform) {
			YoubotPlatform robot = (YoubotPlatform) object;

			robot.addProperty(new CollisionShapeProperty("YoubotPlatform",
					new BoxShape(new Transformation(0, 0, -0.004, 0, 0, 0), 0.29, 0.112, 0.06)));
			for (Wheel w : robot.getWheels()) {
				w.addProperty(new CollisionShapeProperty("wheel", new SphereShape(Transformation.IDENTITY, 0.037)));
			}

		} else if (object instanceof YoubotArm) {
			YoubotArm robot = (YoubotArm) object;

			for (int i = 0; i < robot.getLinks().length; i++) {
				Link link = robot.getLink(i);

				if (link == null) {
					continue;
				}
				link.addProperty(HULLPROPERTIES[i]);
			}

			for (int i = 0; i < robot.getJointCount() - 1; i++) {
				Joint joint = robot.getJoint(i);

				Set<PhysicalObject> friends = new HashSet<PhysicalObject>();
				friends.add(robot.getLink(i));
				friends.add(robot.getLink(i + 1));

				CollisionFriendsProperty prop = new CollisionFriendsProperty(friends);
				joint.addProperty(prop);
			}
		}
	}

	@Override
	public void onUnavailable(RoboticsObject object) {
		// do nothing...
	}

}
