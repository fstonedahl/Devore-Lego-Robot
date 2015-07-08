package testdrive;

import java.util.Random;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.utility.Delay;
import lejos.hardware.sensor.EV3TouchSensor;

public class VConeTarget {

	static DifferentialPilot robot;
	static RegulatedMotor leftMotor = Motor.C;
	static RegulatedMotor rightMotor = Motor.B;
	
	public static void main(String[] args){
		robot = new DifferentialPilot(5.5, 14.5, leftMotor, rightMotor, false);
		robot.setTravelSpeed(10);
		robot.setAcceleration(5);
	
		// wait for the button to come back up
		while(!Button.ESCAPE.isDown()){
			LCD.clear();
			LCD.drawString("Traveling 300cm", 0, 0);

			robot.travel(300);
			LCD.clear();
			LCD.drawString("Press ENTER or ESC!", 0, 1);
			while(!Button.ENTER.isDown() && !Button.ESCAPE.isDown()){
				Delay.msDelay(10);
			}
		}		
	}
	

}
