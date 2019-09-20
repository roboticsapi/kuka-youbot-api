/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2013-2019 ISSE, University of Augsburg 
 */

package org.roboticsapi.device.kuka.youbot.simulation;

import java.rmi.RemoteException;
import java.util.Random;

import org.roboticsapi.configuration.ConfigurationProperty;
import org.roboticsapi.core.Dependency;
import org.roboticsapi.core.world.mutable.MutableTransformation;
import org.roboticsapi.device.kuka.youbot.javarcc.devices.YoubotPlatformKin;
import org.roboticsapi.facet.javarcc.primitives.world.RPICalc;
import org.roboticsapi.facet.javarcc.simulation.AbstractSimulationItem;
import org.roboticsapi.facet.javarcc.simulation.SimulationHelper;
import org.roboticsapi.facet.runtime.rpi.world.types.RPIFrame;
import org.roboticsapi.facet.simulation.SEntity;
import org.roboticsapi.framework.multijoint.javarcc.devices.SIVelocityControlledMultijoint;
import org.roboticsapi.framework.multijoint.simulation.SVelocityControlledAxis;

public class SYoubotPlatform extends SEntity {
	private YoubotPlatformKin kin = new YoubotPlatformKin();
	double frontOffset = 0.228d;
	double leftOffset = 0.158d;
	double heightOffset = -0.034d;
	final double axisWeight = 0.1;

	private final Dependency<Double> startX, startY, startZ, startA;

	public SYoubotPlatform() {
		startX = createDependency("startX");
		startY = createDependency("startY");
		startZ = createDependency("startZ");
		startA = createDependency("startA");
	}

	public double getStartX() {
		return startX.get();
	}

	@ConfigurationProperty
	public void setStartX(double startX) {
		this.startX.set(startX);
	}

	public double getStartY() {
		return startY.get();
	}

	@ConfigurationProperty
	public void setStartY(double startY) {
		this.startY.set(startY);
	}

	public double getStartZ() {
		return startZ.get();
	}

	@ConfigurationProperty
	public void setStartZ(double startZ) {
		this.startZ.set(startZ);
	}

	public double getStartA() {
		return startA.get();
	}

	@ConfigurationProperty
	public void setStartA(double startA) {
		this.startA.set(startA);
	}

	SVelocityControlledAxis fl = new SVelocityControlledAxis(this,
			new MutableTransformation(frontOffset, leftOffset, heightOffset, -Math.PI / 2d, -Math.PI / 2d, 0d), 0,
			axisWeight);
	SVelocityControlledAxis fr = new SVelocityControlledAxis(this,
			new MutableTransformation(frontOffset, -leftOffset, heightOffset, -Math.PI / 2d, -Math.PI / 2d, 0d), 0,
			axisWeight);
	SVelocityControlledAxis rl = new SVelocityControlledAxis(this,
			new MutableTransformation(-frontOffset, leftOffset, heightOffset, -Math.PI / 2d, -Math.PI / 2d, 0d), 0,
			axisWeight);
	SVelocityControlledAxis rr = new SVelocityControlledAxis(this,
			new MutableTransformation(-frontOffset, -leftOffset, heightOffset, -Math.PI / 2d, -Math.PI / 2d, 0d), 0,
			axisWeight);
	SVelocityControlledAxis axes[] = { fl, fr, rl, rr };
	double flp, frp, rlp, rrp;
	MutableTransformation trans = new MutableTransformation();

	@Override
	public MutableTransformation getRelativePosition() {
		return trans;
	}

	private class VelocityControlledMultijoint extends AbstractSimulationItem
			implements SIVelocityControlledMultijoint {
		private static final long serialVersionUID = 6592611832908261554L;

		protected VelocityControlledMultijoint() throws RemoteException {
			super();
		}

		@Override
		public double getMeasuredJointPosition(int joint) throws RemoteException {
			return axes[joint].getMeasuredJointPosition();
		}

		@Override
		public double getMeasuredJointVelocity(int joint) throws RemoteException {
			return axes[joint].getMeasuredJointVelocity();
		}

		@Override
		public void setJointVelocity(int joint, double velocity) throws RemoteException {
			axes[joint].setJointVelocity(velocity);
		}
	}

	@Override
	protected void afterInitialization() {
		super.afterInitialization();
		getWorld().addEntity(fl);
		getWorld().addEntity(fr);
		getWorld().addEntity(rl);
		getWorld().addEntity(rr);
		SimulationHelper.registerSimuationItem(getIdentifier(), () -> new VelocityControlledMultijoint());
		trans.setVectorEuler(startX.get(), startY.get(), startZ.get() + 0.084d, startA.get(), 0, 0);
	}

	@Override
	protected void beforeUninitialization() {
		SimulationHelper.unregisterSimulationItem(getIdentifier());
		getWorld().removeEntity(fl);
		getWorld().removeEntity(fr);
		getWorld().removeEntity(rl);
		getWorld().removeEntity(rr);
		super.beforeUninitialization();
	}

	@Override
	public double getSimulationHz() {
		return 100;
	}

	MutableTransformation _kin = RPICalc.frameCreate();
	RPIFrame _rpikin = RPICalc.rpiFrameCreate();

	@Override
	public void simulateStep(Long time) {
		kin.posKin(noise(fl.getMeasuredJointPosition() - flp), noise(fr.getMeasuredJointPosition() - frp),
				noise(rl.getMeasuredJointPosition() - rlp), noise(rr.getMeasuredJointPosition() - rrp), _kin);
		RPICalc.frameToRpi(_kin, _rpikin);
		flp = fl.getMeasuredJointPosition();
		frp = fr.getMeasuredJointPosition();
		rlp = rl.getMeasuredJointPosition();
		rrp = rr.getMeasuredJointPosition();
		trans.multiply(new MutableTransformation(_rpikin.getPos().getX().get(), _rpikin.getPos().getY().get(),
				_rpikin.getPos().getZ().get(), _rpikin.getRot().getA().get(), _rpikin.getRot().getB().get(),
				_rpikin.getRot().getC().get()));
	}

	Random random = new Random();

	private double noise(double value) {
		return value * (random.nextGaussian() * 0.1 + 1);
	}

}
