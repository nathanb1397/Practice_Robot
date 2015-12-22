package org.usfirst.frc.team4322.practice_robot;

import edu.wpi.first.wpilibj.GenericHID.Hand;

public class PilotController {

	private static PilotController _instance = null;
	private XboxOneController controller = null;
	public boolean isPilotDriving = true;
	
	/** The Alternate Drive Profile swaps driving sticks
	 * Steering will be left stick X axis
	 * Throttle will be right stick Y axis
	 * Strafing will be right stick X axis
	 */
    
	public static PilotController getInstance()
	{
		if(_instance == null)
		{
			_instance = new PilotController();
			_instance.controller = new XboxOneController(RobotMap.PILOT);
		}
		return _instance;
	}
	
	// Get values from left Xbox One controller stick and set to throttle value.
	public double throttleStick(){
		
		return controller.getY(Hand.kLeft);
		
	}
	
	// Get values from right Xbox One controller stick and set to throttle value.
	public double steeringStick(){
		
		return controller.getX(Hand.kRight);
		
	}
	
	public boolean stateSwitchingButton()
	{
			return controller.getXButton();
	}
}
