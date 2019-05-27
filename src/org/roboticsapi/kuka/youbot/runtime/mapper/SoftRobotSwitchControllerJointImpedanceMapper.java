/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2010-2017 ISSE, University of Augsburg 
 */

package org.roboticsapi.kuka.youbot.runtime.mapper;

import org.roboticsapi.core.DeviceParameterBag;
import org.roboticsapi.kuka.youbot.action.SwitchControllerAction;
import org.roboticsapi.kuka.youbot.controller.JointImpedanceController;
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

public class SoftRobotSwitchControllerJointImpedanceMapper
		implements ActionMapper<SoftRobotRuntime, org.roboticsapi.kuka.youbot.action.SwitchControllerAction> {

	@Override
	public ActionMapperResult map(SoftRobotRuntime runtime, SwitchControllerAction action,
			DeviceParameterBag parameters, ActionMappingContext ports) throws MappingException, RPIException {
		if (action.getController() == null)
			return null;
		if (action.getController().getClass() != JointImpedanceController.class)
			return null;

		JointImpedanceController ji = (JointImpedanceController) action.getController();

		NetFragment fragment = new NetFragment("JointImpedance");

		DataflowOutPort[] stiffnessPorts = new DataflowOutPort[ji.getJointCount()];
		DataflowOutPort[] dampingPorts = new DataflowOutPort[ji.getJointCount()];
		DataflowOutPort[] addTorquePorts = new DataflowOutPort[ji.getJointCount()];
		double[] maxTorques = new double[ji.getJointCount()];

		for (int i = 0; i < ji.getJointCount(); i++) {
			DoubleValue stiffness = new DoubleValue(ji.getImpedanceSettings(i).getStiffness());
			fragment.add(stiffness);
			DataflowOutPort dataflowOutPort = new DataflowOutPort(new DoubleDataflow(), stiffness.getOutValue());
			fragment.addOutPort(dataflowOutPort);
			stiffnessPorts[i] = dataflowOutPort;

			DoubleValue damping = fragment.add(new DoubleValue(ji.getImpedanceSettings(i).getDamping()));
			dampingPorts[i] = fragment.addOutPort(new DoubleDataflow(), false, damping.getOutValue());

			DoubleValue addTorque = fragment.add(new DoubleValue(ji.getImpedanceSettings(i).getAddTorque()));
			addTorquePorts[i] = fragment.addOutPort(new DoubleDataflow(), false, addTorque.getOutValue());
			maxTorques[i] = ji.getImpedanceSettings(i).getMaxTorque();
		}

		BooleanValue completed = fragment.add(new BooleanValue(true));

		JointImpedanceActionResult result = new JointImpedanceActionResult(null, 5, stiffnessPorts, dampingPorts,
				addTorquePorts, maxTorques);

		return new BaseActionMapperResult(action, fragment, result,
				fragment.addOutPort(new StateDataflow(), false, completed.getOutValue()));
	}
}
