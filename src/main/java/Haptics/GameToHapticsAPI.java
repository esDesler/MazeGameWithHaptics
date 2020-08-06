package Haptics;

import java.util.ArrayList;

public class GameToHapticsAPI {

    private final int up = 0, down = 1, left = 2,
            right = 3, littleFinger = 4, ringFinger = 5,
            middleFinger = 6, indexFinger = 7, thumb = 8,
            extra = 9;

    private SerialConnector serialConnector;
    private ArrayList<Motor> motors;

    public GameToHapticsAPI() {
        initializeMotors();
        initializeSerialConnection();
        System.out.println("Java motor objects has been created with initial settings");
    }

    private void initializeSerialConnection() {
        serialConnector = new SerialConnector();
    }

    private void initializeMotors() {
        motors = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            motors.add(new Motor("M" + i));
        }
    }

    private void turnOffDescriptiveMotors() {
        motors.get(littleFinger).setMotorStatus(0);
        motors.get(ringFinger).setMotorStatus(0);
        motors.get(middleFinger).setMotorStatus(0);
        motors.get(indexFinger).setMotorStatus(0);
        motors.get(thumb).setMotorStatus(0);
    }

    public void updateUpIntensity(double distance, double outOf) {
        motors.get(up).setMotorStatus(1);
        motors.get(up).setVibrationIntensity((int) (100 - distance / outOf * 100));
    }

    public void updateDownIntensity(double distance, double outOf) {
        motors.get(down).setMotorStatus(1);
        motors.get(down).setVibrationIntensity((int) (100 - distance / outOf*100));
    }

    public void updateLeftIntensity(double distance, double outOf) {
        motors.get(left).setMotorStatus(1);
        motors.get(left).setVibrationIntensity((int) (100 - distance / outOf*100));
    }

    public void updateRightIntensity(double distance, double outOf) {
        motors.get(right).setMotorStatus(1);
        motors.get(right).setVibrationIntensity((int) (100 - distance / outOf*100));
    }

    public void outputMotorInformationToArduino() {
        StringBuilder motorInformation = new StringBuilder();
        for (Motor motor : motors) {
            motorInformation.append(motor.generateDataPackage());
        }
        serialConnector.write(motorInformation.toString());
    }

    public void thumb() {
        turnOffDescriptiveMotors();
        motors.get(thumb).setMotorStatus(1);
        motors.get(thumb).setVibrationIntensity(50);
    }

    public void indexFinger() {
        turnOffDescriptiveMotors();
        motors.get(indexFinger).setMotorStatus(1);
        motors.get(indexFinger).setVibrationIntensity(50);
    }

    public void middleFinger() {
        turnOffDescriptiveMotors();
        motors.get(middleFinger).setMotorStatus(1);
        motors.get(middleFinger).setVibrationIntensity(50);
    }

    public void ringFinger() {
        turnOffDescriptiveMotors();
        motors.get(ringFinger).setMotorStatus(1);
        motors.get(ringFinger).setVibrationIntensity(50);
    }

    public void littleFinger() {
        turnOffDescriptiveMotors();
        motors.get(littleFinger).setMotorStatus(1);
        motors.get(littleFinger).setVibrationIntensity(50);
    }
}
