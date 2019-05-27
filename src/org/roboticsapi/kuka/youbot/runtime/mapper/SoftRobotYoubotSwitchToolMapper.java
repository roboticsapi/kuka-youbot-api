/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2010-2017 ISSE, University of Augsburg 
 */

package org.roboticsapi.kuka.youbot.runtime.mapper;

import org.roboticsapi.core.DeviceParameterBag;
import org.roboticsapi.kuka.youbot.action.SwitchToolAction;
import org.roboticsapi.robot.parameter.RobotTool;
import org.roboticsapi.runtime.SoftRobotRuntime;
import org.roboticsapi.runtime.core.primitives.BooleanValue;
import org.roboticsapi.runtime.core.primitives.DoubleValue;
import org.roboticsapi.runtime.mapping.MappingException;
import org.roboticsapi.runtime.mapping.net.DataflowOutPort;
import org.roboticsapi.runtime.mapping.net.DoubleDataflow;
import org.roboticsapi.runtime.mapping.net.NetFragment;
import org.roboticsapi.runtime.mapping.net.StateDataflow;
import org.roboticsapi.runtime.mapping.parts.ActionMapper;
import org.roboticsapi.runtime.mapping.parts.ActionMappingContext;
import org.roboticsapi.runtime.mapping.result.ActionMapperResult;
import org.roboticsapi.runtime.mapping.result.impl.BaseActionMapperResult;
import org.roboticsapi.runtime.rpi.RPIException;
import org.roboticsapi.runtime.world.dataflow.RelationDataflow;
import org.roboticsapi.runtime.world.dataflow.VectorDataflow;
import org.roboticsapi.runtime.world.primitives.FrameValue;
import org.roboticsapi.runtime.world.primitives.VectorFromXYZ;
import org.roboticsapi.runtime.world.types.RPIFrame;
import org.roboticsapi.world.Frame;

public class SoftRobotYoubotSwitchToolMapper implements ActionMapper<SoftRobotRuntime, SwitchToolAction> {

	@Override
	public ActionMapperResult map(SoftRobotRuntime runtime, SwitchToolAction action, DeviceParameterBag parameters,
			ActionMappingContext ports) throws MappingException, RPIException {

		RobotTool tool = action.getRobotTool();

		NetFragment fragment = new NetFragment("SwitchToolAction");

		DoubleValue mass = fragment.add(new DoubleValue(tool.getMass()));
		DataflowOutPort massPort = fragment.addOutPort(new DoubleDataflow(), false, mass.getOutValue());

		Frame comFrame = tool.getCenterOfMass();
		FrameValue transformation = fragment.add(new FrameValue(new RPIFrame()));
		DataflowOutPort comPort = fragment.addOutPort(new RelationDataflow(comFrame, comFrame), false,
				transformation.getOutValue());

		VectorFromXYZ moi = fragment.add(new VectorFromXYZ(tool.getJx(), tool.getJy(), tool.getJz()));
		DataflowOutPort moiPort = fragment.addOutPort(new VectorDataflow(), false, moi.getOutValue());

		BooleanValue completed = fragment.add(new BooleanValue(true));

		ToolActionResult result = new ToolActionResult(comPort, moiPort, massPort);

		return new BaseActionMapperResult(action, fragment, result,
				fragment.addOutPort(new StateDataflow(), false, completed.getOutValue()));
	}
}
