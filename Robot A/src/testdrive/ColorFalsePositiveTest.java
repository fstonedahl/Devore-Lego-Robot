package testdrive;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.utility.Delay;

public class ColorFalsePositiveTest {

	/*
public static final int	BLACK	7
public static final int	BLUE	2
public static final int	CYAN	12
public static final int	DARK_GRAY	11
public static final int	GRAY	9
public static final int	GREEN	1
public static final int	LIGHT_GRAY	10
public static final int	MAGENTA	4
public static final int	NONE	-1
public static final int	ORANGE	5
public static final int	PINK	8
public static final int	RED	0
public static final int	WHITE	6
public static final int	YELLOW	3
	 
	 */
	public static String getColorName(int colorId) {
		switch (colorId) {
			case -1: return "NONE";
			case 0: return "RED";
			case 1: return "GREEN";
			case 2: return "BLUE";
			case 3: return "YELLOW";
			case 4: return "MAGENTA";
			case 5: return "ORANGE";
			case 6: return "WHITE";
			case 7: return "BLACK";
			case 8: return "PINK";
			case 9: return "GRAY";
			case 10: return "LIGHT_GRAY";
			case 11: return "DARK_GRAY";
		}
		return "OTHER?";
	}
	
	
	static DifferentialPilot robot;
	static RegulatedMotor leftMotor = Motor.C;
	static RegulatedMotor rightMotor = Motor.B;
	static RegulatedMotor frontMotor = Motor.A;
	static EV3UltrasonicSensor sensor;
	static EV3TouchSensor sensorBump; 
	static EV3ColorSensor sensorColor;
	static SampleProvider bumpSampleProvider;
	static SampleProvider colorSampleProvider;
	static float[] bumpSample;

	public static void main(String[] args) {
		robot = new DifferentialPilot(5.4, 14.5, leftMotor, rightMotor, false);
		sensorBump = new EV3TouchSensor(SensorPort.S2);
		sensorColor = new EV3ColorSensor(SensorPort.S4);
		bumpSampleProvider = sensorBump.getTouchMode();
		bumpSample = new float[bumpSampleProvider.sampleSize()];
		
		robot.setTravelSpeed(30);
		robot.setAcceleration(60);
		robot.setRotateSpeed(60);
		robot.reset();
		
		robot.forward();
		
		while(!Button.ENTER.isDown()){
			LCD.clear();
			int colorId = sensorColor.getColorID();
			String colorName = getColorName(colorId);
			LCD.drawString("Color Id: " + colorName, 0 , 0);
			if (colorName.equals("WHITE")) {
				Sound.beepSequence();
				break;
			}
			else if (colorName.equals("RED")) {
				Sound.beepSequence();
				break;
			}
			Delay.msDelay(10);
			bumpSampleProvider.fetchSample(bumpSample, 0);
			if (bumpSample[0] == 1) {
				robot.stop();
				robot.travel(-10);
				robot.rotate(180);
				robot.forward();
			}
		}
		robot.stop();

		while(!Button.ENTER.isDown()){
			Delay.msDelay(100);
		}
	}

}
