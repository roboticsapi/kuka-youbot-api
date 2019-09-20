/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2013-2019 ISSE, University of Augsburg 
 */

package org.roboticsapi.device.kuka.youbot.javarcc.devices;

import org.roboticsapi.core.util.RAPILogger;
import org.roboticsapi.core.world.mutable.MutableRotation;
import org.roboticsapi.core.world.mutable.MutableTransformation;
import org.roboticsapi.core.world.mutable.MutableTwist;
import org.roboticsapi.core.world.mutable.MutableVector;
import org.roboticsapi.facet.javarcc.devices.JDevice;
import org.roboticsapi.facet.javarcc.devices.PeriodicJDevice;
import org.roboticsapi.facet.javarcc.primitives.world.RPICalc;
import org.roboticsapi.facet.javarcc.simulation.SimulationHelper;
import org.roboticsapi.facet.runtime.rpi.world.types.RPIFrame;
import org.roboticsapi.facet.runtime.rpi.world.types.RPITwist;
import org.roboticsapi.framework.cartesianmotion.javarcc.interfaces.JCartesianPositionInterface;
import org.roboticsapi.framework.multijoint.javarcc.devices.SIVelocityControlledMultijoint;
import org.roboticsapi.framework.platform.javarcc.interfaces.JRobotBaseInterface;

public class JYoubotPlatform extends PeriodicJDevice
		implements JDevice, JRobotBaseInterface, JCartesianPositionInterface {
	private final YoubotPlatformKin kin = new YoubotPlatformKin();
	private final RPIFrame msrRpiPos = RPICalc.rpiFrameCreate(), cmdRpiPos = RPICalc.rpiFrameCreate();
	private final RPITwist msrRpiVel = RPICalc.rpiTwistCreate(), cmdRpiVel = RPICalc.rpiTwistCreate();
	MutableTransformation msrPos = RPICalc.frameCreate();
	private final MutableTransformation rpiPos = RPICalc.frameCreate();
	MutableTwist msrVel = RPICalc.twistCreate();
	private final MutableTwist rpiVel = RPICalc.twistCreate();
	private final double[] lastWheelPos = new double[4], lastWheelVel = new double[4];
	private final MutableTransformation _kin = RPICalc.frameCreate();
	private final MutableTransformation _err = RPICalc.frameCreate();
	private final MutableVector _abc = RPICalc.vectorCreate();
	private final double[] _wheelVel = new double[4];
	private final MutableTransformation _newPos = RPICalc.frameCreate();
	private final MutableTwist _twist = RPICalc.twistCreate();
	private final MutableRotation _rot = RPICalc.rotationCreate();

	@Override
	public void doPeriodicTask() {
		enterCriticalSection();
		// calculate position
		{
			double fl = SimulationHelper.callSimulationItem(sim, () -> sim.getMeasuredJointPosition(0), 0d);
			double fr = SimulationHelper.callSimulationItem(sim, () -> sim.getMeasuredJointPosition(1), 0d);
			double rl = SimulationHelper.callSimulationItem(sim, () -> sim.getMeasuredJointPosition(2), 0d);
			double rr = SimulationHelper.callSimulationItem(sim, () -> sim.getMeasuredJointPosition(3), 0d);

			kin.posKin(fl - lastWheelPos[0], fr - lastWheelPos[1], rl - lastWheelPos[2], rr - lastWheelPos[3], _kin);

			msrPos.multiply(_kin);

			lastWheelPos[0] = fl;
			lastWheelPos[1] = fr;
			lastWheelPos[2] = rl;
			lastWheelPos[3] = rr;
		}

		// calculate velocity
		{
			double fl = lastWheelVel[0] = SimulationHelper.callSimulationItem(sim,
					() -> sim.getMeasuredJointVelocity(0), 0d);
			double fr = lastWheelVel[1] = SimulationHelper.callSimulationItem(sim,
					() -> sim.getMeasuredJointVelocity(1), 0d);
			double rl = lastWheelVel[2] = SimulationHelper.callSimulationItem(sim,
					() -> sim.getMeasuredJointVelocity(2), 0d);
			double rr = lastWheelVel[3] = SimulationHelper.callSimulationItem(sim,
					() -> sim.getMeasuredJointVelocity(3), 0d);

			kin.velKin(fl, fr, rl, rr, msrVel);
		}

		// do control
		double velX, velY, velYaw;

		if (Double.isNaN(rpiPos.getTranslation().getX())) {
			RAPILogger.getLogger(this).warning("Goal position is NaN");
		}

		msrPos.invertTo(_err);
		_err.multiply(rpiPos);
		_err.getRotation().getEulerTo(_abc);
		double errX, errY, errYaw;
		errX = _err.getTranslation().getX();
		errY = _err.getTranslation().getY();
		errYaw = _abc.getZ();

		double px = 3, py = 3, pyaw = 3;

		velX = rpiVel.getTranslation().getX() + errX * px;
		velY = rpiVel.getTranslation().getY() + errY * py;
		velYaw = rpiVel.getRotation().getZ() + errYaw * pyaw;

		// limit maximum velocity
		double maxX = 0.8, maxY = 0.8, maxYaw = 0.8;
		double factor = 1;
		factor = Math.max(factor, Math.abs(velX) / maxX);
		factor = Math.max(factor, Math.abs(velY) / maxY);
		factor = Math.max(factor, Math.abs(velYaw) / maxYaw);
		velX = velX / factor;
		velY = velY / factor;
		velYaw = velYaw / factor;

		// command velocity
		kin.velInvKin(velX, velY, velYaw, _wheelVel);
		SimulationHelper.callSimulationItem(sim, () -> sim.setJointVelocity(0, _wheelVel[0]));
		SimulationHelper.callSimulationItem(sim, () -> sim.setJointVelocity(1, _wheelVel[1]));
		SimulationHelper.callSimulationItem(sim, () -> sim.setJointVelocity(2, _wheelVel[2]));
		SimulationHelper.callSimulationItem(sim, () -> sim.setJointVelocity(3, _wheelVel[3]));
		leaveCriticalSection();
	}

	public JYoubotPlatform(String name, String simulation) {
		super(0.02);
		sim = SimulationHelper.getSimulationItem(SIVelocityControlledMultijoint.class, simulation);
	}

	@Override
	public RPIFrame getMeasuredPosition() {
		RPICalc.frameToRpi(msrPos, msrRpiPos);
		return msrRpiPos;
	}

	@Override
	public RPITwist getMeasuredVelocity() {
		RPICalc.twistToRpi(msrVel, msrRpiVel);
		return msrRpiVel;
	}

	@Override
	public RPITwist getCommandedVelocity() {
		RPICalc.twistToRpi(rpiVel, cmdRpiVel);
		return cmdRpiVel;
	}

	@Override
	public RPIFrame getCommandedPosition() {
		RPICalc.frameToRpi(rpiPos, cmdRpiPos);
		return cmdRpiPos;
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
		} else if (pos.getPos().getX() != pos.getPos().getX() || pos.getPos().getY() != pos.getPos().getY()
				|| pos.getRot().getA() != pos.getRot().getA()) {
			return 1;
		}
		return 0;
	}

	private Long rpiTime;
	private SIVelocityControlledMultijoint sim;

	@Override
	public void setPosition(RPIFrame pos, Long time) {
		if (checkPosition(pos) != 0)
			return;
		if (rpiTime != null && rpiTime != time) {
			RPICalc.rpiToFrame(pos, _newPos);
			rpiPos.getDeltaTo(_newPos, (time - rpiTime) / 1e3, _twist);
			_newPos.getRotation().invertTo(_rot);
			_twist.getTranslation().rotateTo(_rot, rpiVel.getTranslation());
			_twist.getRotation().rotateTo(_rot, rpiVel.getRotation());
		}
		RPICalc.rpiToFrame(pos, rpiPos);
		rpiTime = time;
	}

	@Override
	public int getWheelCount() {
		return 4;
	}

	@Override
	public double getWheelPosition(int i) {
		return lastWheelPos[i];
	}

	@Override
	public double getWheelVelocity(int i) {
		return lastWheelVel[i];
	}

}
