package org.usfirst.frc.team4322.practice_robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class RobotDriveBase {

	// Instance for this Singleton Class
	private static RobotDriveBase _instance = null;
	
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

    	
    	// WPI is broken, throttle and steering is reversed from documentation
    	// Robot Drive in Arcade is set with the "steering" and "throttle" from the controller being used in that order.
    	robotDrive.arcadeDrive(steeringValue,  throttleValue);
    	
    	// Write range finder distance to SmartDashboard
    	SmartDashboard.putNumber("Distance in Inches: ", rangeFinder.GetRangeInInches());
    	SmartDashboard.putNumber("Sensor Voltage: ", rangeFinder.GetVoltage());
    	
    	// Get RAW gyro angle
    	double gyroAngle = gyro.getAngle();
    	// Make gyro angle a positive number, regardless of turn direction
    	gyroAngle = Math.abs(gyroAngle);
    	// Force gyro angle to output within 360 degrees
    	gyroAngle = gyroAngle % 360;
    	
    	// Write range finder distance to SmartDashboard
    	SmartDashboard.putNumber("Gyro Angle: ", gyroAngle);
    }
    
}
