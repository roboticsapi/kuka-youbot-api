/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2010-2017 ISSE, University of Augsburg 
 */

package org.roboticsapi.kuka.youbot.javarcc.devices;

import org.roboticsapi.runtime.robot.javarcc.DHKinematics;
import org.roboticsapi.runtime.world.javarcc.primitives.RPICalc;
import org.roboticsapi.runtime.world.types.RPIFrame;
import org.roboticsapi.world.mutable.MutableRotation;
import org.roboticsapi.world.mutable.MutableTransformation;
import org.roboticsapi.world.mutable.MutableVector;

public class YoubotArmKin {

	static DHKinematics dhkin = new DHKinematics(new double[] { -0.147, 0, 0, 0, -0.171 },
			new double[] { 0, -1.5708, 0, 1.5708, 0 }, new double[] { 0.033, 0.155, 0.135, 0, 0 },
			new double[] { 1.5708, 0, 0, -1.5708, 3.142 }, true);

	public double[] invKin(RPIFrame pos, double[] hintJoints) {
		double b1, b2, b3, b4, b5, bestDiff = 999;
		double jnt1 = hintJoints[0], jnt2 = hintJoints[1], jnt3 = hintJoints[2], jnt4 = hintJoints[3],
				jnt5 = hintJoints[4];
		b1 = b2 = b3 = b4 = b5 = Double.NaN;
		for (int solution = 0; solution < 4; solution++) {
			double[] js = ik(pos, solution);
			if (js != null) {
				double j1 = js[0], j2 = js[1], j3 = js[2], j4 = js[3], j5 = js[4];
				double diff = Math.abs(j1 - jnt1) + Math.abs(j2 - jnt2) + Math.abs(j3 - jnt3) + Math.abs(j4 - jnt4)
						+ Math.abs(j5 - jnt5);
				if (diff < bestDiff) {
					bestDiff = diff;
					b1 = j1;
					b2 = j2;
					b3 = j3;
					b4 = j4;
					b5 = j5;
				}
			}
		}

		return new double[] { b1, b2, b3, b4, b5 };
	}

	private final MutableTransformation _frame0_to_frame1 = RPICalc.frameCreate();
	private final MutableTransformation _frame1_to_frame2 = RPICalc.frameCreate();
	private final MutableTransformation _g0 = RPICalc.frameCreate();
	private final MutableTransformation _g1 = RPICalc.frameCreate();
	private final MutableTransformation _g2 = RPICalc.frameCreate();
	private final MutableVector _p2 = RPICalc.vectorCreate();

	private double[] ik(RPIFrame g0, int solution) {
		double EPS = 0.000001;

		boolean offset_joint_1 = (solution & 1) != 0;
		boolean offset_joint_3 = (solution & 2) != 0;

		// arm lenghts
		double l0x = 0.0;
		double l0z = 0.147;

		double l1x = 0.033;
		double l1z = 0;

		double l2 = 0.155;
		double l3 = 0.135;

		// Distance from arm_link_3 to arm_link_5 (can also be replaced by e.g.
		// distance from arm_link_3 to tool center point)
		double d = 0.171;

		double j1;
		double j2;
		double j3;
		double j4;
		double j5;

		// Transform from frame 0 to frame 1
		RPICalc.rpiToFrame(g0, _g0);
		_frame0_to_frame1.setVectorEuler(-l0x, 0, -l0z, 0, 0, 0);
		_frame0_to_frame1.multiplyTo(_g0, _g1);
		MutableVector g1pos = _g1.getTranslation();
		MutableRotation g1rot = _g1.getRotation();

		// First joint
		j1 = Math.atan2(g1pos.getY(), g1pos.getX());
		if (offset_joint_1) {
			if (j1 < 0.0)
				j1 += Math.PI;
			else
				j1 -= Math.PI;
		}

		// Transform from frame 1 to frame 2
		_frame1_to_frame2.setVectorEuler(-l1x, 0, -l1z, -j1, 0, 0);
		_frame1_to_frame2.multiplyTo(_g1, _g2);
		MutableVector g2pos = _g2.getTranslation();
		MutableRotation g2rot = _g2.getRotation();

		// Check if Z vector of hand is in XZ-plane
		MutableVector z = new MutableVector();
		z.set(g2rot.getQ02(), g2rot.getQ12(), g2rot.getQ22());
		MutableVector y = new MutableVector();
		y.set(0, 1, 0);
		if (Math.abs(z.dot(y)) > 0.01) {
			return null;
		}

		// Set all values in the frame that are close to zero to exactly zero
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (Math.abs(g2rot.get(i, j)) < EPS)
					g2rot.set(i, j, 0);
			}
		}

		// Fifth joint, determines the roll of the gripper (= wrist angle)
		double s1 = Math.sin(j1);
		double c1 = Math.cos(j1);
		double r11 = g1rot.getQ00();
		double r12 = g1rot.getQ01();
		double r21 = g1rot.getQ10();
		double r22 = g1rot.getQ11();
		j5 = Math.atan2(r21 * c1 - r11 * s1, r22 * c1 - r12 * s1);

		// The sum of joint angles two to four determines the overall "pitch" of
		// the end effector
		double r13 = g2rot.getQ02();
		double r33 = g2rot.getQ22();
		double j234 = Math.atan2(r13, r33);
		_p2.set(g2pos.getX() - d * Math.sin(j234), g2pos.getY(), g2pos.getZ() - d * Math.cos(j234));

		// Check if the goal position can be reached at all
		if ((l2 + l3) + 1e-3 < Math.sqrt((_p2.getX() * _p2.getX()) + (_p2.getZ() * _p2.getZ()))) {
			return null;
		}

		// Third joint
		double j3_cos = ((_p2.getX() * _p2.getX()) + (_p2.getZ() * _p2.getZ()) - (l2 * l2) - (l3 * l3)) / (2 * l2 * l3);
		if (j3_cos > 1.0 - EPS)
			j3 = 0.0;
		else if (j3_cos < -1.0 + EPS)
			j3 = Math.PI;
		else
			j3 = Math.acos(j3_cos);

		if (offset_joint_3)
			j3 = -j3;

		// Second joint
		double t1 = Math.atan2(-_p2.getX(), _p2.getZ());
		double t2 = Math.atan2(l3 * Math.sin(j3), l2 + l3 * Math.cos(j3));

		if (j3 > 0)
			t2 = -t2;
		if (offset_joint_3)
			t2 = -t2;
		j2 = -t1 + t2;

		// Fourth joint, determines the pitch of the gripper

		j4 = j234 - j2 - j3;

		// normalize j4
		while (j4 > Math.PI)
			j4 -= 2 * Math.PI;

		while (j4 < -Math.PI)
			j4 += 2 * Math.PI;

		return new double[] { -j1, j2, j3, j4, -j5 };
	}

	public RPIFrame kin(double[] joints, RPIFrame ret) {
		return dhkin.kin(joints, ret);
	}

}
