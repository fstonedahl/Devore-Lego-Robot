package testdrive;

import java.io.*;
import java.util.ArrayList;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class VisionConeCalibration {

	public static void main(String[] args) {
		EV3UltrasonicSensor sensor = new EV3UltrasonicSensor(SensorPort.S1);
		sensor.enable();
		
		SampleProvider sp = sensor.getDistanceMode();
		
		float[] sample = new float[sp.sampleSize()];
		
		LCD.drawString("Push button to start/end data collection", 0, 4);
		
		while(!Button.ENTER.isDown()){
			Delay.msDelay(10);
		}

		ArrayList<Float> sightRangeData = new ArrayList<Float>();
		while(!Button.ENTER.isDown()){
			sp.fetchSample(sample, 0);
			float distance = sample[0];
			sightRangeData.add(distance);
			Delay.msDelay(100);
		}
		
		try {
			PrintWriter out = new PrintWriter(new FileWriter("output.txt"));
			for (int i = 0; i < sightRangeData.size(); i++){
				out.printf("%.2f%n", i);
			}
			out.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Sound.beepSequence();
		}
		sensor.close();
	}

}
