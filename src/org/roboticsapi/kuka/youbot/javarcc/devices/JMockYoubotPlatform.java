/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2010-2017 ISSE, University of Augsburg 
 */

package org.roboticsapi.kuka.youbot.javarcc.devices;

import org.roboticsapi.runtime.cartesianmotion.javarcc.interfaces.JCartesianPositionInterface;
import org.roboticsapi.runtime.core.javarcc.devices.AbstractJDevice;
import org.roboticsapi.runtime.core.javarcc.devices.JDevice;
import org.roboticsapi.runtime.platform.javarcc.interfaces.JRobotBaseInterface;
import org.roboticsapi.runtime.world.javarcc.primitives.RPICalc;
import org.roboticsapi.runtime.world.types.RPIFrame;
import org.roboticsapi.runtime.world.types.RPITwist;
import org.roboticsapi.world.mutable.MutableRotation;
import org.roboticsapi.world.mutable.MutableTransformation;
import org.roboticsapi.world.mutable.MutableTwist;

public class JMockYoubotPlatform extends AbstractJDevice
		implements JDevice, JRobotBaseInterface, JCartesianPositionInterface {
	private final YoubotPlatformKin kin = new YoubotPlatformKin();
	private final double[] rpiWheelPos = new double[4], rpiWheelVel = new double[4];
	private final RPIFrame rpiPos = RPICalc.rpiFrameCreate();
	private final RPITwist rpiVel = RPICalc.rpiTwistCreate();
	private final RPITwist stopVel = RPICalc.rpiTwistCreate();
	private final MutableTransformation cmdPos = RPICalc.frameCreate();
	private final MutableTwist cmdVel = RPICalc.twistCreate();

	public JMockYoubotPlatform(String name) {
	}

	@Override
	public RPIFrame getMeasuredPosition() {
		return rpiPos;
	}

	@Override
	public RPITwist getMeasuredVelocity() {
		if (isMoving())
			return rpiVel;
		else
			return stopVel;
	}

	@Override
	public RPITwist getCommandedVelocity() {
		if (isMoving())
			return rpiVel;
		else
			return stopVel;
	}

	@Override
	public RPIFrame getCommandedPosition() {
		return rpiPos;
	}

	@Override
	public int getCartesianPositionDeviceError() {
		return 0;
	}

	@Override
	public int checkPosition(RPIFrame pos) {
		if (Math.abs(pos.getPos().getZ().get()) > 1e-3 || Math.abs(pos.getRot().getB().get()) > 1e-3
				|| Math.abs(pos.getRot().getC().get()) > 1e-3) {
			return 1;
		}
		return 0;
	}

	Long rpiTime;

	private final MutableRotation _rot = RPICalc.rotationCreate();
	private final MutableTransformation _newPos = RPICalc.frameCreate();
	private final MutableTwist _twist = RPICalc.twistCreate();

	@Override
	public void setPosition(RPIFrame pos, Long time) {
		if (rpiTime != null) {
			double dt = (time - rpiTime) / 1e3;
			RPICalc.rpiToFrame(pos, _newPos);
			_newPos.getRotation().invertTo(_rot);
			cmdPos.getDeltaTo(_newPos, dt, _twist);
			_twist.getTranslation().rotateTo(_rot, cmdVel.getTranslation());
			_twist.getRotation().rotateTo(_rot, cmdVel.getRotation());

			kin.velInvKin(rpiVel.getVel().getX().get(), rpiVel.getVel().getY().get(), rpiVel.getRot().getZ().get(),
					rpiWheelVel);
			for (int i = 0; i < 4; i++)
				rpiWheelPos[i] += rpiWheelVel[i] * dt;
		}

		RPICalc.rpiToFrame(pos, cmdPos);
		rpiTime = time;
		RPICalc.twistToRpi(cmdVel, rpiVel);
		RPICalc.frameToRpi(cmdPos, rpiPos);

	}

	@Override
	public int getWheelCount() {
		return 4;
	}

	@Override
	public double getWheelPosition(int i) {
		return rpiWheelPos[i];
	}

	@Override
	public double getWheelVelocity(int i) {
		if (isMoving())
			return rpiWheelVel[i];
		else
			return 0;

	}

	private boolean isMoving() {
		return isMoving(System.currentTimeMillis());
	}

	private boolean isMoving(long time) {
		return rpiTime != null && time - rpiTime < 100;
	}

}
