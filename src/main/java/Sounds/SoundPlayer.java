package Sounds;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class SoundPlayer {

    private static long lastTimeHard =  0;
    private static long lastTimeSoft = 0;
    private static long lastTimeBomb = 0;

    private static final AudioClip bombExplosion = new AudioClip(SoundPlayer.class.getResource("/resources/sound/bombExplosion.wav").toString());
    private static final AudioClip bombPlacement = new AudioClip(SoundPlayer.class.getResource("/resources/sound/bombPlacement.wav").toString());
    private static final AudioClip powerUp = new AudioClip(SoundPlayer.class.getResource("/resources/sound/powerUp.wav").toString());
    private static final AudioClip playerDied = new AudioClip(SoundPlayer.class.getResource("/resources/sound/playerDied.wav").toString());
    private static final AudioClip bombKicked = new AudioClip(SoundPlayer.class.getResource("/resources/sound/bombKicked.wav").toString());
    private static final AudioClip hardWallCollision = new AudioClip(SoundPlayer.class.getResource("/resources/sound/hardWallCollision.wav").toString());
    private static final AudioClip softWallCollision = new AudioClip(SoundPlayer.class.getResource("/resources/sound/softWallCollision.wav").toString());
    private static final AudioClip bombCollision = new AudioClip(SoundPlayer.class.getResource("/resources/sound/bombCollision.wav").toString());
    private static final AudioClip gameStart = new AudioClip(SoundPlayer.class.getResource("/resources/sound/gameStart.wav").toString());
    private static final AudioClip backgroundMusic = new AudioClip(SoundPlayer.class.getResource("/resources/sound/backgroundMusic.wav").toString());

    public static void playBombExplosionSound() {
        bombExplosion.play();
    }

    public static void playBombPlacementSound() {
        bombPlacement.play();
    }

    public static void playPowerUpSound() {
        powerUp.setVolume(0.5);
        powerUp.play();
    }

    public static void playPlayerDiedSound() {
        playerDied.play();
    }

    public static void playBombKickedSound() {
        bombKicked.setVolume(0.4);
        bombKicked.play();
    }

    public static void playHardWallCollisionSound(long currentTime) {
        if (currentTime - lastTimeHard > 500) {
            hardWallCollision.setVolume(0.5);
            hardWallCollision.play();
            lastTimeHard = currentTime;
        }
    }

    public static void playSoftWallCollisionSound(long currentTime) {
        if (currentTime - lastTimeSoft > 500) {
            softWallCollision.setVolume(0.7);
            softWallCollision.play();
            lastTimeSoft = currentTime;
        }
    }

    public static void playBombCollisionSound(long currentTime) {
        if (currentTime - lastTimeBomb > 200) {
            bombCollision.setVolume(0.5);
            bombCollision.play();
            lastTimeBomb = currentTime;
        }
    }

    public static void playGameStartSound() {
        gameStart.setVolume(0.3);
        gameStart.play();
    }

    public static void startBackgroundMusic() {
        backgroundMusic.setCycleCount(AudioClip.INDEFINITE);
        backgroundMusic.setVolume(0.05);
        backgroundMusic.play();
    }

}
