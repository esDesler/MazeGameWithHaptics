import Haptics.DistanceCalculator;
import MazeCreator.MazeCreator;
import SoundPlayer.SoundPlayer;
import UserSettings.UserSettings;
import gameobjects.Bomber;
import gameobjects.GameObject;
import gameobjects.Powerup;
import gameobjects.Wall;
import util.GameObjectCollection;
import util.Key;
import util.ResourceCollection;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * JPanel that contains the entire game and game loop logic.
 */
public class GamePanel extends JPanel implements Runnable {

    // Screen size is determined by the map size
    static int panelWidth;
    static int panelHeight;

    private Thread thread;
    private boolean running;
    int resetDelay;

    private BufferedImage world;
    private final BufferedImage bg;
    private GameHUD gameHUD;

    private int mapWidth;
    private int mapHeight;
    private ArrayList<ArrayList<String>> mapLayout;

    private final MazeCreator mazeCreator;
    int level = 1;

    private HashMap<Integer, Key> controls;

    private static final double SOFTWALL_RATE = 0.825;

    private final DistanceCalculator distanceCalculator;

    private volatile boolean generateNewMaze;
    private volatile boolean reconstructMaze;

    /**
     * Construct game panel and load in a map file.
     * @param filename Name of the map file
     */
    GamePanel(String filename) {
        // TODO make dynamic
        final int mazeSize = 15;
        this.setFocusable(true);
        this.requestFocus();
        this.setControls();
        this.bg = ResourceCollection.Images.BACKGROUND.getImage();
        this.addKeyListener(new GameController(this));
        mazeCreator = new MazeCreator(mazeSize);
        distanceCalculator = new DistanceCalculator(mazeSize);
    }

    /**
     * Initialize the game panel with a HUD, window size, collection of game objects, and start the game loop.
     */
    void init() {
        this.resetDelay = 0;
        GameObjectCollection.init();
        this.gameHUD = new GameHUD();
        this.createTheMazeStructure();
        this.buildTheMapVisually();
        this.gameHUD.init();
        this.setPreferredSize(new Dimension(this.mapWidth * 32, (this.mapHeight * 32) + GameWindow.HUD_HEIGHT));
        System.gc();
        this.running = true;
        SoundPlayer.startBackgroundMusic();
        SoundPlayer.playGameStartSound(level);
        Statistics.incrementTriesOnCurrentMaze(level);
        Statistics.incrementTotalPlayedMazes(level);
        Statistics.setStartTime();
    }

    private void createTheMazeStructure() {
        mapLayout = mazeCreator.createMazeV2(level);
    }

    /**
     * Generate the map given the map file. The map is grid based and each tile is 32x32.
     * Create game objects depending on the string.
     */
    private void buildTheMapVisually() {
        // Map dimensions
        this.mapWidth = mapLayout.get(0).size();
        this.mapHeight = mapLayout.size();
        panelWidth = this.mapWidth * 32;
        panelHeight = this.mapHeight * 32;

        this.world = new BufferedImage(panelWidth, panelHeight, BufferedImage.TYPE_INT_RGB);

        // Build entire map
        for (int y = 0; y < this.mapHeight; y++) {
            for (int x = 0; x < this.mapWidth; x++) {
                switch (mapLayout.get(y).get(x)) {
                    case ("S"):     // Soft wall; breakable
                        if (Math.random() < SOFTWALL_RATE) {
                            BufferedImage sprSoftWall = ResourceCollection.Images.SOFT_WALL.getImage();
                            Wall softWall = new Wall(new Point2D.Float(x * 32, y * 32), sprSoftWall, true);
                            GameObjectCollection.spawn(softWall);
                        }
                        break;

                    case ("H"):     // Hard wall; unbreakable
                        // Code used to choose tile based on adjacent tiles
                        int code = 0;
                        if (y > 0 && mapLayout.get(y - 1).get(x).equals("H")) {
                            code += 1;  // North
                        }
                        if (y < this.mapHeight - 1 && mapLayout.get(y + 1).get(x).equals("H")) {
                            code += 4;  // South
                        }
                        if (x > 0 && mapLayout.get(y).get(x - 1).equals("H")) {
                            code += 8;  // West
                        }
                        if (x < this.mapWidth - 1 && mapLayout.get(y).get(x + 1).equals("H")) {
                            code += 2;  // East
                        }
                        BufferedImage sprHardWall = ResourceCollection.getHardWallTile(code);
                        Wall hardWall = new Wall(new Point2D.Float(x * 32, y * 32), sprHardWall, false);
                        GameObjectCollection.spawn(hardWall);
                        break;

                    case ("1"):     // Player 1; Bomber
                        BufferedImage[][] sprMapP1 = ResourceCollection.SpriteMaps.PLAYER_1.getSprites();
                        Bomber player = new Bomber(new Point2D.Float(x * 32, y * 32 - 16), sprMapP1, 1);
                        PlayerController playerController = new PlayerController(player, this.controls);
                        this.addKeyListener(playerController);
                        this.gameHUD.assignPlayer(player);
                        GameObjectCollection.spawn(player);
                        distanceCalculator.updatePlayer(player);
                        SoundPlayer.setStartPosition(player.getPosition().x, player.getPosition().y);
                        break;

                    case ("C"):
                        Powerup goalTile = new Powerup(new Point2D.Float(x * 32, y * 32), Powerup.Type.Checkpoint);
                        GameObjectCollection.spawn(goalTile);
                        distanceCalculator.updateGoalTile(goalTile);
                        break;

                    case ("PB"):    // Powerup Bomb
                        Powerup powerBomb = new Powerup(new Point2D.Float(x * 32, y * 32), Powerup.Type.Bomb);
                        GameObjectCollection.spawn(powerBomb);
                        break;

                    case ("PU"):    // Powerup Fireup
                        Powerup powerFireup = new Powerup(new Point2D.Float(x * 32, y * 32), Powerup.Type.Fireup);
                        GameObjectCollection.spawn(powerFireup);
                        break;

                    case ("PM"):    // Powerup Firemax
                        Powerup powerFiremax = new Powerup(new Point2D.Float(x * 32, y * 32), Powerup.Type.Firemax);
                        GameObjectCollection.spawn(powerFiremax);
                        break;

                    case ("PS"):    // Powerup Speed
                        Powerup powerSpeed = new Powerup(new Point2D.Float(x * 32, y * 32), Powerup.Type.Speed);
                        GameObjectCollection.spawn(powerSpeed);
                        break;

                    case ("PP"):    // Powerup Pierce
                        Powerup powerPierce = new Powerup(new Point2D.Float(x * 32, y * 32), Powerup.Type.Pierce);
                        GameObjectCollection.spawn(powerPierce);
                        break;

                    case ("PK"):    // Powerup Kick
                        Powerup powerKick = new Powerup(new Point2D.Float(x * 32, y * 32), Powerup.Type.Kick);
                        GameObjectCollection.spawn(powerKick);
                        break;

                    case ("PT"):    // Powerup Timer
                        Powerup powerTimer = new Powerup(new Point2D.Float(x * 32, y * 32), Powerup.Type.Timer);
                        GameObjectCollection.spawn(powerTimer);
                        break;

                    default:
                        break;
                }
            }
        }
    }

    /**
     * Initialize default key bindings for all players.
     */
    private void setControls() {
        this.controls = new HashMap<>();

        // Set Player controls
        this.controls.put(KeyEvent.VK_UP, Key.up);
        this.controls.put(KeyEvent.VK_DOWN, Key.down);
        this.controls.put(KeyEvent.VK_LEFT, Key.left);
        this.controls.put(KeyEvent.VK_RIGHT, Key.right);
        this.controls.put(KeyEvent.VK_SPACE, Key.action);
    }

    /**
     * When ESC is pressed, close the game
     */
    void exit() {
        this.running = false;
    }

    /**
     * When Enter is pressed, reset game object collection, collect garbage, reinitialize game panel, reload map
     */
    void resetGame() {
        generateNewMaze = true;
    }

    /**
     * Reset only the map, keeping the score
     */
    void resetMap() {
        reconstructMaze = true;
    }

    public void addNotify() {
        super.addNotify();

        if (this.thread == null) {
            this.thread = new Thread(this, "GameThread");
            this.thread.start();
        }
    }

    /**
     * The game loop.
     * The loop repeatedly calls update and repaints the panel.
     * Also reports the frames drawn per second and updates called per second (ticks).
     */
    @Override
    public void run() {
        long timer = System.currentTimeMillis();
        long lastTime = System.nanoTime();

        final double NS = 1000000000.0 / 60.0; // Locked ticks per second to 60
        double delta = 0;
        int fps = 0;    // Frames per second
        int ticks = 0;  // Ticks/Updates per second; should be 60 at all times

        // Count FPS, Ticks, and execute updates
        while (this.running) {
            long currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / NS;
            lastTime = currentTime;

            if (delta >= 1) {
                this.update();
                distanceCalculator.generateHaptics();
                ticks++;
                delta--;
            }

            this.repaint();
            fps++;

            // Update FPS and Ticks counter every second
            if (System.currentTimeMillis() - timer > 1000) {
                timer = System.currentTimeMillis();
                GameLauncher.window.update(fps, ticks);
                //haptics.generateHaptics();
                fps = 0;
                ticks = 0;

            }

            if (generateNewMaze) {
                generateNewMap();
                generateNewMaze = false;
            }

            if (reconstructMaze) {
                reconstructCurrentMaze();
                reconstructMaze = false;
            }
        }

        System.exit(0);
    }

    /**
     * The update method that loops through every game object and calls update.
     * Checks collisions between every two game objects.
     * Deletes game objects that are marked for deletion.
     * Checks if a player is a winner and updates score, then reset the map.
     */
    private void update() {
        GameObjectCollection.sortBomberObjects();
        // Loop through every game object arraylist
        for (int list = 0; list < GameObjectCollection.gameObjects.size(); list++) {
            for (int objIndex = 0; objIndex < GameObjectCollection.gameObjects.get(list).size(); objIndex++) {
                GameObject obj = GameObjectCollection.gameObjects.get(list).get(objIndex);
                obj.update();
                if (obj.isDestroyed()) {
                    // Destroy and remove game objects that were marked for deletion
                    obj.onDestroy();
                    GameObjectCollection.gameObjects.get(list).remove(obj);
                } else {
                    for (int list2 = 0; list2 < GameObjectCollection.gameObjects.size(); list2++) {
                        for (int objIndex2 = 0; objIndex2 < GameObjectCollection.gameObjects.get(list2).size(); objIndex2++) {
                            GameObject collidingObj = GameObjectCollection.gameObjects.get(list2).get(objIndex2);
                            // Skip detecting collision on the same object as itself
                            if (obj == collidingObj) {
                                continue;
                            }

                            // Visitor pattern collision handling
                            if (obj.getCollider().intersects(collidingObj.getCollider())) {
                                collidingObj.onCollisionEnter(obj);
                            }
                        }
                    }
                }
            }
        }

        // Check for the last bomber to survive longer than the others and increase score
        // Score is added immediately so there is no harm of dying when you are the last one
        // Reset map when there are 1 or less bombers left
        if (!this.gameHUD.matchSet) {
            this.gameHUD.updateScore();
        } else {
            // Checking size of array list because when a bomber dies, they do not immediately get deleted
            // This makes it so that the next round doesn't start until the winner is the only bomber object on the map
            if (GameObjectCollection.bomberObjects.size() <= 1) {
                Statistics.updateAverageTriesPerMaze(level);
                Statistics.incrementClearedMazes(level);
                Statistics.updateAverageTimePerMaze(level);
                this.gameHUD.matchSet = false;
                generateNewMaze = true;
            }
        }

        // Used to prevent resetting the game really fast
        this.resetDelay++;

        try {
            Thread.sleep(1000 / 144);
        } catch (InterruptedException ignored) {
        }
    }

    private void generateNewMap() {
        GameObjectCollection.init();
        createTheMazeStructure();
        buildTheMapVisually();
        System.gc();
        SoundPlayer.playGameStartSound(level);
        Statistics.updateStatisticsFile(level);
        Statistics.incrementTotalPlayedMazes(level);
        Statistics.resetTriesOnCurrentMaze(level);
        Statistics.setStartTime();
    }

    private void reconstructCurrentMaze() {
        GameObjectCollection.init();
        this.buildTheMapVisually();
        System.gc();
        SoundPlayer.playGameStartSound(level);
        Statistics.incrementTriesOnCurrentMaze(level);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Graphics2D buffer = this.world.createGraphics();
        buffer.clearRect(0, 0, this.world.getWidth(), this.world.getHeight());
        super.paintComponent(g2);

        this.gameHUD.drawHUD();

        // Draw background
        for (int i = 0; i < this.world.getWidth(); i += this.bg.getWidth()) {
            for (int j = 0; j < this.world.getHeight(); j += this.bg.getHeight()) {
                buffer.drawImage(this.bg, i, j, null);
            }
        }

        // Draw game objects
        for (int i = 0; i < GameObjectCollection.gameObjects.size(); i++) {
            for (int j = 0; j < GameObjectCollection.gameObjects.get(i).size(); j++) {
                GameObject obj = GameObjectCollection.gameObjects.get(i).get(j);
                obj.drawImage(buffer);
            }
        }

        // Draw HUD
        g2.drawImage(this.gameHUD.getP1info(), 0, 0, null);

        // Draw game world offset by the HUD
        g2.drawImage(this.world, 0, GameWindow.HUD_HEIGHT, null);

        g2.dispose();
        buffer.dispose();
    }

    public void turnOnOffBackgroundSound() {
        SoundPlayer.turnOnOffBackgroundSound();
    }

    public void switchOnOffNavigationMode(boolean navigationMode) {
        distanceCalculator.switchOnOffNavigationMode(navigationMode);
    }
}

/**
 * Used to control the game
 */
class GameController implements KeyListener {

    private GamePanel gamePanel;

    private boolean navigationMode;
    /**
     * Construct a universal game controller key listener for the game.
     * @param gamePanel Attach game controller to this game panel
     */
    GameController(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Key events for general game operations such as exit
     * @param e Keyboard key pressed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        // Display controls
        if (e.getKeyCode() == KeyEvent.VK_F1) {
            System.out.println("F1 key pressed: Displaying help");

            String[] columnHeaders = { "Command", "Key" };
            Object[][] controls = {
                    {"Help", "F1"},
                    {"Start from center", "Shift"},
                    {"New maze", "Enter"},
                    {"Choose level 1", "Numpad1"},
                    {"Choose level 2", "Numpad2"},
                    {"Navigation Mode", "Space"},
                    {"Mute music", "Numpad0"},
                    {"Increase vibration", "+"},
                    {"Decrease vibration", "-"},
                    {"Reset", "Enter"},
                    {"Exit", "ESC"}
            };

            JTable controlsTable = new JTable(controls, columnHeaders);
            JTableHeader tableHeader = controlsTable.getTableHeader();

            // Wrap JTable inside JPanel to display
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            panel.add(tableHeader, BorderLayout.NORTH);
            panel.add(controlsTable, BorderLayout.CENTER);

            JOptionPane.showMessageDialog(this.gamePanel, panel, "Controls", JOptionPane.PLAIN_MESSAGE);
        }

        // Close game
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.out.println("Escape key pressed: Closing game");
            this.gamePanel.exit();
        }

        // Reset game
        // Delay prevents resetting too fast which causes the game to crash
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (this.gamePanel.resetDelay >= 20) {
                System.out.println("Enter pressed: Creating new maze");
                this.gamePanel.resetGame();
            }
        }

        // Reset map
        // Delay prevents resetting too fast which causes the game to crash
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            if (this.gamePanel.resetDelay >= 20) {
                System.out.println("Shift pressed: Resetting to start point");
                this.gamePanel.resetMap();
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_NUMPAD1) {
            if (this.gamePanel.resetDelay >= 20) {
                System.out.println("Level 1 selected");
                this.gamePanel.level = 1;
                this.gamePanel.resetGame();
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_NUMPAD2) {
            if (this.gamePanel.resetDelay >= 20) {
                System.out.println("Level 2 selected");
                this.gamePanel.level = 2;
                this.gamePanel.resetGame();
            }
        }

        // Request haptic information about where the goal is relative to you
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (this.gamePanel.resetDelay >= 20) {
                System.out.println("Space key pressed: " + (navigationMode ? "Switching back to game mode" : "Switching to navigation mode"));
                navigationMode = !navigationMode;
                this.gamePanel.switchOnOffNavigationMode(navigationMode);
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_NUMPAD0) {
            if (UserSettings.isMuteMusic()) {
                System.out.println("Playing music");
            } else {
                System.out.println("Muting music");
            }
            UserSettings.setMuteMusic(!UserSettings.isMuteMusic());
            this.gamePanel.turnOnOffBackgroundSound();
        }

        if (e.getKeyCode() == KeyEvent.VK_PLUS) {
            System.out.println("Plus key pressed: Increasing vibration intensity");
            UserSettings.incrementIntensity();
        }

        if (e.getKeyCode() == KeyEvent.VK_MINUS) {
            System.out.println("Minus key pressed: Decreasing vibration intensity");
            UserSettings.decrementIntensity();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
