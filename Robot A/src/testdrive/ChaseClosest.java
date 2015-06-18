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

public class ChaseClosest {
	
	static DifferentialPilot robot;
	static RegulatedMotor leftMotor = Motor.C;
	static RegulatedMotor rightMotor = Motor.B;
	static RegulatedMotor frontMotor = Motor.A;
	static EV3UltrasonicSensor sensor;
	static SampleProvider sampleProvider;
	static float[] scan;
	static float[] sample;

	public static void main(String[] args) {
		// Sets up Pilot for robot and sensor for robot
		robot = new DifferentialPilot(5.5, 14.5, leftMotor, rightMotor, false);
		sensor = new EV3UltrasonicSensor(SensorPort.S1);
		scan = new float[12];
		beginDrive();
	}
	
	public static void beginDrive(){
		robot.setTravelSpeed(100);
		robot.setAcceleration(500);
		robot.setRotateSpeed(60.0);
		robot.reset();
		boolean forward = true;
		
		while(!Button.ENTER.isDown()){
			scanSurroundings();
			int index = findNearest(scan);
			int angle = index * -30;
			if (angle < -180) { angle += 360; }
			//frontMotor.rotate(index*30);
			robot.rotate(angle);
			robot.travel(4);
			/*while(forward){
				robot.forward();
				Delay.msDelay(500);
				float temp = getDistanceMeasurement();
				if(temp <= 0.4){
					forward = false;
					robot.stop();
				}
			}*/
			//beginDrive();
		}
	}
	
	public static void scanSurroundings(){
		int count = 0;
		while(count < 12){
			if (Button.ENTER.isDown()) { return; }
			
			float temp = getDistanceMeasurement();
			scan[count] = temp;
			frontMotor.rotate(30);
			count++;
		}
		frontMotor.rotate(-360);
	}
	
	public static int findFarthest(float[] distances){
		float maxDistance = 0;
		int index = 0;
		String output = "";
		
		for(int i = 0; i < distances.length; i++){
			output = i + ": "+ distances[i];
			LCD.clear();
			LCD.drawString(output, 0, 0);
			Delay.msDelay(2000);
			if(distances[i] >= maxDistance){
				maxDistance = distances[i];
				index = i;
			}
		}	
		return index;
	}
	public static int findNearest(float[] distances){
		float minDistance = Float.MAX_VALUE;
		int index = 0;
		
		for(int i = 0; i < distances.length; i++){
			if(distances[i] <= minDistance){
				minDistance = distances[i];
				index = i;
			}
		}	
		return index;
	}
	
	public static float getDistanceMeasurement(){
		sampleProvider = sensor.getDistanceMode();
		sample = new float[sampleProvider.sampleSize()];
		sampleProvider.fetchSample(sample, 0);
		String output = "curDist: " + sample[0];
		LCD.clear();
		LCD.drawString(output, 0, 0);
		//Delay.msDelay(2000);
		
		return sample[0];
	}

}
