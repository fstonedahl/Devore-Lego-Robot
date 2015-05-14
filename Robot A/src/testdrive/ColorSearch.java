package testdrive;

import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.navigation.DifferentialPilot;

public class ColorSearch {

	static DifferentialPilot robot;
	static RegulatedMotor leftMotor = Motor.C;
	static RegulatedMotor rightMotor = Motor.B;
	static EV3ColorSensor sensor;
	
	static boolean colorFound = false;
	static int moveMultiplier = 1;
	static int previousMove;
	
	static int length;
	static int width;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		robot = new DifferentialPilot(5.5, 14.5, leftMotor, rightMotor, true);
		robot.setTravelSpeed(30);
		robot.setRotateSpeed(60);
		sensor = new EV3ColorSensor(SensorPort.S4);
		
		//Dimensions of robot in cm
		length = 8;
		width = 5;

		previousMove = 1;
		findColor(1);
	}
	
	
	public static int findColor(int move){
		if(colorFound){
			return 0;
		}else{
			//Executes current move
			if(move == 0){
				robot.rotate(-100, true);
			}else if(move == 1){
				robot.travel(length*moveMultiplier, true);
			}else if(move == -1){
				robot.travel(width*moveMultiplier, true);
				moveMultiplier = moveMultiplier + 1;
			}
			//Change next move
			if(move == 0 && previousMove == 1){
				move = -1;
			}else if(move == 0 && previousMove == -1){
				move = 1;
			}else{
				move = 0;
			}
			LCD.drawString("Next Move: " + move, 0, 0);
			LCD.drawString("Multiplier: " + moveMultiplier, 0, 1);
			//Checks for color during move
			while(robot.isMoving()){
				if(sensor.getColorID() == 3){
					colorFound = true;
					robot.stop();
				}
			}
			LCD.clear();
			//Re-Calls Method
			return findColor(move);
		}
		
	}

}
