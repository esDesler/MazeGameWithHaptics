package Haptics;

public class GameToHapticsIntensity extends GameToHapticsAPI {

    public GameToHapticsIntensity() {
        super();
    }

    @Override
    public void updateUpIntensity(double distance, double outOf) {
        motors.get(up).setMotorStatus(1);
        motors.get(up).setVibrationIntensity((int) (distance / outOf * 100));
    }

    @Override
    public void updateDownIntensity(double distance, double outOf) {
        motors.get(down).setMotorStatus(1);
        motors.get(down).setVibrationIntensity((int) (distance / outOf * 100));
    }

    @Override
    public void updateLeftIntensity(double distance, double outOf) {
        motors.get(left).setMotorStatus(1);
        motors.get(left).setVibrationIntensity((int) (distance / outOf * 100));
    }

    @Override
    public void updateRightIntensity(double distance, double outOf) {
        motors.get(right).setMotorStatus(1);
        motors.get(right).setVibrationIntensity((int) (distance / outOf * 100));
    }
}
