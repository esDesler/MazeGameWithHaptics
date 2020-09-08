package Haptics;

import java.util.ArrayList;

public abstract class GameToHapticsAPI {

    protected final int up = 0;
    protected final int down = 1;
    protected final int left = 2;
    protected final int right = 3;
    private final int thumb = 4;
    private final int indexFinger = 5;
    private final int middleFinger = 6;
    private final int ringFinger = 7;
    private final int littleFinger = 8;
    private final int extra = 9;

    private SerialConnector serialConnector;
    protected ArrayList<Motor> motors;

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

    public void turnOffDescriptiveMotors() {
        /*motors.get(thumb).setMotorStatus(0);
        motors.get(indexFinger).setMotorStatus(0);
        motors.get(middleFinger).setMotorStatus(0);
        motors.get(ringFinger).setMotorStatus(0);
        motors.get(littleFinger).setMotorStatus(0);*/
    }

    public abstract void updateUpIntensity(double distance, double outOf);

    public abstract void updateDownIntensity(double distance, double outOf);

    public abstract void updateLeftIntensity(double distance, double outOf);

    public abstract void updateRightIntensity(double distance, double outOf);

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
