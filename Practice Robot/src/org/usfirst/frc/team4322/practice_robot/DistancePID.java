package org.usfirst.frc.team4322.practice_robot;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;

public class DistancePID extends PIDController {

	public DistancePID(double Kp, double Ki, double Kd, PIDSource source, PIDOutput output) {
		super(Kp, Ki, Kd, source, output);
		// TODO Auto-generated constructor stub
	}

	public DistancePID(double Kp, double Ki, double Kd, PIDSource source, PIDOutput output, double period) {
		super(Kp, Ki, Kd, source, output, period);
		// TODO Auto-generated constructor stub
	}

	public DistancePID(double Kp, double Ki, double Kd, double Kf, PIDSource source, PIDOutput output) {
		super(Kp, Ki, Kd, Kf, source, output);
		// TODO Auto-generated constructor stub
	}

	public DistancePID(double Kp, double Ki, double Kd, double Kf, PIDSource source, PIDOutput output, double period) {
		super(Kp, Ki, Kd, Kf, source, output, period);
		// TODO Auto-generated constructor stub
	}

}
