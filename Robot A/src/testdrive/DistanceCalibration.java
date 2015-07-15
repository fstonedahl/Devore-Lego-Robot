package testdrive;

import java.io.IOException;
import java.util.ArrayList;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.utility.Delay;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;

public class DistanceCalibration {
	static DifferentialPilot robot;
	static RegulatedMotor leftMotor = Motor.C;
	static RegulatedMotor rightMotor = Motor.B;
	static RegulatedMotor frontMotor = Motor.A;
	static EV3UltrasonicSensor sensor;
	static SampleProvider sampleProvider;

	public static void main(String[] args) {
		// Sets up Pilot for robot and sensor for robot
		robot = new DifferentialPilot(5.4, 14.5, leftMotor, rightMotor, false);
		// sensor = new EV3UltrasonicSensor(SensorPort.S1);

		// while(!Button.ENTER.isDown()){
		/*for (int j = 0; j < 10; j++) {
			int angle = 0;
			String output;
			int i = 0;
			while (angle != 360) {
				frontMotor.rotate(45);
				angle = angle + 45;
				output = ": " + angle;
				LCD.drawString(output, i, i);
				i++;
				Delay.msDelay(100);
			}
			frontMotor.rotate(-360);
			Delay.msDelay(500);
			LCD.clear();
		}
	}
	// }*/
		robot.setTravelSpeed(30);
		robot.setAcceleration(60);
		robot.setRotateSpeed(60);
		robot.reset();
		//boolean forward = true;
		int i = 0; 
		while(i!=5){
			robot.travel(40);
			Delay.msDelay(1000);
			i++;
		}
			
	}		
		
}

