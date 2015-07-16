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
import lejos.utility.TextMenu;

public class VisionConeCalibration {

	static EV3UltrasonicSensor sensor;
	static SampleProvider sp;

	public static void main(String[] args) {

		sensor = new EV3UltrasonicSensor(SensorPort.S1);
		sensor.enable();
		sp = sensor.getDistanceMode();

		startRun(75, true);
		startRun(75, false);
		startRun(100, true);
		startRun(100, false);
		startRun(125, true);
		startRun(125, false);
		startRun(150, true);
		startRun(150, false);

	}

	public static void startRun(float lineDistance, boolean movingRight) {
		float placeAtDist = lineDistance - 20;
		LCD.clear();
		LCD.drawString("Place robot at " + placeAtDist + "cm", 0, 1);
		if (movingRight) {
			LCD.drawString("Target moves RIGHT", 0, 2);
		} else {
			LCD.drawString("Target moves LEFT", 0, 2);
		}		
		LCD.drawString("Push button to", 0, 4);
		LCD.drawString("start sensing", 0, 5);
		Delay.msDelay(100);
		while (!Button.ENTER.isDown()) {
			Delay.msDelay(10);
			if (Button.ESCAPE.isDown()) {
				System.exit(0);
			}
		}

		// wait for the button to come back up
		while (Button.ENTER.isDown()) {
			Delay.msDelay(10);
		}
		LCD.clear();
		LCD.drawString(((int) lineDistance) + ":" +(movingRight?"RIGHT":"LEFT"), 0, 0);
		LCD.drawString("Push button to", 0, 4);
		LCD.drawString("end sensing", 0, 5);
		collectAndPrintData(lineDistance, movingRight);
	}

	public static void collectAndPrintData(float lineDistance, boolean movingRight){

		
			float[] sample = new float[sp.sampleSize()];

			ArrayList<Float> sightRangeData = new ArrayList<Float>();

			while(!Button.ENTER.isDown()){
				if (Button.ESCAPE.isDown()) {
					System.exit(0);
				}
				sp.fetchSample(sample, 0);
				float distance = sample[0];
				sightRangeData.add(distance);
				Delay.msDelay(10);
			}
			LCD.clear();
			int choice = new TextMenu(new String[] {"Accept", "Discard"}).select();
			LCD.clear();
			if (choice == 0) {
				LCD.drawString("saving...", 0, 0);
				try {
					PrintWriter out = new PrintWriter(new FileWriter("range_data.txt", true));
					out.print("\n");
					if(movingRight == true){
						out.print("LineDistance:"+ lineDistance +"\nDirection:right\n");
					}else{
						out.print("LineDistance:"+ lineDistance +"\nDirection:left\n");
					}

					for (int i = 0; i < sightRangeData.size(); i++){
						out.printf("%.2f ", sightRangeData.get(i));
					}
					out.print("\n");
					out.close();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Sound.beepSequence();
				}
			} else {
				startRun(lineDistance, movingRight);
			}
			Delay.msDelay(500);
			while (Button.ENTER.isDown()) { // wait for button to come back up!
				Delay.msDelay(10);
			}
	}
}
