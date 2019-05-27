/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2010-2017 ISSE, University of Augsburg 
 */

package org.roboticsapi.kuka.youbot.javarcc.primitives;

import org.roboticsapi.runtime.javarcc.JInPort;
import org.roboticsapi.runtime.javarcc.JOutPort;
import org.roboticsapi.runtime.javarcc.JPrimitive;
import org.roboticsapi.runtime.world.javarcc.primitives.RPICalc;
import org.roboticsapi.runtime.world.types.RPIFrame;
import org.roboticsapi.world.mutable.MutableRotation;
import org.roboticsapi.world.mutable.MutableTransformation;
import org.roboticsapi.world.mutable.MutableVector;

public class JPositionPreservingProject extends JPrimitive {
	private JOutPort<RPIFrame> outValue = add("outValue", new JOutPort<RPIFrame>());
	private JInPort<RPIFrame> inFlange = add("inFlange", new JInPort<RPIFrame>());
	private JInPort<RPIFrame> inMotionCenter = add("inMotionCenter", new JInPort<RPIFrame>());
	private RPIFrame value = RPICalc.rpiFrameCreate();

	public void checkParameters() throws IllegalArgumentException {
		connected(inFlange, inMotionCenter);
	}

	@Override
	public void updateData() {
		if (anyNull(inFlange, inMotionCenter))
			return;
		RPIFrame flange = inFlange.get();
		RPIFrame mc = inMotionCenter.get();
		outValue.set(project(flange, mc, value));
	}

	MutableTransformation f = RPICalc.frameCreate();
	MutableTransformation mcp = RPICalc.frameCreate();
	MutableRotation rot = RPICalc.rotationCreate();
	MutableRotation rot2 = RPICalc.rotationCreate();
	MutableTransformation frame = RPICalc.frameCreate();
	MutableVector fm = RPICalc.vectorCreate();
	MutableVector v = RPICalc.vectorCreate();
	MutableVector axis = RPICalc.vectorCreate();
	MutableVector tmp = RPICalc.vectorCreate();
	MutableVector tmp1 = RPICalc.vectorCreate();
	MutableVector tmp2 = RPICalc.vectorCreate();

	private RPIFrame project(RPIFrame flange, RPIFrame motionCenter, RPIFrame ret) {
		RPICalc.rpiToFrame(flange, f);
		RPICalc.rpiToFrame(motionCenter, mcp);
		f.getRotation().multiplyTo(mcp.getRotation(), mcp.getRotation());
		f.getTranslation().scaleTo(-1, fm);
		fm.add(mcp.getTranslation());
		MutableRotation frot = f.getRotation();
		MutableVector fpos = f.getTranslation();
		MutableRotation mcprot = mcp.getRotation();
		MutableVector mcppos = mcp.getTranslation();

		// flange position vector in XY plane
		fpos.setZ(0);
		// flange orientation unit Z vector in XY plane
		v.set(frot.getQ02(), frot.getQ12(), 0);
		// RPIVector v = flange.getRot().UnitZ();
		// v.getX(), v.getY(), new RPIdouble(0));
		// motion center vector in XY plane
		mcppos.setZ(0);
		// flange to motion center vector in XY plane
		fm.setZ(0);

		// v points up or down - everything's fine
		if (v.getLength() < 0.001) {
			return flange;
		}

		// _f and v are parallel - everything's fine
		fpos.crossTo(v, tmp);
		if (tmp.getLength() < 0.001) {
			return flange;
		}

		// find angles to rotate up/down
		MutableVector frotz = new MutableVector();
		MutableVector zVec = new MutableVector();
		frotz.set(frot.getQ02(), frot.getQ12(), frot.getQ22());
		zVec.set(0, 0, 1);
		double uRot = Math.acos(frotz.dot(zVec));
		zVec.set(0, 0, -1);
		double dRot = Math.acos(frotz.dot(zVec));

		// find angles to rotate forward/backward
		double fRot = 0, bRot = 0;
		if (fm.getLength() < 0.001) {
			// m is zero - just turn v to point towards _f
			fRot = sanatize(angle2D(fpos, v));
			fpos.scaleTo(-1, tmp);
			bRot = sanatize(angle2D(tmp, v));

		} else {
			// m is not zero - calculate rotation angle
			{
				// looking at the triangle O-_f-m
				// the angle O-_f-m should be equal to the one between v and fm
				double angle = angle2D(v, fm);

				// compute the other angles
				double angle2 = Math.asin(fm.getLength() / mcppos.getLength() * Math.sin(Math.abs(angle)));
				double angle3 = Math.PI - Math.abs(angle) - angle2;

				if (angle < 0)
					angle3 = -angle3;

				fm.scaleTo(-1, tmp1);
				mcppos.scaleTo(-1, tmp2);
				double curAngle = angle2D(tmp1, tmp2);

				fRot = sanatize(angle3 - curAngle);

			}
			{
				// looking at the triangle O-_f-m
				// the angle O-_f-m should be equal to the one between -v and fm
				v.scaleTo(-1, tmp);
				double angle = angle2D(tmp, fm);

				// compute the other angles
				double angle2 = Math.asin(fm.getLength() / mcppos.getLength() * Math.sin(Math.abs(angle)));
				double angle3 = Math.PI - Math.abs(angle) - angle2;

				if (angle < 0)
					angle3 = -angle3;

				fm.scaleTo(-1, tmp1);
				mcppos.scaleTo(-1, tmp2);
				double curAngle = angle2D(tmp1, tmp2);

				bRot = sanatize(angle3 - curAngle);
			}
		}
		if (Math.min(Math.abs(fRot), Math.abs(bRot)) < Math.min(Math.abs(uRot), Math.abs(dRot))) {
			axis.set(0, 0, Math.abs(fRot) < Math.abs(bRot) ? fRot : bRot);
		} else {
			if (Math.abs(uRot) < Math.abs(dRot)) {
				tmp2.set(0, 0, 1);
				frotz.crossTo(tmp2, tmp1);
				tmp1.normalize();
				tmp1.scaleTo(uRot, axis);
			} else {
				tmp2.set(0, 0, -1);
				frotz.crossTo(tmp2, tmp1);
				tmp1.normalize();
				tmp1.scaleTo(dRot, axis);
			}
		}
		// _rot = _rot(mcp)^-1 * _rot(axis) * _rot(mcp)
		rot.setAxis(axis);
		mcprot.invertTo(rot2);
		rot2.multiplyTo(rot, rot);
		rot.multiply(mcprot);

		// frame = mcp * frame(_rot, 0) * mcp^-1
		rot.copyTo(frame.getRotation());
		frame.getTranslation().set(0, 0, 0);
		mcp.multiplyTo(frame, frame);
		frame.invert();
		frame.multiply(mcp);

		RPICalc.frameToRpi(frame, ret);
		return ret;
	}

	private double sanatize(double angle) {
		while (angle < -Math.PI)
			angle += Math.PI * 2;
		while (angle > Math.PI)
			angle -= Math.PI * 2;
		return angle;
	}

	double angle2D(MutableVector x, MutableVector y) {
		return Math.atan2(x.getY(), x.getX()) - Math.atan2(y.getY(), y.getX());
	}

}
