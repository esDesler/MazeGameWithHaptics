package Haptics;

public class Motor {

    private String motorId; // from M0 - M9
    private int motorStatus; // 0 / 1 for off / on
    private int vibrationIntensity; // from 0 - 100
    private int onTime; // in ms
    private int offTime; // in ms

    public Motor(String motorId) {
        this.motorId = motorId;
        this.motorStatus = 0;
        this.vibrationIntensity = 0;
        this.onTime = 900; // Default value
        this.offTime = 100; // Default value
    }

    public String generateDataPackage() {
        return motorId + "," + motorStatus + "," + vibrationIntensity + "," + onTime + "," + offTime + ";";
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

    public void setVibrationIntensity(int vibrationIntensity) {
        this.vibrationIntensity = vibrationIntensity;
    }

    public double getOnTime() {
        return onTime;
    }

    public void setOnTime(int onTime) {
        this.onTime = onTime;
    }

    public double getOffTime() {
        return offTime;
    }

    public void setOffTime(int offTime) {
        this.offTime = offTime;
    }

    public String getMotorId() {
        return motorId;
    }
}
