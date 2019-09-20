/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2013-2019 ISSE, University of Augsburg 
 */

package org.roboticsapi.device.kuka.youbot;

import org.roboticsapi.configuration.ConfigurationProperty;
import org.roboticsapi.core.Dependency;
import org.roboticsapi.core.MultiDependency;
import org.roboticsapi.core.RoboticsObject;
import org.roboticsapi.core.realtimevalue.realtimedouble.RealtimeDouble;
import org.roboticsapi.core.world.AbstractPhysicalObject;
import org.roboticsapi.core.world.Frame;
import org.roboticsapi.core.world.Transformation;
import org.roboticsapi.core.world.realtimevalue.realtimetransformation.RealtimeTransformation;
import org.roboticsapi.core.world.relation.CommandedPosition;
import org.roboticsapi.core.world.relation.StaticConnection;
import org.roboticsapi.core.world.relation.StaticPosition;
import org.roboticsapi.framework.gripper.GripperDriver;
import org.roboticsapi.framework.gripper.activity.GripperOpeningWidthSensorInterface;

public class YoubotSoftGripper extends AbstractPhysicalObject {

	private final double BASEJAW_HOLE_DISTANCE = 0.01;
	private final double INNER_LINK_LENGTH = 0.03;
	private final double OUTER_LINK_LENGTH = 0.0285;
	private final double FINGER_HOLE_DISTANCE = 0.046;

	private final Dependency<Integer> innerHole, outerHole;

	private final Dependency<YoubotGripper> gripper;
	private final Dependency<Finger> leftFinger, rightFinger;
	private final MultiDependency<Link> links;

	public YoubotSoftGripper() {
		innerHole = createDependency("innerHole", 0);
		outerHole = createDependency("outerHole", 2);
		gripper = createDependency("gripper");
		leftFinger = createDependency("leftFinger", new Finger("Left Finger"));
		rightFinger = createDependency("rightFinger", new Finger("Right Finger"));
		links = createMultiDependency("finger", new Link("Left Inner"), new Link("Left Outer"), new Link("Right Inner"),
				new Link("Right Outer"));
	}

	@ConfigurationProperty
	public void setInnerHole(int firstHole) {
		this.innerHole.set(firstHole);
	}

	@ConfigurationProperty
	public void setOuterHole(int secondHole) {
		this.outerHole.set(secondHole);
	}

	public int getInnerHole() {
		return innerHole.get();
	}

	public int getOuterHole() {
		return outerHole.get();
	}

	private ReferenceListener refListener = new ReferenceListener() {
		@Override
		public void referenceRemoved(RoboticsObject referencingObject, RoboticsObject referencedObject) {

		}

		@Override
		public void referenceAdded(RoboticsObject referencingObject, RoboticsObject referencedObject) {
			if (referencingObject instanceof GripperDriver) {
				double JAW_HOLE_DISTANCE = (outerHole.get() - innerHole.get()) * BASEJAW_HOLE_DISTANCE;
				GripperDriver driver = (GripperDriver) referencingObject;
				RealtimeDouble xOffset = driver.getDevice().use(GripperOpeningWidthSensorInterface.class)
						.getBaseJawOpeningWidth().add(0.01 + innerHole.get() * 0.02).multiply(0.5);
				RealtimeDouble zOffset = xOffset.square().negate().add(INNER_LINK_LENGTH * INNER_LINK_LENGTH).sqrt();
				RealtimeDouble diag = zOffset.square().add(xOffset.add(JAW_HOLE_DISTANCE).square()).sqrt();
				RealtimeDouble alpha = zOffset.divide(diag).acos();
				RealtimeDouble beta = diag.square().add(FINGER_HOLE_DISTANCE * FINGER_HOLE_DISTANCE)
						.add(-OUTER_LINK_LENGTH * OUTER_LINK_LENGTH)
						.divide(diag.multiply(2).multiply(FINGER_HOLE_DISTANCE)).acos();
				RealtimeDouble b = alpha.add(beta).add(Math.toRadians(-90)).negate();

				RealtimeDouble zero = RealtimeDouble.createFromConstant(0);
				RealtimeTransformation trans = RealtimeTransformation.createFromXYZABC(xOffset.negate(), zero, zOffset,
						zero, b, zero);

				RealtimeDouble f1 = b.add(zOffset.divide(INNER_LINK_LENGTH).asin().negate()).negate()
						.add(Math.toRadians(-90));
				RealtimeTransformation f1trans = RealtimeTransformation.createFromXYZABC(zero, zero, zero, zero, f1,
						zero);

				RealtimeDouble f2 = diag.square().negate()
						.add(FINGER_HOLE_DISTANCE * FINGER_HOLE_DISTANCE + OUTER_LINK_LENGTH * OUTER_LINK_LENGTH)
						.divide(2 * FINGER_HOLE_DISTANCE * OUTER_LINK_LENGTH).acos().negate().add(Math.toRadians(90));
				RealtimeTransformation f2trans = RealtimeTransformation.createFromXYZABC(
						RealtimeDouble.createFromConstant(FINGER_HOLE_DISTANCE), zero, zero, zero, f2, zero);

				Frame leftInnerHole = new Frame(), leftOuterHole = new Frame();
				new StaticConnection(gripper.get().getBaseJaw(0).getBase(), leftInnerHole).establish();
				new StaticPosition(gripper.get().getBaseJaw(0).getBase(), leftInnerHole,
						new Transformation(0.005 + innerHole.get() * 0.01, 0, 0, 0, 0, 0)).establish();
				new StaticConnection(gripper.get().getBaseJaw(0).getBase(), leftOuterHole).establish();
				new StaticPosition(gripper.get().getBaseJaw(0).getBase(), leftOuterHole,
						new Transformation(0.005 + outerHole.get() * 0.01, 0, 0, 0, 0, 0)).establish();
				Frame lf = leftFinger.get().getBase();

				new CommandedPosition(leftInnerHole, lf, trans, null).establish();
				new CommandedPosition(lf, links.get(0).getBase(), f1trans, null).establish();
				new CommandedPosition(lf, links.get(1).getBase(), f2trans, null).establish();

				Frame rightInnerHole = new Frame(), rightOuterHole = new Frame();
				new StaticConnection(gripper.get().getBaseJaw(1).getBase(), rightInnerHole).establish();
				new StaticPosition(gripper.get().getBaseJaw(1).getBase(), rightInnerHole,
						new Transformation(0.005 + innerHole.get() * 0.01, 0, 0, 0, 0, 0)).establish();

				new StaticConnection(gripper.get().getBaseJaw(1).getBase(), rightOuterHole).establish();
				new StaticPosition(gripper.get().getBaseJaw(1).getBase(), rightOuterHole,
						new Transformation(0.005 + outerHole.get() * 0.01, 0, 0, 0, 0, 0)).establish();
				Frame rf = rightFinger.get().getBase();
				new CommandedPosition(rightInnerHole, rf, trans, null).establish();
				new CommandedPosition(rf, links.get(2).getBase(), f1trans, null).establish();
				new CommandedPosition(rf, links.get(3).getBase(), f2trans, null).establish();

			}
		}
	};

	@ConfigurationProperty
	public void setGripper(YoubotGripper gripper) {
		this.gripper.set(gripper);
	}

	public YoubotGripper getGripper() {
		return gripper.get();
	}

	@Override
	protected void afterInitialization() {
		super.afterInitialization();
		gripper.get().addReferenceListener(refListener);
	}

	@Override
	protected void afterUninitialization() {
		super.afterUninitialization();
		gripper.get().removeReferenceListener(refListener);
	}

	public Finger getLeftFinger() {
		return leftFinger.get();
	}

	public Finger getRightFinger() {
		return rightFinger.get();
	}

	public Link[] getLinks() {
		return links.getAll().toArray(new Link[0]);
	}

	class Finger extends AbstractPhysicalObject {
		public Finger(String name) {
			setName(name);
		}
	}

	class Link extends AbstractPhysicalObject {
		public Link(String name) {
			setName(name);
		}
	}

}
