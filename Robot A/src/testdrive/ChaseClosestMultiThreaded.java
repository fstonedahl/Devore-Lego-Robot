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
	static final int SCAN_ANGLE = 45;

	static DifferentialPilot robot;
	static RegulatedMotor leftMotor = Motor.C;
	static RegulatedMotor rightMotor = Motor.B;
	static VisionRunner visionRunner;
	
	public static void main(String[] args) {
		// Sets up Pilot for robot and sensor for robot
		robot = new DifferentialPilot(5.5, 14.5, leftMotor, rightMotor, false);
		visionRunner = new VisionRunner(SCAN_ANGLE); 
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
			int index = findNearest();
			int angle = index * -SCAN_ANGLE;
			if (angle < -180) {
				angle += 360;
			}
			// frontMotor.rotate(index*30);
			robot.rotate(angle);
			visionRunner.rotateScanArray(index);
			robot.travel(10);
			Delay.msDelay(1000);
			/*
			 * while(forward){ robot.forward(); Delay.msDelay(500); float temp =
			 * getDistanceMeasurement(); if(temp <= 0.4){ forward = false;
			 * robot.stop(); } }
			 */
			// beginDrive();
		}
	}

//
//	public static int findFarthest(float[] distances) {
//		float maxDistance = 0;
//		int index = 0;
//		String output = "";
//
//		for (int i = 0; i < distances.length; i++) {
//			output = i + ": " + distances[i];
//			LCD.clear();
//			LCD.drawString(output, 0, 0);
//			Delay.msDelay(2000);
//			if (distances[i] >= maxDistance) {
//				maxDistance = distances[i];
//				index = i;
//			}
//		}
//		return index;
//	}

	public static int findNearest() {
		float minDistance = Float.MAX_VALUE;
		int index = 0;

		for (int i = 0; i < visionRunner.getScanArraySize(); i++) {
			float curDistance = visionRunner.getScannedDistanceAtIndex(i);
			if (curDistance <= minDistance) {
				minDistance = curDistance;
				index = i;
			}
		}
		return index;
	}


}

class VisionRunner implements Runnable {
	private float[] scan;
	private int scanAngle;
	private RegulatedMotor neckMotor = Motor.A;
	private EV3UltrasonicSensor distanceSensor;
	private SampleProvider sampleProvider;
	private float[] sample;

	
	public VisionRunner(int scanAngle) {
		distanceSensor = new EV3UltrasonicSensor(SensorPort.S1);
		sampleProvider = distanceSensor.getDistanceMode();
		scan = new float[360 / scanAngle];
		this.scanAngle = scanAngle;
		for (int i = 0; i < scan.length; i++) {
			scan[i] = 1000; // large value for things we haven't seen yet.
		}
	}

	@Override
	public void run() {
		while (!Button.ENTER.isDown()) {
			scanSurroundings();
		}
	}
	
	public synchronized int getScanArraySize() {
		return scan.length;
	}
	public synchronized float getScannedDistanceAtIndex(int i) {
		return scan[i];
	}
	private synchronized void setScannedDistanceAtIndex(int i, float newVal)
	{
		scan[i] = newVal;
	}
	public synchronized void rotateScanArray(int offset) {
		float[] newScan = new float[scan.length];
		for (int i = 0; i < newScan.length; i++) {
			newScan[i] = scan[(i+2) % scan.length];
		}
		scan = newScan;
	}
	private void scanSurroundings() {

		for (int i = 0; i < getScanArraySize(); i++) {
			if (Button.ENTER.isDown()) {
				return;

			}
			setScannedDistanceAtIndex(i, getDistanceMeasurement());
			if (i < getScanArraySize() - 1) {
				neckMotor.rotate(scanAngle);
			}

		}
		neckMotor.rotate(-360 + scanAngle);
	}

	public float getDistanceMeasurement() {
		sample = new float[sampleProvider.sampleSize()];
		sampleProvider.fetchSample(sample, 0);
		//String output = "curDist: " + sample[0];
		//LCD.clear();
		//LCD.drawString(output, 0, 0);

		return sample[0];
	}

	
}

