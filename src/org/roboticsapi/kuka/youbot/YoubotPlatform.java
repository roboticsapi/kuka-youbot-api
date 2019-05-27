/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2010-2017 ISSE, University of Augsburg 
 */

package org.roboticsapi.kuka.youbot;

import java.util.Map;

import org.roboticsapi.cartesianmotion.parameter.CartesianParameters;
import org.roboticsapi.cartesianmotion.parameter.FrameProjectorParameter;
import org.roboticsapi.configuration.ConfigurationProperty;
import org.roboticsapi.core.InvalidParametersException;
import org.roboticsapi.core.RoboticsObject;
import org.roboticsapi.core.exception.EntityException;
import org.roboticsapi.core.exception.InitializationException;
import org.roboticsapi.platform.AbstractMecanumPlatform;
import org.roboticsapi.platform.PlatformDriver;
import org.roboticsapi.world.Connection;
import org.roboticsapi.world.Frame;
import org.roboticsapi.world.Rotation;
import org.roboticsapi.world.StaticConnection;
import org.roboticsapi.world.Transformation;

/**
 * Implementation for the mobile Mecanum platform of a KUKA youBot.
 */
public final class YoubotPlatform extends AbstractMecanumPlatform<PlatformDriver> {

	private Frame frontSensorMountFrame;
	private Frame rearSensorMountFrame;
	private Frame topSensorMountFrame;
	private Frame frontArmMountFrame;
	private Frame rearArmMountFrame;

	/**
	 * Constructor.
	 */
	public YoubotPlatform() {
		super();
	}

	@ConfigurationProperty(Optional = true)
	public void setFrontSensorMountFrame(Frame frontSensorMountFrame) {
		immutableWhenInitialized();
		this.frontSensorMountFrame = frontSensorMountFrame;
	}

	public Frame getFrontSensorMountFrame() {
		return frontSensorMountFrame;
	}

	@ConfigurationProperty(Optional = true)
	public void setRearSensorMountFrame(Frame rearSensorMountFrame) {
		immutableWhenInitialized();
		this.rearSensorMountFrame = rearSensorMountFrame;
	}

	public Frame getRearSensorMountFrame() {
		return rearSensorMountFrame;
	}

	@ConfigurationProperty(Optional = true)
	public void setTopSensorMountFrame(Frame topSensorMountFrame) {
		immutableWhenInitialized();
		this.topSensorMountFrame = topSensorMountFrame;
	}

	public Frame getTopSensorMountFrame() {
		return topSensorMountFrame;
	}

	@ConfigurationProperty(Optional = true)
	public void setFrontArmMountFrame(Frame frontArmMountFrame) {
		immutableWhenInitialized();
		this.frontArmMountFrame = frontArmMountFrame;
	}

	public Frame getFrontArmMountFrame() {
		return frontArmMountFrame;
	}

	@ConfigurationProperty(Optional = true)
	public void setRearArmMountFrame(Frame rearArmMountFrame) {
		immutableWhenInitialized();
		this.rearArmMountFrame = rearArmMountFrame;
	}

	public Frame getRearArmMountFrame() {
		return rearArmMountFrame;
	}

	@Override
	protected void defineMaximumParameters() throws InvalidParametersException {
		// TODO Auto-generated method stub
	}

	@Override
	protected void defineMoreDefaultParameters() throws InvalidParametersException {
		addDefaultParameters(new CartesianParameters(0.8, 1, 0.8, 1));

		addDefaultParameters(new FrameProjectorParameter(new YoubotPlatformPositionPreservingFrameProjector(this)));
	}

	@Override
	protected void setupPlatformEntities() throws EntityException, InitializationException {
		// TODO: these values are guessed

		// sensor mount frames
		if (frontSensorMountFrame != null) {
			getBase().addRelation(new StaticConnection(new Transformation(0.33, 0, -0.068, 0, 0, 0)),
					frontSensorMountFrame);
		}

		if (rearSensorMountFrame != null) {
			getBase().addRelation(new StaticConnection(new Transformation(-0.33, 0, -0.068, Math.PI, 0, 0)),
					rearSensorMountFrame);
		}

		if (topSensorMountFrame != null) {
			getBase().addRelation(new StaticConnection(new Transformation(-0.159, 0, 0.046, 0, 0, 0)),
					topSensorMountFrame);
		}

		// arm mount frames
		if (frontArmMountFrame != null) {
			getBase().addRelation(new StaticConnection(new Transformation(0.167, 0, 0.014, 0, 0, 0)),
					frontArmMountFrame);
		}

		if (rearArmMountFrame != null) {
			getBase().addRelation(new StaticConnection(new Transformation(-0.167, 0, 0.014, Math.PI, 0, 0)),
					rearArmMountFrame);
		}
	}

	@Override
	protected void fillMoreAutomaticPlatformProperties(Map<String, RoboticsObject> createdObjects) {
		frontArmMountFrame = fill("frontArmMountFrame", frontArmMountFrame,
				new Frame(getName() + " Front Arm Mount Frame"), createdObjects);

		frontSensorMountFrame = fill("frontSensorMountFrame", frontSensorMountFrame,
				new Frame(getName() + " Front Sensor Mount Frame"), createdObjects);
		rearArmMountFrame = fill("rearArmMountFrame", rearArmMountFrame, new Frame(getName() + " Rear Arm Mount Frame"),
				createdObjects);
		rearSensorMountFrame = fill("rearSensorMountFrame", rearSensorMountFrame,
				new Frame(getName() + " Rear Sensor Mount Frame"), createdObjects);
		topSensorMountFrame = fill("topSensorMountFrame", topSensorMountFrame,
				new Frame(getName() + " Top Sensor Mount Frame"), createdObjects);
	}

	@Override
	protected void clearAutomaticPlatformProperties(Map<String, RoboticsObject> createdObjects) {
		frontArmMountFrame = clear("frontArmMountFrame", frontArmMountFrame, createdObjects);
		rearArmMountFrame = clear("rearArmMountFrame", rearArmMountFrame, createdObjects);

		frontSensorMountFrame = clear("frontSensorMountFrame", frontSensorMountFrame, createdObjects);
		rearSensorMountFrame = clear("rearSensorMountFrame", rearSensorMountFrame, createdObjects);
		topSensorMountFrame = clear("topSensorMountFrame", topSensorMountFrame, createdObjects);
	}

	@Override
	protected Connection createBaseConnection() {
		return new StaticConnection(new Transformation(0d, 0d, 0.084d, 0d, 0d, 0d));
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
