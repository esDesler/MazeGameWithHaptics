package MazeCreator;

import java.util.ArrayList;
import java.util.Random;

public class Maze {

    private MazeTile[][] maze;
    private final int viewSize;
    private final int mazeSize;
    private Random random;

    public Maze(int mazeSize) {
        this.random = new Random();
        this.viewSize = mazeSize;
        this.maze = new MazeTile[mazeSize][mazeSize];
        initiateMaze();
        this.mazeSize = viewSize - 2;
    }

    private void initiateMaze() {
        for (int i = 0; i < viewSize; i++) {
            for (int j = 0; j < viewSize; j++) {
                this.maze[i][j] = new MazeTile(i, j);
            }
        }
    }

    public int getMazeSize() {
        return mazeSize;
    }

    public void setCenter(String s) {
        this.maze[viewSize / 2][viewSize / 2].setTileElement(s);
    }

    public ArrayList<ArrayList<String>> toMapLayout() {
        ArrayList<ArrayList<String>> mapLayout = new ArrayList<>();
        for (int i = 0; i < viewSize; i++) {
            mapLayout.add(new ArrayList<>());
            for (int j = 0; j < viewSize; j++) {
                mapLayout.get(i).add(j, maze[i][j].getTileElement());
            }
        }
        return mapLayout;
    }

    public void placeRandomGoalTile() {
        int i;
        int j;

        do {
            i = random.nextInt(mazeSize - 1) + 1;
            j = random.nextInt(mazeSize - 1) + 1;
        } while (distanceFromCenter(i,j) < mazeSize/4);

        maze[i][j].setTileElement("C");
    }

    private int distanceFromCenter(int i, int j) {
        return (int) Math.sqrt(Math.pow(i - getCenter(), 2) + Math.pow(j - getCenter(), 2));
    }

    public void placeGoalTileInRandomCorner() {
        // (i,j) can be in either of the four corners
        int i = random.nextInt(2) * (mazeSize - 1) + 1;
        int j = random.nextInt(2) * (mazeSize - 1) + 1;

        maze[i][j].setTileElement("C");
    }

    public int getCenter() {
        return viewSize / 2;
    }

    public void updatePosition(int i, int j, String value) {
        maze[i][j].setTileElement(value);
    }

    public MazeTile getTile(int i, int j) {
        return maze[i][j];
    }

    public ArrayList<MazeTile> createOuterRingOfMapArray() {
        ArrayList<MazeTile> outerRingOfMap = new ArrayList<>();

        //noinspection ManualArrayToCollectionCopy
        for (int j = 1; j < this.mazeSize; j++) outerRingOfMap.add(maze[1][j]);
        for (int i = 1; i < mazeSize; i++) outerRingOfMap.add(maze[i][mazeSize]);
        for (int j = this.mazeSize; j > 1; j--) outerRingOfMap.add(maze[mazeSize][j]);
        for (int i = mazeSize; i > 1; i--) outerRingOfMap.add(maze[i][1]);

        return outerRingOfMap;
    }

    @Override
    public String toString() {
        StringBuilder maze = new StringBuilder();
        for (int i = 0; i < viewSize; i++) {
            for (int j = 0; j < viewSize; j++) {
                maze.append(this.maze[i][j].getTileElement()).append("\t");
            }
            maze.append("\n");
        }
        return maze.toString();
    }

    public void addPath(Path path) {
        for (MazeTile mazeTile : path.getPath()) {
            mazeTile.setTileElement("-1");
            this.maze[mazeTile.getI()][mazeTile.getJ()] = mazeTile;
        }
    }

    public void clearMaze() {
        initiateMaze();
    }
}
