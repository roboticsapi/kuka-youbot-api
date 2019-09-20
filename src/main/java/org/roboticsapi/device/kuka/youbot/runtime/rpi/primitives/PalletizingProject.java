package org.roboticsapi.device.kuka.youbot.runtime.rpi.primitives;

import org.roboticsapi.facet.runtime.rpi.InPort;
import org.roboticsapi.facet.runtime.rpi.OutPort;
import org.roboticsapi.facet.runtime.rpi.Primitive;

/**
 * Projects a transformation so that the Z direction points upwards or downwards
 * (respecting motion center)
 */
public class PalletizingProject extends Primitive {
	/** Type name of the primitive */
	public static final String PRIMITIVE_TYPE = "KUKA::youBot::PalletizingProject";

	/** Flange position */
	private final InPort inFlange = new InPort("inFlange");

	/** Motion center position (relative to flange) */
	private final InPort inMotionCenter = new InPort("inMotionCenter");

	/** Projected flange transformation */
	private final OutPort outValue = new OutPort("outValue");

	public PalletizingProject() {
		super(PRIMITIVE_TYPE);

		// Add all ports
		add(inFlange);
		add(inMotionCenter);
		add(outValue);

		// Add all parameters
	}

	/**
	 * Flange position
	 *
	 * @return the input port of the block
	 */
	public final InPort getInFlange() {
		return this.inFlange;
	}

	/**
	 * Motion center position (relative to flange)
	 *
	 * @return the input port of the block
	 */
	public final InPort getInMotionCenter() {
		return this.inMotionCenter;
	}

	/**
	 * Projected flange transformation
	 * 
	 * @return the output port of the block
	 */
	public final OutPort getOutValue() {
		return this.outValue;
	}

}
