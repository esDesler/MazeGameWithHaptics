package Haptics;

public class GameToHapticsInterval extends GameToHapticsAPI {

    public GameToHapticsInterval() {
        super();
    }

    @Override
    public void updateUpIntensity(double distance, double outOf) {
        updateIntensity(distance, outOf, up);
    }

    private void updateIntensity(double distance, double outOf, int direction) {
        motors.get(direction).setMotorStatus(1);
        motors.get(direction).setVibrationIntensity(40);
        int interval = (int) (500 - distance / outOf * 500);
        interval = fitInterval(interval);
        motors.get(direction).setOnTime(interval);
        motors.get(direction).setOffTime(interval);
    }

    private int fitInterval(int interval) {
        if (interval < 100) {
            return 62;
        } else if (interval < 200) {
            return 125;
        } else if (interval < 300) {
            return 250;
        } else {
            return 500;
        }
    }

    @Override
    public void updateDownIntensity(double distance, double outOf) {
        updateIntensity(distance, outOf, down);
    }

    @Override
    public void updateLeftIntensity(double distance, double outOf) {
        updateIntensity(distance, outOf, left);
    }

    @Override
    public void updateRightIntensity(double distance, double outOf) {
        updateIntensity(distance, outOf, right);
    }
}
