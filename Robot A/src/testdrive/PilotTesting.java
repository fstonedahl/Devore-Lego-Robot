package testdrive;

import lejos.hardware.motor.Motor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.navigation.DifferentialPilot;

public class PilotTesting {

	public static void main(String[] args) {
		DifferentialPilot robot;
		RegulatedMotor leftMotor = Motor.C;
		RegulatedMotor rightMotor = Motor.B;
		
		robot = new DifferentialPilot(5.5, 15, leftMotor, rightMotor, true);
		robot.setRotateSpeed(60.0);
		robot.reset();
		robot.rotate(-90);
	}

}
