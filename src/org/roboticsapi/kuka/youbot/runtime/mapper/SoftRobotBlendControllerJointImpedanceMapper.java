/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2010-2017 ISSE, University of Augsburg 
 */

package org.roboticsapi.kuka.youbot.runtime.mapper;

import org.roboticsapi.core.DeviceParameterBag;
import org.roboticsapi.kuka.youbot.action.BlendControllerAction;
import org.roboticsapi.kuka.youbot.controller.JointImpedanceController;
import org.roboticsapi.kuka.youbot.controller.JointImpedanceSettings;
import org.roboticsapi.multijoint.parameter.Controller;
import org.roboticsapi.runtime.SoftRobotRuntime;
import org.roboticsapi.runtime.core.primitives.BooleanNot;
import org.roboticsapi.runtime.core.primitives.Clock;
import org.roboticsapi.runtime.core.primitives.Interval;
import org.roboticsapi.runtime.core.primitives.Lerp;
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

public class SoftRobotBlendControllerJointImpedanceMapper
		implements ActionMapper<SoftRobotRuntime, org.roboticsapi.kuka.youbot.action.BlendControllerAction> {

	@Override
	public ActionMapperResult map(SoftRobotRuntime runtime, BlendControllerAction action, DeviceParameterBag parameters,
			ActionMappingContext ports) throws MappingException, RPIException {

		Controller startCon = action.getStartController();
		Controller endCon = action.getEndController();

		if (startCon.getClass() != JointImpedanceController.class
				|| endCon.getClass() != JointImpedanceController.class) {
			return null;
		}

		JointImpedanceController sc = (JointImpedanceController) startCon;
		JointImpedanceController ec = (JointImpedanceController) endCon;

		if (sc.getJointCount() != ec.getJointCount()) {
			throw new MappingException("Controller Joint Count are not equal.");
		}

		NetFragment fragment = new NetFragment("BlendController");

		Clock clock = fragment.add(new Clock());
		Interval interval = fragment.add(new Interval(0.0, action.getDuration()));
		fragment.connect(clock.getOutValue(), interval.getInValue());

		BooleanNot completed = fragment.add(new BooleanNot());
		fragment.connect(interval.getOutActive(), completed.getInValue());

		DataflowOutPort[] stiffnessPorts = new DataflowOutPort[sc.getJointCount()];
		DataflowOutPort[] dampingPorts = new DataflowOutPort[sc.getJointCount()];
		DataflowOutPort[] addTorquePorts = new DataflowOutPort[sc.getJointCount()];
		double[] maxTorques = new double[ec.getJointCount()];

		JointImpedanceSettings startImpParam, endImpParam;

		for (int i = 0; i < sc.getJointCount(); i++) {

			startImpParam = sc.getImpedanceSettings(i);
			endImpParam = ec.getImpedanceSettings(i);

			Lerp stiffScale = fragment.add(new Lerp(startImpParam.getStiffness(), endImpParam.getStiffness()));
			Lerp dampScale = fragment.add(new Lerp(startImpParam.getDamping(), endImpParam.getDamping()));
			Lerp addTorqueScale = fragment.add(new Lerp(startImpParam.getAddTorque(), endImpParam.getAddTorque()));

			fragment.connect(interval.getOutValue(), stiffScale.getInAmount());
			fragment.connect(interval.getOutValue(), dampScale.getInAmount());
			fragment.connect(interval.getOutValue(), addTorqueScale.getInAmount());

			stiffnessPorts[i] = fragment.addOutPort(new DoubleDataflow(), false, stiffScale.getOutValue());
			dampingPorts[i] = fragment.addOutPort(new DoubleDataflow(), false, dampScale.getOutValue());
			addTorquePorts[i] = fragment.addOutPort(new DoubleDataflow(), false, addTorqueScale.getOutValue());

			maxTorques[i] = ec.getImpedanceSettings(i).getMaxTorque();
		}

		JointImpedanceActionResult result = new JointImpedanceActionResult(null, 5, stiffnessPorts, dampingPorts,
				addTorquePorts, maxTorques);

		return new BaseActionMapperResult(action, fragment, result,
				fragment.addOutPort(new StateDataflow(), false, completed.getOutValue()));
	}

}
