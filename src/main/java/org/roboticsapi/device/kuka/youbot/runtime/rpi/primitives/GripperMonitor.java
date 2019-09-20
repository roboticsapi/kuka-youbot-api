package org.roboticsapi.device.kuka.youbot.runtime.rpi.primitives;

import org.roboticsapi.facet.runtime.rpi.OutPort;
import org.roboticsapi.facet.runtime.rpi.Parameter;
import org.roboticsapi.facet.runtime.rpi.Primitive;
import org.roboticsapi.facet.runtime.rpi.core.types.RPIstring;

/**
 * Monitor the gripper position
 */
public class GripperMonitor extends Primitive {
	/** Type name of the primitive */
	public static final String PRIMITIVE_TYPE = "KUKA::youBot::GripperMonitor";

	/** Finger distance [m] */
	private final OutPort outDistance = new OutPort("outDistance");

	/** Name of youBot */
	private final Parameter<RPIstring> paramRobot = new Parameter<RPIstring>("Robot", new RPIstring(""));

	public GripperMonitor() {
		super(PRIMITIVE_TYPE);

		// Add all ports
		add(outDistance);

		// Add all parameters
		add(paramRobot);
	}

	/**
	 * Creates monitor the gripper position
	 *
	 * @param robot Name of youBot
	 */
	public GripperMonitor(RPIstring paramRobot) {
		this();

		// Set the parameters
		setRobot(paramRobot);
	}

	/**
	 * Creates monitor the gripper position
	 *
	 * @param robot Name of youBot
	 */
	public GripperMonitor(String paramRobot) {
		this(new RPIstring(paramRobot));
	}

	/**
	 * Finger distance [m]
	 * 
	 * @return the output port of the block
	 */
	public final OutPort getOutDistance() {
		return this.outDistance;
	}

	/**
	 * Name of youBot
	 * 
	 * @return the parameter of the block
	 */
	public final Parameter<RPIstring> getRobot() {
		return this.paramRobot;
	}

	/**
	 * Sets a parameter of the block: Name of youBot
	 * 
	 * @param value new value of the parameter
	 */
	public final void setRobot(RPIstring value) {
		this.paramRobot.setValue(value);
	}

	/**
	 * Sets a parameter of the block: Name of youBot
	 * 
	 * @param value new value of the parameter
	 */
	public final void setRobot(String value) {
		this.setRobot(new RPIstring(value));
	}

}
