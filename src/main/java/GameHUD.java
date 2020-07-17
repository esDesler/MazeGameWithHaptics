import gameobjects.Bomber;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Displays various game information on the screen such as each player's score.
 */
public class GameHUD {

    private Bomber player;
    private BufferedImage playerInfo;
    private int playerScore;
    boolean matchSet;

    GameHUD() {
        this.matchSet = false;
    }

    void init() {
        // Height of the HUD
        int height = GameWindow.HUD_HEIGHT;;

        this.playerInfo = new BufferedImage(GamePanel.panelWidth, height, BufferedImage.TYPE_INT_RGB);
    }

    /**
     * Used by game panel to draw player info to the screen
     *
     * @return Player info box
     */
    BufferedImage getP1info() {
        return this.playerInfo;
    }

    /**
     * Assign an info box to a player that shows the information on this player.
     *
     * @param player The player to be assigned
     */
    void assignPlayer(Bomber player) {
        this.player = player;
    }

    /**
     * Checks if there is only one player alive left and increases their score.
     * The match set boolean is used to check if a point is already added so that the winner can freely
     * move around for a while before resetting the map. This also allows the winner to kill themselves without
     * affecting their score since the score was already updated.
     */
    public void updateScore() {
        if (player.isHasWon()) {
            playerScore++;
            this.matchSet = true;
        }
    }

    /**
     * Continuously redraw player information such as score.
     */
    void drawHUD() {
        Graphics playerGraphics = this.playerInfo.createGraphics();

        // Clean info boxes
        playerGraphics.clearRect(0, 0, playerInfo.getWidth(), playerInfo.getHeight());

        // Set border color per player
        playerGraphics.setColor(Color.WHITE);    // Player 1 info box border color

        Font font = new Font("Courier New", Font.BOLD, 24);

        // Draw border and sprite
        playerGraphics.drawRect(1, 1, this.playerInfo.getWidth() - 2, this.playerInfo.getHeight() - 2);
        playerGraphics.drawImage(this.player.getBaseSprite(), 0, 0, null);

        // Draw score
        playerGraphics.setFont(font);
        playerGraphics.setColor(Color.WHITE);
        playerGraphics.drawString("" + this.playerScore, this.playerInfo.getWidth() / 2, 32);

        // Dispose
        playerGraphics.dispose();
    }

}
