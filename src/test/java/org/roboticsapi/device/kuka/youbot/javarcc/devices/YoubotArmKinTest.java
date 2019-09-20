/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2013-2019 ISSE, University of Augsburg 
 */

package org.roboticsapi.device.kuka.youbot.javarcc.devices;

import org.junit.Assert;
import org.junit.Test;
import org.roboticsapi.facet.runtime.rpi.world.types.RPIFrame;

public class YoubotArmKinTest {

	@Test
	public void testNearPositiveLimit() {
		YoubotArmKin kin = new YoubotArmKin();
		double[] joints = { 2.914, 1.535, 2.513, 1.754, 2.888 };
		RPIFrame pos = new RPIFrame();
		kin.kin(joints, pos);
		double[] ret = kin.invKin(pos, joints);
		Assert.assertArrayEquals(joints, ret, 1e-3);
	}

}
