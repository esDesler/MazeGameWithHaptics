package Haptics;

public class GameToHapticsAPI {

    private Motor upMotor, downMotor, leftMotor,
            rightMotor, littleFingerMotor, ringFingerMotor,
            middleFingerMotor, indexFingerMotor, thumbMotor,
            extraMotor;

    private SerialConnector serialConnector;

    public GameToHapticsAPI() {
        initializeMotors();
        initializeSerialConnection();
        System.out.println("Java motor objects has been created with initial settings");
    }

    private void initializeSerialConnection() {
        serialConnector = new SerialConnector();
    }

    private void initializeMotors() {
        upMotor = new Motor("M0");
        downMotor = new Motor("M1");
        leftMotor = new Motor("M2");
        rightMotor = new Motor("M3");
        littleFingerMotor = new Motor("M4");
        ringFingerMotor = new Motor("M5");
        middleFingerMotor = new Motor("M6");
        indexFingerMotor = new Motor("M7");
        thumbMotor = new Motor("M8");
        extraMotor = new Motor("M9");
    }

    public void up(double distance, double outOf) {
        updateMotorSettings(upMotor, distance / outOf * 100);
        upMotor.sendToArduino(serialConnector);
    }

    public void down(double distance, double outOf) {
        updateMotorSettings(downMotor, distance / outOf * 100);
        downMotor.sendToArduino(serialConnector);
    }

    public void left(double distance, double outOf) {
        updateMotorSettings(leftMotor, distance / outOf * 100);
        leftMotor.sendToArduino(serialConnector);
    }

    public void right(double distance, double outOf) {
        updateMotorSettings(rightMotor, distance / outOf * 100);
        rightMotor.sendToArduino(serialConnector);
    }

    public void littleFinger() {
        turnOfDescriptiveMotors();
        updateMotorSettings(littleFingerMotor, 50);
        littleFingerMotor.sendToArduino(serialConnector);
    }

    public void ringFinger() {
        turnOfDescriptiveMotors();
        updateMotorSettings(ringFingerMotor, 50);
        ringFingerMotor.sendToArduino(serialConnector);
    }

    public void middleFinger() {
        turnOfDescriptiveMotors();
        updateMotorSettings(middleFingerMotor, 50);
        middleFingerMotor.sendToArduino(serialConnector);
    }

    public void indexFinger() {
        turnOfDescriptiveMotors();
        updateMotorSettings(indexFingerMotor, 50);
        indexFingerMotor.sendToArduino(serialConnector);
    }

    public void thumb() {
        turnOfDescriptiveMotors();
        updateMotorSettings(thumbMotor, 50);
        thumbMotor.sendToArduino(serialConnector);
    }

    private void updateMotorSettings(Motor motor, double vibrationIntensity) {
        motor.setMotorStatus(1);
        motor.setVibrationIntensity(vibrationIntensity);
        motor.setOnTime(100);
        motor.setOffTime(0);
    }

    private void turnOfDescriptiveMotors() {
        littleFingerMotor.setMotorStatus(0);
        ringFingerMotor.setMotorStatus(0);
        middleFingerMotor.setMotorStatus(0);
        indexFingerMotor.setMotorStatus(0);
        thumbMotor.setMotorStatus(0);
    }
}
