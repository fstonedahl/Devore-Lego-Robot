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

	
	public class IntergratedBehavior {
		static DifferentialPilot robot;
		static RegulatedMotor leftMotor = Motor.C;
		static RegulatedMotor rightMotor = Motor.B;
		static RegulatedMotor frontMotor = Motor.A;
		static EV3UltrasonicSensor sensor;
		static EV3TouchSensor sensorBump; 
		static EV3ColorSensor sensorColor;
		static SampleProvider bumpSampleProvider;
		static SampleProvider distanceSampleProvider;
		static SampleProvider colorSampleProvider;
		static float[] scan;
		static float[] distanceSample;
		static float[] bumpSample;

		public static void main(String[] args) {
			// Sets up Pilot for robot and sensor for robot
			robot = new DifferentialPilot(5.4, 14.5, leftMotor, rightMotor, false);
			sensor = new EV3UltrasonicSensor(SensorPort.S1);
			sensorBump = new EV3TouchSensor(SensorPort.S2);
			sensorColor = new EV3ColorSensor(SensorPort.S4);
			scan = new float[12];
			
			robot.setTravelSpeed(30);
			robot.setAcceleration(60);
			robot.setRotateSpeed(60);
			robot.reset();
			
			run();
		}
		
		public static void run(){
			bumpSampleProvider = sensorBump.getTouchMode();
			bumpSample = new float[bumpSampleProvider.sampleSize()];
			bumpSampleProvider.fetchSample(bumpSample, 0);
			
			//looking for white
			while(sensorColor.getColorID() != 6&&!Button.ENTER.isDown()){
				scanSurroundings();
				int index = findFarthest(scan);
				int angle = (index* -30) + 180;
				robot.rotate(angle);
				robot.travel(40, true);
				while (robot.isMoving()) {
					bumpSampleProvider.fetchSample(bumpSample, 0);
					if(bumpSample[0] == 1){
						robot.stop();
						robot.travel(-10);
						Random r = new Random();
						int degree = (r.nextInt(135)+45);
						robot.rotate(degree);
					}					
				}
			}
			robot.stop();
			Sound.beepSequenceUp();
			
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
		
		public static float getDistanceMeasurement(){
			distanceSampleProvider = sensor.getDistanceMode();
			distanceSample = new float[distanceSampleProvider.sampleSize()];
			distanceSampleProvider.fetchSample(distanceSample, 0);
			String output = "curDist: " + distanceSample[0];
			LCD.clear();
			LCD.drawString(output, 0, 0);
			//Delay.msDelay(2000);
			
			return distanceSample[0];
		}
		

	
}
