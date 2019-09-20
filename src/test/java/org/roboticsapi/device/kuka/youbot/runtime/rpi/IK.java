/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2013-2019 ISSE, University of Augsburg 
 */

package org.roboticsapi.device.kuka.youbot.runtime.rpi;

import java.util.Arrays;

public class IK {

	public static void main(String[] args) {
		System.out.println(Arrays.toString((IK.ik(0, -0.4, 0.05))));
	}

	public static double[] ik(double x, double y, double z) {
		double r = Math.sqrt((x * x) + (y * y));
		double a0 = -Math.atan2(y / r, x / r);

		double[] ik = ik(r, z);

		double[] result = new double[5];
		result[0] = a0;
		result[1] = ik[0];
		result[2] = ik[1];
		result[3] = ik[2];
		result[4] = 0;
		return result;
	}

	private static double[] ik(double x, double z) {
		double x2 = x - 0.175;// x-0.2175;
		double y2 = z;
		double x1 = 0.033;
		double y1 = 0.147;
		double r1 = 0.155;
		double r2 = 0.135;

		double dx = x2 - x1;
		double dy = y2 - y1;

		double d = Math.sqrt((dx * dx) + (dy * dy));
		double a = ((r1 * r1) + (d * d) - (r2 * r2)) / (2 * d);
		double h = Math.sqrt((r1 * r1) - (a * a));

		// Schnittpunkt 1:
//		double xs1 = x1 + (dx*a/d) + (dy*h/d);
//		double ys1 = y1 + (dy*a/d) - (dx*h/d);

//		// Schnittpunkt 2:
		double xs1 = x1 + (dx * a / d) - (dy * h / d);
		double ys1 = y1 + (dy * a / d) + (dx * h / d);

		double a1 = (Math.PI / 2) - Math.atan((ys1 - y1) / (xs1 - x1));
		double a2 = Math.PI - ((Math.acos(h / r1)) + (Math.acos(h / r2)));
		double a3 = Math.PI / 2 - a1 - a2;

		return new double[] { a1, a2, a3 };
//		return "[?, " + a1 + ", " + a2 + ", " + a3 + ", 0.0]";
	}

}
