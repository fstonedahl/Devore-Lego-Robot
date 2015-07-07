package testdrive;

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
		float lastDistance = 0;

		while(!Button.ENTER.isDown()){
			LCD.clear();
			sp.fetchSample(sample, 0);
			float distance = sample[0];
			LCD.drawString(String.format("Dist: %.2f", distance), 0, 4);
			if ((lastDistance > 2.2 && distance <= 2.2)) {
				//Sound.beep();
				Sound.playNote(Sound.XYLOPHONE, 500, 100);
			} else if ((lastDistance <= 2.2 && distance > 2.2)) {				
				Sound.playNote(Sound.FLUTE, 700, 100);
			}
			lastDistance = distance;
			if (distance > 2.2) {
				Button.LEDPattern(1);				
			} else {
				Button.LEDPattern(2);
			}
			Delay.msDelay(100);
		}
		
	}

}
