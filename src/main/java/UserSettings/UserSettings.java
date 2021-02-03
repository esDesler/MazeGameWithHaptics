package UserSettings;

public class UserSettings {

    private static boolean muteMusic; // On/Off
    private static double vibrationIntensity = 1.0; // From 1-3 with 0.2 adjustments


    public static boolean isMuteMusic() {
        return muteMusic;
    }

    public static void setMuteMusic(boolean muteMusic) {
        UserSettings.muteMusic = muteMusic;
    }

    public static double getVibrationIntensity() {
        return vibrationIntensity;
    }

    public static void decrementIntensity() {
        if (vibrationIntensity < 3) {
            vibrationIntensity += 0.2;
        }
    }

    public static void incrementIntensity() {
        if (vibrationIntensity > 1) {
            vibrationIntensity -= 0.2;
        }
    }

    public static void setVibrationIntensity(int vibrationIntensity) {
        UserSettings.vibrationIntensity = vibrationIntensity;
    }


}
