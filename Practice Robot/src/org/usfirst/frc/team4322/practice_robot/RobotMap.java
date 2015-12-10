package org.usfirst.frc.team4322.practice_robot;

import java.io.IOException;
import java.util.jar.JarFile;

public class RobotMap {
	
	// Create a String with the Last Build Time of Code
	public static String LAST_BUILD_TIME;
	static{
		try {
			LAST_BUILD_TIME = new JarFile(RobotMap.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getManifest().getMainAttributes().getValue("Build-Time");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Set Joystick Ports for Drive Station
	public static int PILOT = 0;
	public static int COPILOT = 1;
	public static int CONTROL_PANEL = 2;
	
	// Set CAN_Jaguar Ports
	public static int CAN_Jaguar_1 = 10;
	public static int CAN_Jaguar_2 = 11;

}
