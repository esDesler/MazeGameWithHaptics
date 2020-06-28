package Haptics;

public class Motor {

    private String motorId; // from M0 - M9
    private int motorStatus; // 0 / 1 for off / on
    private double vibrationIntensity; // from 0 - 100
    private double onTime; // in ms
    private double offTime; // in ms

    public Motor(String motorId) {
        this.motorId = motorId;
        this.motorStatus = 0;
        this.vibrationIntensity = 0;
        this.onTime = 0;
        this.offTime = 0;
    }

    public void sendToArduino(SerialConnector serialConnector) {
        serialConnector.write(generateDataPackage());
    }

    private String generateDataPackage() {
        return motorId + "," + motorStatus + "," + vibrationIntensity + "," + onTime + "," + offTime;
    }

    public int getMotorStatus() {
        return motorStatus;
    }

    public void setMotorStatus(int motorStatus) {
        this.motorStatus = motorStatus;
    }

    public double getVibrationIntensity() {
        return vibrationIntensity;
    }

    public void setVibrationIntensity(double vibrationIntensity) {
        this.vibrationIntensity = vibrationIntensity;
    }

    public double getOnTime() {
        return onTime;
    }

    public void setOnTime(double onTime) {
        this.onTime = onTime;
    }

    public double getOffTime() {
        return offTime;
    }

    public void setOffTime(double offTime) {
        this.offTime = offTime;
    }

    public String getMotorId() {
        return motorId;
    }
}
