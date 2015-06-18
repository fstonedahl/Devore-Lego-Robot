package testdrive;

import java.util.Random;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;

public class HiccupTest {

	static DifferentialPilot robot;
	static RegulatedMotor leftMotor = Motor.C;
	static RegulatedMotor rightMotor = Motor.B;
	static EV3UltrasonicSensor sensor;
	static SampleProvider sampleProvider;
	static float[] sample;
	
	public static void main(String[] args){
		robot = new DifferentialPilot(5.5, 14.5, leftMotor, rightMotor, false);
		robot.setTravelSpeed(30);
		robot.setRotateSpeed(60);
		
		sensor = new EV3UltrasonicSensor(SensorPort.S1);
		
		run();
	}
	
	public static void run(){
		robot.forward();
		sampleProvider = sensor.getDistanceMode();
		sample = new float[sampleProvider.sampleSize()];
		sampleProvider.fetchSample(sample, 0);
		
		while(!Button.ENTER.isDown()){
			if(sample[0] < 0.3){
				robot.stop();
				robot.travel(-10);
				Random r = new Random();
				int degree = (r.nextInt(250)+110);
				robot.rotate(degree);
				robot.forward();
			}
			sampleProvider.fetchSample(sample, 0);
			
			LCD.drawString("Distance: " + sample[0] + "      ", 0, 0);
		}
	}

}
