package UserSettings;

public class UserSettings {

    private static boolean muteMusic; // On/Off
    private static double vibrationIntensity = 2.0; // From 1-2 with 0.1 adjustments


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
        if (vibrationIntensity < 2) {
            vibrationIntensity += 0.1;
        }
    }

    public static void incrementIntensity() {
        if (vibrationIntensity > 1) {
            vibrationIntensity -= 0.1;
        }
    }

    public static void setVibrationIntensity(int vibrationIntensity) {
        UserSettings.vibrationIntensity = vibrationIntensity;
    }


}
