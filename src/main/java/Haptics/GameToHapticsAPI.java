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
        upMotor.setMotorStatus(1);
        upMotor.setOnTime(500);
        upMotor.setOffTime(100);
        downMotor = new Motor("M1");
        downMotor.setMotorStatus(1);
        downMotor.setOnTime(500);
        downMotor.setOffTime(100);
        leftMotor = new Motor("M2");
        leftMotor.setMotorStatus(1);
        leftMotor.setOnTime(500);
        leftMotor.setOffTime(100);
        rightMotor = new Motor("M3");
        rightMotor.setMotorStatus(1);
        rightMotor.setOnTime(500);
        rightMotor.setOffTime(100);


        littleFingerMotor = new Motor("M4");
        ringFingerMotor = new Motor("M5");
        middleFingerMotor = new Motor("M6");
        indexFingerMotor = new Motor("M7");
        thumbMotor = new Motor("M8");
        extraMotor = new Motor("M9");
    }

    public void up(double distance, double outOf) {
        updateMotorSettings(upMotor, (int) (100 - distance / outOf * 100));
        upMotor.sendToArduino(serialConnector);
    }

    public void down(double distance, double outOf) {
        updateMotorSettings(downMotor, (int) (100 - distance / outOf * 100));
        downMotor.sendToArduino(serialConnector);
    }

    public void left(double distance, double outOf) {
        updateMotorSettings(leftMotor, (int) (100 - distance / outOf * 100));
        leftMotor.sendToArduino(serialConnector);
    }

    public void right(double distance, double outOf) {
        updateMotorSettings(rightMotor, (int) (100 - distance / outOf * 100));
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

    private void updateMotorSettings(Motor motor, int vibrationIntensity) {
        motor.setMotorStatus(1);
        motor.setVibrationIntensity(vibrationIntensity);
        motor.setOnTime(500);
        motor.setOffTime(100);
    }

    private void turnOfDescriptiveMotors() {
        littleFingerMotor.setMotorStatus(0);
        ringFingerMotor.setMotorStatus(0);
        middleFingerMotor.setMotorStatus(0);
        indexFingerMotor.setMotorStatus(0);
        thumbMotor.setMotorStatus(0);
    }

    public void updateUpIntensity(double distance, double outOf) {
        upMotor.setVibrationIntensity((int) (100 - distance / outOf * 100));
    }

    public void updateDownIntensity(double distance, double outOf) {
        downMotor.setVibrationIntensity((int) (100 - distance / outOf*100));
    }

    public void updateLeftIntensity(double distance, double outOf) {
        leftMotor.setVibrationIntensity((int) (100 - distance / outOf*100));
    }

    public void updateRightIntensity(double distance, double outOf) {
        rightMotor.setVibrationIntensity((int) (100 - distance / outOf*100));
    }

    public void outputMotorInformationToArduino() {
        String toSend = "";
        toSend += upMotor.generateDataPackage();
        toSend += downMotor.generateDataPackage();
        toSend += leftMotor.generateDataPackage();
        toSend += rightMotor.generateDataPackage() + "\n";
        serialConnector.write(toSend);
    }
}
