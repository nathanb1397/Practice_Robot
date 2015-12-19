package org.usfirst.frc.team4322.practice_robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
	
	private boolean disabledBegin = false;
	//private boolean autoBegin = false;
	private boolean teleBegin = false;
	//private boolean testBegin = false;
	//private boolean resetPressed = false;
	private boolean matchRecord = false;
	
    public void robotInit() {
    	
    	RobotDriveBase.getInstance().initRobotDrive();
    	SmartDashboard.putString("Last Robot Build Time", RobotMap.LAST_BUILD_TIME);
    	SmartDashboard.putString("Code Author", RobotMap.CODE_AUTHOR);

    }
    
    @Override
	public void disabledInit(){
		disabledBegin = false;
	}
    
    @Override
	public void disabledPeriodic(){
    	
    	if (!disabledBegin) disabledBegin = true;
    	if(DriverStation.getInstance().isFMSAttached() && !matchRecord) matchRecord = true;
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	
    	if (!teleBegin) teleBegin = true;
    	RobotDriveBase.getInstance().runTeleOp();
        
    	// Write controller values to SmartDashboard
    	SmartDashboard.putNumber("throttleStick", PilotController.getInstance().throttleStick());
    	SmartDashboard.putNumber("steeringStick", PilotController.getInstance().steeringStick());
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}
