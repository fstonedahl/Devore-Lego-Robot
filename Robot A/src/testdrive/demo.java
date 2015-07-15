package testdrive;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.utility.Delay;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;

public class demo {
	static DifferentialPilot robot;
	static RegulatedMotor leftMotor = Motor.C;
	static RegulatedMotor rightMotor = Motor.B;
	static RegulatedMotor frontMotor = Motor.A;
	static EV3TouchSensor sensor; 
	static EV3ColorSensor sensorColor;
	static SampleProvider bumpSampleProvider;
	static float[] sample;

	public static void main(String[] args) {
		// Sets up Pilot for robot and sensor for robot
		robot = new DifferentialPilot(5.4, 14.5, leftMotor, rightMotor, false);
		robot.setTravelSpeed(30);
		robot.setAcceleration(120);
		robot.setRotateSpeed(60);
		robot.reset();
		
		sensor = new EV3TouchSensor(SensorPort.S2);
		SampleProvider sp = sensor.getTouchMode();
		 sensorColor = new EV3ColorSensor(SensorPort.S4);
		 
		 run();
	}
	public static void run(){
			robot.forward();
			bumpSampleProvider = sensor.getTouchMode();
			sample = new float[bumpSampleProvider.sampleSize()];
			bumpSampleProvider.fetchSample(sample, 0);
			
			//looking for white
			while(sensorColor.getColorID() != 6&&!Button.ENTER.isDown()){
				if(sample[0] == 1){
					robot.stop();
					robot.travel(-10);
					Random r = new Random();
					int degree = (r.nextInt(250)+110);
					robot.rotate(degree);
					robot.forward();
				}
				bumpSampleProvider = sensor.getTouchMode();
				bumpSampleProvider.fetchSample(sample, 0);
			}
			robot.stop();
			Sound.beepSequenceUp();
			
		}
}

