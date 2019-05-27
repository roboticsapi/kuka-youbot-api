/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2010-2017 ISSE, University of Augsburg 
 */

package org.roboticsapi.kuka.youbot.runtime.mapper;

import org.roboticsapi.core.DeviceParameterBag;
import org.roboticsapi.kuka.youbot.action.SwitchControllerAction;
import org.roboticsapi.kuka.youbot.controller.PositionController;
import org.roboticsapi.runtime.SoftRobotRuntime;
import org.roboticsapi.runtime.core.primitives.BooleanValue;
import org.roboticsapi.runtime.mapping.MappingException;
import org.roboticsapi.runtime.mapping.net.NetFragment;
import org.roboticsapi.runtime.mapping.net.StateDataflow;
import org.roboticsapi.runtime.mapping.parts.ActionMapper;
import org.roboticsapi.runtime.mapping.parts.ActionMappingContext;
import org.roboticsapi.runtime.mapping.result.ActionMapperResult;
import org.roboticsapi.runtime.mapping.result.impl.BaseActionMapperResult;
import org.roboticsapi.runtime.rpi.RPIException;

public class SoftRobotSwitchControllerPositionMapper
		implements ActionMapper<SoftRobotRuntime, org.roboticsapi.kuka.youbot.action.SwitchControllerAction> {

	@Override
	public ActionMapperResult map(SoftRobotRuntime runtime, SwitchControllerAction action,
			DeviceParameterBag parameters, ActionMappingContext ports) throws MappingException, RPIException {
		if (action.getController() == null)
			return null;
		if (action.getController().getClass() != PositionController.class)
			return null;

		NetFragment fragment = new NetFragment("PositionController");

		BooleanValue completed = fragment.add(new BooleanValue(true));

		PositionControllerActionResult result = new PositionControllerActionResult(null);

		return new BaseActionMapperResult(action, fragment, result,
				fragment.addOutPort(new StateDataflow(), false, completed.getOutValue()));
	}

}
