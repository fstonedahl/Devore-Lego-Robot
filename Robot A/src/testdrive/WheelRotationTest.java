package testdrive;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.utility.Delay;

public class WheelRotationTest {

	public static void main(String[] args) {
		int i = 0;
        int angle =20;
		while (!Button.ENTER.isDown() || i == 5) {
			turn(20, angle);
			Delay.msDelay(3000);
			angle = angle + 20;
			i++;
		}

	}

	public static void turn(float angle, float rotateSpeed) {

		LCD.drawString("Testing: Angle=" + angle, 0, 3);
		LCD.drawString(" RotateSpeed=" + rotateSpeed + ".", 0, 4);

		DifferentialPilot robot;
		RegulatedMotor leftMotor = Motor.C;
		RegulatedMotor rightMotor = Motor.B;

		robot = new DifferentialPilot(5.5, 14.5, leftMotor, rightMotor, false);
		robot.setRotateSpeed(angle);
		robot.reset();
		robot.rotate(rotateSpeed);
	}

}
