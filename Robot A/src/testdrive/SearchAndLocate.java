package testdrive;

import java.util.Random;

import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.utility.Delay;
import lejos.hardware.motor.Motor;
import lejos.robotics.RegulatedMotor;
import lejos.hardware.sensor.EV3TouchSensor;

public class SearchAndLocate {
	
	final static int targetColor = 6;

	static DifferentialPilot robot;
	static RegulatedMotor leftMotor = Motor.C;
	static RegulatedMotor rightMotor = Motor.B;
	static EV3TouchSensor sensor;
	static SampleProvider bumpSampleProvider;
	static float[] bumpSample;
	
	public static void main(String[] args){
		EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S4);
		EV3TouchSensor touchSensor = new EV3TouchSensor(SensorPort.S2);
		bumpSampleProvider = touchSensor.getTouchMode();
		bumpSample = new float[bumpSampleProvider.sampleSize()];
		Random rand = new Random();

		int currentColor = colorSensor.getColorID();
		robot = new DifferentialPilot(5.5, 14.5, leftMotor, rightMotor, false);
		robot.setTravelSpeed(20);
		robot.setRotateSpeed(60);

		while (currentColor != targetColor && !Button.ENTER.isDown()) {
			robot.forward();			
			Delay.msDelay(100);
				
			bumpSampleProvider.fetchSample(bumpSample, 0);
			if(bumpSample[0] == 1){
				robot.stop();
				robot.travel(-10);
				int degree = (rand.nextInt(180)-90);
				robot.rotate(degree);
				robot.forward();
			}

			LCD.clear();
			currentColor = colorSensor.getColorID();
			String colorName = getColorName(currentColor);
			LCD.drawString("Color Id: " + colorName, 0 , 0);
		}
		robot.stop();
		victoryMessage();
		
	}
	
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
	
	public static void victoryMessage() {
		GraphicsLCD g = BrickFinder.getDefault().getGraphicsLCD();
        final int SW = g.getWidth();
        final int SH = g.getHeight();
        Button.LEDPattern(4);
        Sound.beepSequenceUp();
        g.setFont(Font.getSmallFont());
        g.drawString("Found It!", SW/2, SH/2, GraphicsLCD.BASELINE|GraphicsLCD.HCENTER);
        Button.LEDPattern(3);
        Delay.msDelay(400);
        Button.LEDPattern(5);
        g.clear();
        g.refresh();
        Delay.msDelay(500);
        Button.LEDPattern(0);
	}
	
	

}
