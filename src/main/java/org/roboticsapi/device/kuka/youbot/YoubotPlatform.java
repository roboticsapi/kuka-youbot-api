/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2013-2019 ISSE, University of Augsburg 
 */

package org.roboticsapi.device.kuka.youbot;

import org.roboticsapi.configuration.ConfigurationProperty;
import org.roboticsapi.core.Dependency;
import org.roboticsapi.core.InvalidParametersException;
import org.roboticsapi.core.world.Frame;
import org.roboticsapi.core.world.Rotation;
import org.roboticsapi.core.world.Transformation;
import org.roboticsapi.core.world.relation.StaticConnection;
import org.roboticsapi.core.world.relation.StaticPosition;
import org.roboticsapi.framework.cartesianmotion.parameter.CartesianParameters;
import org.roboticsapi.framework.cartesianmotion.parameter.FrameProjectorParameter;
import org.roboticsapi.framework.platform.AbstractMecanumPlatform;

/**
 * Implementation for the mobile Mecanum platform of a KUKA youBot.
 */
public final class YoubotPlatform extends AbstractMecanumPlatform {

	private final Dependency<Frame> frontSensorMountFrame;
	private final Dependency<Frame> rearSensorMountFrame;
	private final Dependency<Frame> topSensorMountFrame;
	private final Dependency<Frame> frontArmMountFrame;
	private final Dependency<Frame> rearArmMountFrame;

	/**
	 * Constructor.
	 */
	public YoubotPlatform() {
		frontSensorMountFrame = createFrameProperty("frontSensorMountFrame", "Front Sensor Mount Frame");
		rearSensorMountFrame = createFrameProperty("rearSensorMountFrame", "Rear Sensor Mount Frame");
		topSensorMountFrame = createFrameProperty("topSensorMountFrame", "Top Sensor Mount Frame");
		frontArmMountFrame = createFrameProperty("frontArmMountFrame", "Front Arm Mount Frame");
		rearArmMountFrame = createFrameProperty("rearArmMountFrame", "Rear Arm Mount Frame");

		createConnectionProperty("frontSensorMountFrameConnection", new Transformation(0.33, 0, -0.068, 0, 0, 0),
				frontSensorMountFrame);
		createConnectionProperty("rearSensorMountFrameConnection", new Transformation(-0.33, 0, -0.068, Math.PI, 0, 0),
				rearSensorMountFrame);
		createConnectionProperty("topSensorMountFrameConnection", new Transformation(-0.159, 0, 0.046, 0, 0, 0),
				topSensorMountFrame);
		createConnectionProperty("frontArmMountFrameConnection", new Transformation(0.167, 0, 0.014, 0, 0, 0),
				frontArmMountFrame);
		createConnectionProperty("rearArmMountFrameConnection", new Transformation(-0.167, 0, 0.014, Math.PI, 0, 0),
				rearArmMountFrame);
		createDependency("odometryConnection",
				() -> new MecanumPlatformConnection(getOdometryOrigin(), getOdometryFrame()));
	}

	private Dependency<Frame> createFrameProperty(String name, final String frameNameSuffix) {
		return createDependency(name, new Dependency.Builder<Frame>() {
			@Override
			public Frame create() {
				return new Frame(getName() + " " + frameNameSuffix);
			}
		});
	}

	private void createConnectionProperty(String name, final Transformation trans, final Dependency<Frame> to) {
		createDependency(name, new Dependency.Builder<StaticConnection>() {
			public StaticConnection create() {
				return new StaticConnection(getBase(), to.get());
			}
		});
		createDependency(name + " position", new Dependency.Builder<StaticPosition>() {
			public StaticPosition create() {
				return new StaticPosition(getBase(), to.get(), trans);
			}
		});
	}

	@ConfigurationProperty(Optional = true)
	public void setFrontSensorMountFrame(Frame frontSensorMountFrame) {
		this.frontSensorMountFrame.set(frontSensorMountFrame);
	}

	public Frame getFrontSensorMountFrame() {
		return frontSensorMountFrame.get();
	}

	@ConfigurationProperty(Optional = true)
	public void setRearSensorMountFrame(Frame rearSensorMountFrame) {
		this.rearSensorMountFrame.set(rearSensorMountFrame);
	}

	public Frame getRearSensorMountFrame() {
		return rearSensorMountFrame.get();
	}

	@ConfigurationProperty(Optional = true)
	public void setTopSensorMountFrame(Frame topSensorMountFrame) {
		this.topSensorMountFrame.set(topSensorMountFrame);
	}

	public Frame getTopSensorMountFrame() {
		return topSensorMountFrame.get();
	}

	@ConfigurationProperty(Optional = true)
	public void setFrontArmMountFrame(Frame frontArmMountFrame) {
		this.frontArmMountFrame.set(frontArmMountFrame);
	}

	public Frame getFrontArmMountFrame() {
		return frontArmMountFrame.get();
	}

	@ConfigurationProperty(Optional = true)
	public void setRearArmMountFrame(Frame rearArmMountFrame) {
		this.rearArmMountFrame.set(rearArmMountFrame);
	}

	public Frame getRearArmMountFrame() {
		return rearArmMountFrame.get();
	}

	@Override
	protected void defineMaximumParameters() throws InvalidParametersException {
		// TODO Auto-generated method stub
	}

	@Override
	protected void defineMoreDefaultParameters() throws InvalidParametersException {
		addDefaultParameters(new CartesianParameters(0.8, 0.8, Double.POSITIVE_INFINITY, 0.8, 0.8,
				Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));

		addDefaultParameters(new FrameProjectorParameter(new YoubotPlatformPositionPreservingFrameProjector(this)));
	}

	@Override
	protected Transformation getBaseTransformation() {
		return new Transformation(0d, 0d, 0.084d, 0d, 0d, 0d);
	}

	@Override
	protected double getFrontOffset() {
		return 0.228d;
	}

	@Override
	protected double getLeftOffset() {
		return 0.158d;
	}

	@Override
	protected double getHeightOffset() {
		return -0.034d;
	}

	@Override
	protected Rotation getWheelOrientation() {
		return new Rotation(-Math.PI / 2d, -Math.PI / 2d, 0d);
	}

}
