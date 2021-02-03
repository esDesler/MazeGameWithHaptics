package Haptics;

public class GameToGloveIntensity extends GameToGloveAPI {

    public GameToGloveIntensity() {
        super();
    }

    @Override
    public void updateUpIntensity(double distance, double outOf) {
        motors.get(up).setMotorStatus(1);

        int vibrationIntensity = (int) (distance / outOf * 100);
        if (5 < vibrationIntensity && vibrationIntensity < 40) vibrationIntensity = 40;
        motors.get(up).setVibrationIntensity(vibrationIntensity);
    }

    @Override
    public void updateDownIntensity(double distance, double outOf) {
        motors.get(down).setMotorStatus(1);

        int vibrationIntensity = (int) (distance / outOf * 100);
        if (5 < vibrationIntensity && vibrationIntensity < 40) vibrationIntensity = 40;
        motors.get(down).setVibrationIntensity(vibrationIntensity);
    }

    @Override
    public void updateLeftIntensity(double distance, double outOf) {
        motors.get(left).setMotorStatus(1);

        int vibrationIntensity = (int) (distance / outOf * 100);
        if (5 < vibrationIntensity && vibrationIntensity < 40) vibrationIntensity = 40;
        motors.get(left).setVibrationIntensity(vibrationIntensity);
    }

    @Override
    public void updateRightIntensity(double distance, double outOf) {
        motors.get(right).setMotorStatus(1);

        int vibrationIntensity = (int) (distance / outOf * 100);
        if (5 < vibrationIntensity && vibrationIntensity < 40) vibrationIntensity = 40;
        motors.get(right).setVibrationIntensity(vibrationIntensity);
    }
}
