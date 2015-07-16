package testdrive;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.utility.Delay;

public class WheelRotationTest {

	public static void main(String[] args) {
		/*
		 * int angle; int rotate = 20; for (int j = 0; j < 4 &&
		 * !Button.ENTER.isDown(); j++) { angle = 20; for (int i = 0; i < 5 &&
		 * !Button.ENTER.isDown(); i++) { turn(angle, rotate);
		 * Delay.msDelay(9000); angle = angle + 20; } rotate = rotate + 20;
		 * while (!Button.ESCAPE.isDown()) { Delay.msDelay(10); } }
		 */
		
		
		/*
		int rotate = 60;
		int angle = 30;
		for (int i = 0; i < 6; i++) {
			turn (angle, rotate);
			angle = angle + 30;
			while (!Button.ENTER.isDown()) {
				Delay.msDelay(10);
			}
		}
		*/
		
		for (int i = 0; i < 10; i++) {
			turn (90, 60);
			while (!Button.ENTER.isDown()) {
				Delay.msDelay(10);
			}
		}

	}

	public static void turn(float angle, float rotateSpeed) {

		LCD.drawString("Testing: Angle=" + angle, 0, 3);
		LCD.drawString(" RotateSpeed=" + rotateSpeed + ".", 0, 4);

		DifferentialPilot robot;
		RegulatedMotor leftMotor = Motor.C;
		RegulatedMotor rightMotor = Motor.B;

		robot = new DifferentialPilot(5.5, 14.5, leftMotor, rightMotor, false);
		robot.setRotateSpeed(rotateSpeed);
		robot.reset();
		robot.rotate(angle);
	}

}
