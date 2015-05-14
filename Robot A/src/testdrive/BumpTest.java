package testdrive;

import java.util.Random;

import lejos.hardware.Button;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.hardware.sensor.EV3TouchSensor;

public class BumpTest {

	static DifferentialPilot robot;
	static RegulatedMotor leftMotor = Motor.C;
	static RegulatedMotor rightMotor = Motor.B;
	static EV3TouchSensor sensor;
	static SampleProvider sampleProvider;
	static float[] sample;
	
	public static void main(String[] args){
		robot = new DifferentialPilot(5.5, 14.5, leftMotor, rightMotor, true);
		robot.setTravelSpeed(30);
		robot.setRotateSpeed(60);
		
		sensor = new EV3TouchSensor(SensorPort.S2);
		SampleProvider sp = sensor.getTouchMode();
		
		run();
	}
	
	public static void run(){
		robot.forward();
		sampleProvider = sensor.getTouchMode();
		sample = new float[sampleProvider.sampleSize()];
		sampleProvider.fetchSample(sample, 0);
		
		while(!Button.ENTER.isDown()){
			if(sample[0] == 1){
				robot.stop();
				robot.travel(-10);
				Random r = new Random();
				int degree = (r.nextInt(250)+110);
				robot.rotate(degree);
				robot.forward();
			}
			sampleProvider = sensor.getTouchMode();
			sampleProvider.fetchSample(sample, 0);
		}
	}

}
