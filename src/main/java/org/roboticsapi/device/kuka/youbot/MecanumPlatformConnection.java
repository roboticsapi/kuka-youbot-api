/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2013-2019 ISSE, University of Augsburg 
 */

package org.roboticsapi.device.kuka.youbot;

import java.util.HashMap;
import java.util.Map;

import org.roboticsapi.core.realtimevalue.realtimedouble.RealtimeDouble;
import org.roboticsapi.core.world.AbstractStateVariableExpression;
import org.roboticsapi.core.world.Frame;
import org.roboticsapi.core.world.FrameTopology;
import org.roboticsapi.core.world.StateVariable;
import org.roboticsapi.core.world.TransformationException;
import org.roboticsapi.core.world.realtimevalue.realtimetransformation.RealtimeTransformation;
import org.roboticsapi.core.world.realtimevalue.realtimetwist.RealtimeTwist;
import org.roboticsapi.core.world.relation.DynamicConnection;

public class MecanumPlatformConnection extends DynamicConnection {
	StateVariable x = addPositionStateVariable("x"), y = addPositionStateVariable("y"),
			a = addPositionStateVariable("a");
	StateVariable forward = addVelocityStateVariable("forward"), left = addVelocityStateVariable("left"),
			turn = addVelocityStateVariable("turn");

	public MecanumPlatformConnection(Frame from, Frame to) {
		super(from, to);
		a.addFlowExpression(turn.asExpression());
		x.addFlowExpression(new AbstractStateVariableExpression(a, forward, left) {
			@Override
			public RealtimeDouble getRealtimeValue(Map<StateVariable, RealtimeDouble> var) {
				return var.get(a).cos().multiply(var.get(forward))
						.add(var.get(a).sin().multiply(var.get(left).negate()));
			}
		});
		y.addFlowExpression(new AbstractStateVariableExpression(a, forward, left) {
			@Override
			public RealtimeDouble getRealtimeValue(Map<StateVariable, RealtimeDouble> var) {
				return var.get(a).sin().multiply(var.get(forward)).add(var.get(a).cos().multiply(var.get(left)));
			}
		});
	}

	@Override
	public RealtimeTransformation getTransformationForState(Map<StateVariable, RealtimeDouble> state) {
		return RealtimeTransformation.createFromXYZABC(state.get(x), state.get(y), RealtimeDouble.ZERO, state.get(a),
				RealtimeDouble.ZERO, RealtimeDouble.ZERO);
	}

	@Override
	public RealtimeTwist getTwistForState(Map<StateVariable, RealtimeDouble> state) {
		RealtimeTwist localTwist = RealtimeTwist.createFromLinearAngular(state.get(forward), state.get(left),
				RealtimeDouble.ZERO, RealtimeDouble.ZERO, RealtimeDouble.ZERO, state.get(turn));
		RealtimeTransformation trans = getTransformationForState(state);
		return RealtimeTwist.createFromLinearAngular(localTwist.getTranslationVelocity().transform(trans.getRotation()),
				localTwist.getRotationVelocity().transform(trans.getRotation()));
	}

	@Override
	public Map<StateVariable, RealtimeDouble> getStateForTransformation(RealtimeTransformation trans,
			FrameTopology topology) throws TransformationException {
		Map<StateVariable, RealtimeDouble> ret = new HashMap<>();
		ret.put(x, trans.getX());
		ret.put(y, trans.getY());
		ret.put(a, trans.getA());
		return ret;
	}

	@Override
	public Map<StateVariable, RealtimeDouble> getStateForTwist(RealtimeTransformation pose, RealtimeTwist velocity,
			FrameTopology topology) throws TransformationException {
		Map<StateVariable, RealtimeDouble> ret = new HashMap<>();
		RealtimeTwist globalVel = velocity;
		RealtimeTwist localVel = RealtimeTwist.createFromLinearAngular(
				globalVel.getTranslationVelocity().transform(pose.getRotation().invert()),
				globalVel.getRotationVelocity().transform(pose.getRotation().invert()));

		ret.put(forward, localVel.getTranslationVelocity().getX());
		ret.put(left, localVel.getTranslationVelocity().getY());
		ret.put(turn, localVel.getRotationVelocity().getZ());
		return ret;
	}
}