package org.roboticsapi.device.kuka.youbot.runtime.rpi.primitives;

import org.roboticsapi.facet.runtime.rpi.InPort;
import org.roboticsapi.facet.runtime.rpi.OutPort;
import org.roboticsapi.facet.runtime.rpi.Parameter;
import org.roboticsapi.facet.runtime.rpi.Primitive;
import org.roboticsapi.facet.runtime.rpi.core.types.RPIdouble;
import org.roboticsapi.facet.runtime.rpi.core.types.RPIstring;

/**
 * Double variable to move the gripper
 */
public class Gripper extends Primitive {
	/** Type name of the primitive */
	public static final String PRIMITIVE_TYPE = "KUKA::youBot::Gripper";

	/** Activation port */
	private final InPort inActive = new InPort("inActive");

	/** gripper request has completed */
	private final OutPort outCompleted = new OutPort("outCompleted");

	/** Gripper finger distance */
	private final Parameter<RPIdouble> paramDistance = new Parameter<RPIdouble>("Distance", new RPIdouble("0"));

	/** Name of youBot */
	private final Parameter<RPIstring> paramRobot = new Parameter<RPIstring>("Robot", new RPIstring(""));

	public Gripper() {
		super(PRIMITIVE_TYPE);

		// Add all ports
		add(inActive);
		add(outCompleted);

		// Add all parameters
		add(paramDistance);
		add(paramRobot);
	}

	/**
	 * Creates double variable to move the gripper
	 * 
	 * @param distance Gripper finger distance
	 * @param robot    Name of youBot
	 */
	public Gripper(RPIdouble paramDistance, RPIstring paramRobot) {
		this();

		// Set the parameters
		setDistance(paramDistance);
		setRobot(paramRobot);
	}

	/**
	 * Creates double variable to move the gripper
	 * 
	 * @param distance Gripper finger distance
	 * @param robot    Name of youBot
	 */
	public Gripper(Double paramDistance, String paramRobot) {
		this(new RPIdouble(paramDistance), new RPIstring(paramRobot));
	}

	/**
	 * Activation port
	 * 
	 * @return the input port of the block
	 */
	public final InPort getInActive() {
		return this.inActive;
	}

	/**
	 * gripper request has completed
	 * 
	 * @return the output port of the block
	 */
	public final OutPort getOutCompleted() {
		return this.outCompleted;
	}

	/**
	 * Gripper finger distance
	 * 
	 * @return the parameter of the block
	 */
	public final Parameter<RPIdouble> getDistance() {
		return this.paramDistance;
	}

	/**
	 * Sets a parameter of the block: Gripper finger distance
	 * 
	 * @param value new value of the parameter
	 */
	public final void setDistance(RPIdouble value) {
		this.paramDistance.setValue(value);
	}

	/**
	 * Sets a parameter of the block: Gripper finger distance
	 * 
	 * @param value new value of the parameter
	 */
	public final void setDistance(Double value) {
		this.setDistance(new RPIdouble(value));
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
