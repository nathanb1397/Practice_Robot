package org.usfirst.frc.team4322.practice_robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class RobotDriveBase implements PIDSource, PIDOutput {

	// Instance for this Singleton Class
	private static RobotDriveBase _instance = null;
	
	// Instance for DistancePID class
	private DistancePID distancePID = null;
	
	// Instance for Power Distribution Panel
	private static PowerDistributionPanel pdp = null;
	
	// Instances for CAN_Jaguars
	private static CANJaguar Jaguar_1 = null;
	private static CANJaguar Jaguar_2 = null;
	
	// Instance for RobotDrive function from WPILib
	private static RobotDrive robotDrive = null;
	
	// Instance for RangeFinder
	private static RangeFinder rangeFinder = null;
	
	// Instance for Gyro
	private static Gyro gyro = null;
	
	// Drive base State Engine values
	private static final int STATE_DRIVE_MANUAL = 0;
	private static final int STATE_DRIVE_FORWARD_TO_DISTANCE = 1;
	private static final int STATE_DRIVE_TURN_AROUND = 2;
	private static final int STATE_DRIVE_BACK_TO_BEGINNING = 3;
	private int currentState = STATE_DRIVE_MANUAL;
	
	// Boolean for button control
	boolean buttonPressed = false;
	boolean secondTry = false;
	
	// This is the static getInstance() method that provides easy access to the RobotDriveBase singleton class.
    public static RobotDriveBase getInstance() {
    	
        // Look to see if the instance has already been created...
        if(_instance == null) {
        	
            // If the instance does not yet exist, create it.
            _instance = new RobotDriveBase();
            
        }
        
        // Return the singleton instance to the caller.
        return _instance;
        
    }
    
    public void initAutonomous()
    {
    	if(distancePID == null)
    	{
    		// Create an instance of DistancePID called distancePID
    		// Arguments are ( P , I , D , source (looks for pidGet in THIS class), output (looks for pidWrite in THIS class) ) 
    		distancePID = new DistancePID(0.5, 0.5, 0.1, this, this);
    	}
    	// Set PID setpoint to 36 inches.
    	distancePID.setSetpoint(36);
    	// Set PID tollerance range.
    	distancePID.setPercentTolerance(2.0);
    	// Set minimum and maximum output of the PID controller
    	distancePID.setOutputRange(-0.8, 0.8);
    	// Begin running the PID controller.
    	distancePID.enable();
    }
    
    public void runAutonomous()
    {
    	if(distancePID.onTarget())
    	{
    		distancePID.disable();
    	}
    }
    
    // Initialize Instances (Void Operation because there are no values being returned.)
    public void initRobotDrive()
    {
    	if (pdp == null)
    	{
    		pdp = new PowerDistributionPanel(); // PDP CAN address must be at 0 unless otherwise specified.
    		pdp.clearStickyFaults(); // Clear any alerts/faults with PDP and initialize.
    	}
    	
    	// Initializes Jaguars and assigns their CAN address from RobotMap
    	if (Jaguar_1 == null) Jaguar_1 = new CANJaguar(RobotMap.CAN_Jaguar_1);
    	if (Jaguar_2 == null) Jaguar_2 = new CANJaguar(RobotMap.CAN_Jaguar_2);
    	
    	// Initializes RobotDrive instance and sets Jaguars to (Left, Right) positions
    	if (robotDrive == null) robotDrive = new RobotDrive(Jaguar_1, Jaguar_2);
    	
    	// Initializes RangeFinder instance and sets Analog channel.
    	if (rangeFinder == null) rangeFinder = new RangeFinder(RobotMap.RANGE_FINDER_PORT);
    	
    	// Initializes RangeFinder instance and sets Analog channel.
    	if (gyro == null) gyro = new Gyro(RobotMap.GYRO_PORT);
    	gyro.reset();
    }
	
    
    // Shutsdown DriveBase by setting Drive Controllers (Jaguars, Talons, etc.) to NULL
    public void shutdownRobotDrive()
    {
    	if (Jaguar_1 != null) Jaguar_1 = null;
    	if (Jaguar_2 != null) Jaguar_2 = null;
    }
    
    
    public void initTeleOp()
    {
    	gyro.reset();
    }
    
    
    
    // Periodic code for teleop mode should go here. This method is called ~50x per second.
    public void runTeleOp()
    {	
    	
    	// Write controller values to SmartDashboard
    	SmartDashboard.putNumber("throttleStick: ", PilotController.getInstance().throttleStick());
    	SmartDashboard.putNumber("steeringStick: ", PilotController.getInstance().steeringStick());
    	
    	// Add power limit
    	double steeringValue = PilotController.getInstance().steeringStick() * RobotMap.POWER_LIMIT;
    	double throttleValue = PilotController.getInstance().throttleStick() * RobotMap.POWER_LIMIT;
    	
    	// Write throttle values to SmartDashboard
    	SmartDashboard.putNumber("throttleValue: ", throttleValue);
    	SmartDashboard.putNumber("steeringValue: ", steeringValue);
    	
    	// Write range finder distance to SmartDashboard
    	SmartDashboard.putNumber("Distance in Inches: ", rangeFinder.GetRangeInInches());
    	SmartDashboard.putNumber("Sensor Voltage: ", rangeFinder.GetVoltage());
    	
    	// Add state to SmartDashboard
    	if (currentState == STATE_DRIVE_MANUAL) SmartDashboard.putString("State: ", "STATE_DRIVE_MANUAL");
    	else if (currentState == STATE_DRIVE_FORWARD_TO_DISTANCE) SmartDashboard.putString("State: ", "STATE_DRIVE_FORWARD_TO_DISTANCE");
    	else if (currentState == STATE_DRIVE_TURN_AROUND) SmartDashboard.putString("State: ", "STATE_DRIVE_TURN_AROUND");

    	
    	// Get RAW gyro angle
    	double gyroAngle = gyro.getAngle();
    	// Make gyro angle a positive number, regardless of turn direction
    	gyroAngle = Math.abs(gyroAngle);
    	// Force gyro angle to output within 360 degrees
    	gyroAngle = gyroAngle % 360;
    	
    	// Write range finder distance to SmartDashboard
    	SmartDashboard.putNumber("Gyro Angle: ", gyroAngle);
    	
    	// Monitor the button for changing states
    	if(PilotController.getInstance().stateSwitchingButton())
    	{
    		// This code runs only ONCE right when the button is first pressed
    		if(!buttonPressed)
    		{
    			if(currentState == STATE_DRIVE_MANUAL)
    			{
    				currentState = STATE_DRIVE_FORWARD_TO_DISTANCE;
    			}
    			else
    			{
    				currentState = STATE_DRIVE_MANUAL;
    			}
    		}
    		// Help us not process this button press 50 times per second
    		buttonPressed = true;
    	}
    	else
    	{
    		// Thanks for letting go of the button,
    		// Now, you can press it again.
    		buttonPressed = false;
    	}


    	// Run the STATE ENGINE
    	switch(currentState)
    	{
    		case STATE_DRIVE_MANUAL:
    	    	// WPI is broken, throttle and steering is reversed from documentation
    	    	// Robot Drive in Arcade is set with the "steering" and "throttle" from the controller being used in that order.
    	    	robotDrive.arcadeDrive(steeringValue,  throttleValue);
    			break;

    		case STATE_DRIVE_FORWARD_TO_DISTANCE:
    			// Monitor the range finder and drive forward until we are within 36" of the target
    			if(rangeFinder.GetRangeInInches() > (36 + 10))
    			{
    				// We need to keep driving straight forward until we are in range
        	    	robotDrive.arcadeDrive(0.0,  -0.7);
    			}
    			else
    			{
    				// Stop the drive base and change states to manual mode
        	    	robotDrive.arcadeDrive(0.0,  0.0);
        	    	gyro.reset();
        	    	currentState = STATE_DRIVE_TURN_AROUND;
    			}
    			break;
    		
    		case STATE_DRIVE_TURN_AROUND:
    			// Monitor the angle of the gryo
    			if(gyroAngle < (180 - 20))
    			{
    				// We need to keep driving straight forward until we are in range
        	    	if (!secondTry)
        	    	{
        	    		robotDrive.arcadeDrive(0.7,  0.0);
        	   		}
        	    	else
        	    	{
        	    		robotDrive.arcadeDrive(-0.7,  0.0);
        	    	}
    			}
    			else
    			{
    				// Stop the drive base and change states to manual mode
        	    	robotDrive.arcadeDrive(0.0,  0.0);
        	    	secondTry = !secondTry;
        	    	currentState = STATE_DRIVE_FORWARD_TO_DISTANCE;
    			}
    			break;

    		case STATE_DRIVE_BACK_TO_BEGINNING:
    			break;
    	
    		default:
    			currentState = STATE_DRIVE_MANUAL;
    			break;
    	}

    }



	@Override
	public void pidWrite(double output) {
		// TODO Auto-generated method stub
		robotDrive.arcadeDrive(0, (output * -1));
	}



	@Override
	public double pidGet() {
		// TODO Auto-generated method stub
		return rangeFinder.GetRangeInInches();
	}
    
}
