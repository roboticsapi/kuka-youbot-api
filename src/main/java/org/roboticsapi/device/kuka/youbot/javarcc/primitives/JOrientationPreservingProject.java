/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2013-2019 ISSE, University of Augsburg 
 */

package org.roboticsapi.device.kuka.youbot.javarcc.primitives;

import org.roboticsapi.core.world.mutable.MutableRotation;
import org.roboticsapi.core.world.mutable.MutableVector;
import org.roboticsapi.facet.javarcc.JInPort;
import org.roboticsapi.facet.javarcc.JOutPort;
import org.roboticsapi.facet.javarcc.JPrimitive;
import org.roboticsapi.facet.javarcc.primitives.world.RPICalc;
import org.roboticsapi.facet.runtime.rpi.world.types.RPIFrame;

public class JOrientationPreservingProject extends JPrimitive {
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

	private final MutableVector _f = RPICalc.vectorCreate();
	private final MutableVector _f1 = RPICalc.vectorCreate();
	private final MutableVector _f2 = RPICalc.vectorCreate();
	private final MutableRotation _frot = RPICalc.rotationCreate();
	private final MutableVector _znorm = RPICalc.vectorCreate();
	private final MutableVector _tmp = RPICalc.vectorCreate();
	private final MutableVector _z = RPICalc.vectorCreate();

	private RPIFrame project(RPIFrame flange, RPIFrame motionCenter, RPIFrame ret) {
		RPICalc.rpiToVector(flange.getPos(), _f);
		RPICalc.rpiToRotation(flange.getRot(), _frot);
		// flange position vector in XY plane
		_f2.setZ(0);
		// flange orientation unit Z vector in XY plane
		_z.set(_frot.getQ02(), _frot.getQ12(), 0);

		// v points up or down - everything's fine
		if (_z.getLength() < 0.001)
			return flange;

		// _f and v are parallel - everything's fine
		_f2.setZ(0);
		_f.crossTo(_z, _tmp);
		if (_tmp.getLength() < 0.001)
			return flange;

		_z.normalizeTo(_znorm);
		double len = _f.getLength();
		if (len > 0.4)
			len = 0.4;
		if (len < 0.1)
			len = 0.1;
		_znorm.scaleTo(len, _f1);
		_f1.add(_f);
		_znorm.scaleTo(-len, _f2);
		_f2.add(_f);
		if (_f1.getLength() < _f2.getLength()) {
			_znorm.scaleTo(-len, _f);
		} else {
			_znorm.scaleTo(len, _f);
		}
		_f.setZ(flange.getPos().getZ().get());
		RPICalc.rotationToRpi(_frot, ret.getRot());
		RPICalc.vectorToRpi(_f, ret.getPos());
		return ret;
	}

}
