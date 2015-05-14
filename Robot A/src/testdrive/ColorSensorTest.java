package testdrive;

import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class ColorSensorTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EV3ColorSensor sensor = new EV3ColorSensor(SensorPort.S4);
		
		int colorId = sensor.getColorID();
		
		LCD.clear();
		LCD.drawString("Color Id: " + colorId, 0 , 0);
		Delay.msDelay(5000);
	}

}
