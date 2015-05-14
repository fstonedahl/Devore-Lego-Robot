package testdrive;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class DistanceSensorTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EV3UltrasonicSensor sensor = new EV3UltrasonicSensor(SensorPort.S1);
		sensor.enable();
		
		SampleProvider sp = sensor.getDistanceMode();
		
		float[] sample = new float[sp.sampleSize()];
		sp.fetchSample(sample, 0);
		LCD.clear();
		LCD.drawString("Distance: " + sample[0], 0, 0);
		Delay.msDelay(5000);
		/*while(!Button.ENTER.isDown()){
			sp.fetchSample(sample, 0);
			distance = (int)sample[0];
			LCD.drawString("IR: " + distance, 0, 0);
			Delay.msDelay(3000);
		}*/
		
	}

}
