package org.roboticsapi.device.kuka.youbot.runtime.rpi.primitives;

import org.roboticsapi.facet.runtime.rpi.InPort;
import org.roboticsapi.facet.runtime.rpi.OutPort;
import org.roboticsapi.facet.runtime.rpi.Parameter;
import org.roboticsapi.facet.runtime.rpi.Primitive;
import org.roboticsapi.facet.runtime.rpi.core.types.RPIint;
import org.roboticsapi.facet.runtime.rpi.core.types.RPIstring;

/**
 * Integer variable to select controller
 */
public class ArmControlStrategy extends Primitive {
	/** Type name of the primitive */
	public static final String PRIMITIVE_TYPE = "KUKA::youBot::ArmControlStrategy";

	/** Activation port */
	private final InPort inActive = new InPort("inActive");

	/** Index to select Controller */
	private final InPort inController = new InPort("inController");

	/** controller selection has completed */
	private final OutPort outSuccess = new OutPort("outSuccess");

	/** Name of youBot */
	private final Parameter<RPIstring> paramRobot = new Parameter<RPIstring>("Robot", new RPIstring(""));

	/** Index of Controller to set */
	private final Parameter<RPIint> paramController = new Parameter<RPIint>("Controller", new RPIint("0"));

	public ArmControlStrategy() {
		super(PRIMITIVE_TYPE);

		// Add all ports
		add(inActive);
		add(inController);
		add(outSuccess);

		// Add all parameters
		add(paramRobot);
		add(paramController);
	}

	/**
	 * Creates integer variable to select controller
	 * 
	 * @param robot Name of youBot
	 */
	public ArmControlStrategy(RPIstring paramRobot) {
		this();

		// Set the parameters
		setRobot(paramRobot);
	}

	/**
	 * Creates integer variable to select controller
	 * 
	 * @param robot Name of youBot
	 */
	public ArmControlStrategy(String paramRobot) {
		this(new RPIstring(paramRobot));
	}

	/**
	 * Creates integer variable to select controller
	 * 
	 * @param paramRobot      Name of youBot
	 * @param paramController Index of Controller to set
	 */
	public ArmControlStrategy(RPIstring paramRobot, RPIint paramController) {
		this();

		// Set the parameters
		setRobot(paramRobot);
		setController(paramController);
	}

	/**
	 * Creates integer variable to select controller
	 * 
	 * @param paramRobot      Name of youBot
	 * @param paramController Index of Controller to set
	 */
	public ArmControlStrategy(String paramRobot, Integer paramController) {
		this(new RPIstring(paramRobot), new RPIint(paramController));
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
	 * Index to select Controller
	 * 
	 * @return the input port of the block
	 */
	public final InPort getInController() {
		return this.inController;
	}

	/**
	 * controller selection has completed
	 * 
	 * @return the output port of the block
	 */
	public final OutPort getOutSuccess() {
		return this.outSuccess;
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

	/**
	 * Index of Controller to set
	 * 
	 * @return the parameter of the block
	 */
	public final Parameter<RPIint> getController() {
		return this.paramController;
	}

	/**
	 * Sets a parameter of the block: Index of Controller to set
	 * 
	 * @param value new value of the parameter
	 */
	public final void setController(RPIint value) {
		this.paramController.setValue(value);
	}

	/**
	 * Sets a parameter of the block: Index of Controller to set
	 * 
	 * @param value new value of the parameter
	 */
	public final void setController(Integer value) {
		this.setController(new RPIint(value));
	}

}
