package Sounds;


import gameobjects.Bomber;
import javafx.scene.media.AudioClip;

public class SoundPlayer {

    private static float x;
    private static float y;
    private static int i = 0;

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
    private static final AudioClip gameStartLevel1 = new AudioClip(SoundPlayer.class.getResource("/resources/sound/gameStart1.wav").toString());
    private static final AudioClip gameStartLevel2 = new AudioClip(SoundPlayer.class.getResource("/resources/sound/gameStart2.wav").toString());
    private static final AudioClip backgroundMusic = new AudioClip(SoundPlayer.class.getResource("/resources/sound/backgroundMusic.wav").toString());
    private static final AudioClip footStepsOne = new AudioClip(SoundPlayer.class.getResource("/resources/sound/footStepsOne.wav").toString());
    private static final AudioClip footStepsTwo = new AudioClip(SoundPlayer.class.getResource("/resources/sound/footStepsTwo.wav").toString());
    private static final AudioClip footStepsThree = new AudioClip(SoundPlayer.class.getResource("/resources/sound/footStepsThree.wav").toString());

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

    public static void playGameStartSound(int level) {
        if (level == 1) {
            gameStartLevel1.setVolume(0.3);
            gameStartLevel1.play();
        } else {
            gameStartLevel2.setVolume(0.15);
            gameStartLevel2.play();
        }
    }

    public static void startBackgroundMusic() {
        backgroundMusic.setCycleCount(AudioClip.INDEFINITE);
        backgroundMusic.setVolume(0.03);
        backgroundMusic.play();
    }

    public static void setStartPosition(float newX, float newY) {
        x = newX;
        y = newY;
    }

    public static void playFootStepsSound(Bomber bomber) {
        if (Math.abs(bomber.getPosition().x - x) >= 32) {
            pickStepsClip().play();
            x = bomber.getPosition().x;
            i = (i + 1) % 3;
        } else if (Math.abs(bomber.getPosition().y - y) >= 32) {
            pickStepsClip().play();
            y = bomber.getPosition().y;
            i = (i + 1) % 3;
        }
    }

    private static AudioClip pickStepsClip() {
        switch (i) {
            case 0: return footStepsOne;
            case 1: return footStepsTwo;
            default: return footStepsThree;
        }
    }
}
