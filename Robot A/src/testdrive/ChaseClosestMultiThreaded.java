package testdrive;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.utility.Delay;

public class ChaseClosestMultiThreaded {
	static DifferentialPilot robot;
	static RegulatedMotor leftMotor = Motor.C;
	static RegulatedMotor rightMotor = Motor.B;
	static RegulatedMotor neckMotor = Motor.A;
	static EV3UltrasonicSensor distanceSensor;
	static SampleProvider sampleProvider;
	static float[] scan;
	static float[] sample;
	static final int SCAN_ANGLE = 45;

	public static void main(String[] args) {
		// Sets up Pilot for robot and sensor for robot
		robot = new DifferentialPilot(5.5, 14.5, leftMotor, rightMotor, false);
		distanceSensor = new EV3UltrasonicSensor(SensorPort.S1);
		sampleProvider = distanceSensor.getDistanceMode();
		scan = new float[360 / SCAN_ANGLE];
		for (int i = 0; i < scan.length; i++) {
			scan[i] = 1000; // large value for things we haven't seen yet.
		}
		VisionRunner visionRunner = new VisionRunner(); 
		Thread visionThread = new Thread(visionRunner);
		visionThread.start();
		beginDrive();
		
	}

	public static void beginDrive() {
		robot.setTravelSpeed(100);
		robot.setAcceleration(500);
		robot.setRotateSpeed(60.0);
		robot.reset();
		// boolean forward = true;

		while (!Button.ENTER.isDown()) {
			int index = findNearest(scan);
			int angle = index * -SCAN_ANGLE;
			if (angle < -180) {
				angle += 360;
			}
			// frontMotor.rotate(index*30);
			robot.rotate(angle);
			robot.travel(10);
			Delay.msDelay(6000);
			/*
			 * while(forward){ robot.forward(); Delay.msDelay(500); float temp =
			 * getDistanceMeasurement(); if(temp <= 0.4){ forward = false;
			 * robot.stop(); } }
			 */
			// beginDrive();
		}
	}

	public static void scanSurroundings() {

		for (int i = 0; i < scan.length; i++) {
			if (Button.ENTER.isDown()) {
				return;

			}
			scan[i] = getDistanceMeasurement();
			if (i < scan.length - 1) {
				neckMotor.rotate(SCAN_ANGLE);
			}

		}
		neckMotor.rotate(-360 + SCAN_ANGLE);
	}

	public static int findFarthest(float[] distances) {
		float maxDistance = 0;
		int index = 0;
		String output = "";

		for (int i = 0; i < distances.length; i++) {
			output = i + ": " + distances[i];
			LCD.clear();
			LCD.drawString(output, 0, 0);
			Delay.msDelay(2000);
			if (distances[i] >= maxDistance) {
				maxDistance = distances[i];
				index = i;
			}
		}
		return index;
	}

	public static int findNearest(float[] distances) {
		float minDistance = Float.MAX_VALUE;
		int index = 0;

		for (int i = 0; i < distances.length; i++) {
			if (distances[i] <= minDistance) {
				minDistance = distances[i];
				index = i;
			}
		}
		return index;
	}

	public static float getDistanceMeasurement() {
		sample = new float[sampleProvider.sampleSize()];
		sampleProvider.fetchSample(sample, 0);
		String output = "curDist: " + sample[0];
		LCD.clear();
		LCD.drawString(output, 0, 0);

		return sample[0];
	}

}

class VisionRunner implements Runnable {
	@Override
	public void run() {
		while (!Button.ENTER.isDown()) {
			ChaseClosestMultiThreaded.scanSurroundings();
		}
	}
}

