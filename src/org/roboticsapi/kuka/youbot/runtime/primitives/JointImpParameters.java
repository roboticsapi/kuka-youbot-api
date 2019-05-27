package org.roboticsapi.kuka.youbot.runtime.primitives;

import org.roboticsapi.runtime.core.types.RPIdouble;
import org.roboticsapi.runtime.core.types.RPIint;
import org.roboticsapi.runtime.core.types.RPIstring;
import org.roboticsapi.runtime.rpi.InPort;
import org.roboticsapi.runtime.rpi.OutPort;
import org.roboticsapi.runtime.rpi.Parameter;
import org.roboticsapi.runtime.rpi.Primitive;

/**
 * Set joint Impedance parameter stiffness and damping
 */
public class JointImpParameters extends Primitive {
	/** Type name of the primitive */
	public static final String PRIMITIVE_TYPE = "KUKA::youBot::JointImpParameters";

	/** Activation port */
	private final InPort inActive = new InPort("inActive");

	/** Additional Torque in Nm */
	private final InPort inAddTorque = new InPort("inAddTorque");

	/** Damping to set */
	private final InPort inDamping = new InPort("inDamping");

	/** Maximum Torque caused by impedance controller in Nm, <=0 for unlimited */
	private final InPort inMaxTorque = new InPort("inMaxTorque");

	/** Stiffness to set */
	private final InPort inStiffness = new InPort("inStiffness");

	/** Parameter setting was successful */
	private final OutPort outSuccess = new OutPort("outSuccess");

	/** Name of youBot */
	private final Parameter<RPIstring> paramRobot = new Parameter<RPIstring>("Robot", new RPIstring(""));

	/** Stiffness Parameter in Nm/rad */
	private final Parameter<RPIdouble> paramStiffness = new Parameter<RPIdouble>("Stiffness", new RPIdouble("0"));

	/** Damping Parameter relative in 0..1 range */
	private final Parameter<RPIdouble> paramDamping = new Parameter<RPIdouble>("Damping", new RPIdouble("0"));

	/** Additional Torque in Nm */
	private final Parameter<RPIdouble> paramAddTorque = new Parameter<RPIdouble>("AddTorque", new RPIdouble("0"));

	/**
	 * Maximum Torque caused by the impedance controller in Nm, <=0 for unlimited
	 */
	private final Parameter<RPIdouble> paramMaxTorque = new Parameter<RPIdouble>("MaxTorque", new RPIdouble("0"));

	/** Joint Index to set paramters */
	private final Parameter<RPIint> paramJointIndex = new Parameter<RPIint>("JointIndex", new RPIint("0"));

	public JointImpParameters() {
		super(PRIMITIVE_TYPE);

		// Add all ports
		add(inActive);
		add(inAddTorque);
		add(inDamping);
		add(inMaxTorque);
		add(inStiffness);
		add(outSuccess);

		// Add all parameters
		add(paramRobot);
		add(paramStiffness);
		add(paramDamping);
		add(paramAddTorque);
		add(paramMaxTorque);
		add(paramJointIndex);
	}

	/**
	 * Creates set joint Impedance parameter stiffness and damping
	 * 
	 * @param robot      Name of youBot
	 * @param jointIndex Joint Index to set paramters
	 */
	public JointImpParameters(RPIstring paramRobot, RPIint paramJointIndex) {
		this();

		// Set the parameters
		setRobot(paramRobot);
		setJointIndex(paramJointIndex);
	}

	/**
	 * Creates set joint Impedance parameter stiffness and damping
	 * 
	 * @param robot      Name of youBot
	 * @param jointIndex Joint Index to set paramters
	 */
	public JointImpParameters(String paramRobot, Integer paramJointIndex) {
		this(new RPIstring(paramRobot), new RPIint(paramJointIndex));
	}

	/**
	 * Creates set joint Impedance parameter stiffness and damping
	 * 
	 * @param paramRobot      Name of youBot
	 * @param paramStiffness  Stiffness Parameter in Nm/rad
	 * @param paramDamping    Damping Parameter relative in 0..1 range
	 * @param paramAddTorque  Additional Torque in Nm
	 * @param paramMaxTorque  Maximum Torque caused by the impedance controller in
	 *                        Nm, <=0 for unlimited
	 * @param paramJointIndex Joint Index to set paramters
	 */
	public JointImpParameters(RPIstring paramRobot, RPIdouble paramStiffness, RPIdouble paramDamping,
			RPIdouble paramAddTorque, RPIdouble paramMaxTorque, RPIint paramJointIndex) {
		this();

		// Set the parameters
		setRobot(paramRobot);
		setStiffness(paramStiffness);
		setDamping(paramDamping);
		setAddTorque(paramAddTorque);
		setMaxTorque(paramMaxTorque);
		setJointIndex(paramJointIndex);
	}

	/**
	 * Creates set joint Impedance parameter stiffness and damping
	 * 
	 * @param paramRobot      Name of youBot
	 * @param paramStiffness  Stiffness Parameter in Nm/rad
	 * @param paramDamping    Damping Parameter relative in 0..1 range
	 * @param paramAddTorque  Additional Torque in Nm
	 * @param paramMaxTorque  Maximum Torque caused by the impedance controller in
	 *                        Nm, <=0 for unlimited
	 * @param paramJointIndex Joint Index to set paramters
	 */
	public JointImpParameters(String paramRobot, Double paramStiffness, Double paramDamping, Double paramAddTorque,
			Double paramMaxTorque, Integer paramJointIndex) {
		this(new RPIstring(paramRobot), new RPIdouble(paramStiffness), new RPIdouble(paramDamping),
				new RPIdouble(paramAddTorque), new RPIdouble(paramMaxTorque), new RPIint(paramJointIndex));
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
	 * Additional Torque in Nm
	 * 
	 * @return the input port of the block
	 */
	public final InPort getInAddTorque() {
		return this.inAddTorque;
	}

	/**
	 * Damping to set
	 * 
	 * @return the input port of the block
	 */
	public final InPort getInDamping() {
		return this.inDamping;
	}

	/**
	 * Maximum Torque caused by impedance controller in Nm, <=0 for unlimited
	 * 
	 * @return the input port of the block
	 */
	public final InPort getInMaxTorque() {
		return this.inMaxTorque;
	}

	/**
	 * Stiffness to set
	 * 
	 * @return the input port of the block
	 */
	public final InPort getInStiffness() {
		return this.inStiffness;
	}

	/**
	 * Parameter setting was successful
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
	 * Stiffness Parameter in Nm/rad
	 * 
	 * @return the parameter of the block
	 */
	public final Parameter<RPIdouble> getStiffness() {
		return this.paramStiffness;
	}

	/**
	 * Sets a parameter of the block: Stiffness Parameter in Nm/rad
	 * 
	 * @param value new value of the parameter
	 */
	public final void setStiffness(RPIdouble value) {
		this.paramStiffness.setValue(value);
	}

	/**
	 * Sets a parameter of the block: Stiffness Parameter in Nm/rad
	 * 
	 * @param value new value of the parameter
	 */
	public final void setStiffness(Double value) {
		this.setStiffness(new RPIdouble(value));
	}

	/**
	 * Damping Parameter relative in 0..1 range
	 * 
	 * @return the parameter of the block
	 */
	public final Parameter<RPIdouble> getDamping() {
		return this.paramDamping;
	}

	/**
	 * Sets a parameter of the block: Damping Parameter relative in 0..1 range
	 * 
	 * @param value new value of the parameter
	 */
	public final void setDamping(RPIdouble value) {
		this.paramDamping.setValue(value);
	}

	/**
	 * Sets a parameter of the block: Damping Parameter relative in 0..1 range
	 * 
	 * @param value new value of the parameter
	 */
	public final void setDamping(Double value) {
		this.setDamping(new RPIdouble(value));
	}

	/**
	 * Additional Torque in Nm
	 * 
	 * @return the parameter of the block
	 */
	public final Parameter<RPIdouble> getAddTorque() {
		return this.paramAddTorque;
	}

	/**
	 * Sets a parameter of the block: Additional Torque in Nm
	 * 
	 * @param value new value of the parameter
	 */
	public final void setAddTorque(RPIdouble value) {
		this.paramAddTorque.setValue(value);
	}

	/**
	 * Sets a parameter of the block: Additional Torque in Nm
	 * 
	 * @param value new value of the parameter
	 */
	public final void setAddTorque(Double value) {
		this.setAddTorque(new RPIdouble(value));
	}

	/**
	 * Maximum Torque caused by the impedance controller in Nm, <=0 for unlimited
	 * 
	 * @return the parameter of the block
	 */
	public final Parameter<RPIdouble> getMaxTorque() {
		return this.paramMaxTorque;
	}

	/**
	 * Sets a parameter of the block: Maximum Torque caused by the impedance
	 * controller in Nm, <=0 for unlimited
	 * 
	 * @param value new value of the parameter
	 */
	public final void setMaxTorque(RPIdouble value) {
		this.paramMaxTorque.setValue(value);
	}

	/**
	 * Sets a parameter of the block: Maximum Torque caused by the impedance
	 * controller in Nm, <=0 for unlimited
	 * 
	 * @param value new value of the parameter
	 */
	public final void setMaxTorque(Double value) {
		this.setMaxTorque(new RPIdouble(value));
	}

	/**
	 * Joint Index to set paramters
	 * 
	 * @return the parameter of the block
	 */
	public final Parameter<RPIint> getJointIndex() {
		return this.paramJointIndex;
	}

	/**
	 * Sets a parameter of the block: Joint Index to set paramters
	 * 
	 * @param value new value of the parameter
	 */
	public final void setJointIndex(RPIint value) {
		this.paramJointIndex.setValue(value);
	}

	/**
	 * Sets a parameter of the block: Joint Index to set paramters
	 * 
	 * @param value new value of the parameter
	 */
	public final void setJointIndex(Integer value) {
		this.setJointIndex(new RPIint(value));
	}

}