package MazeCreator;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class Path {
    private MazeTile headOfPath;
    private Direction direction;
    private Direction illegalDirection;
    private ArrayList<MazeTile> path;
    private final Random random;
    private Maze maze;
    private boolean hasMoved;

    public Path(MazeTile headOfPath, Direction direction, Maze maze) {
        this.headOfPath = headOfPath;
        this.direction = direction;
        this.illegalDirection = null;
        this.path = new ArrayList<MazeTile>();
        this.random = new Random();
        this.maze = maze;
    }

    public MazeTile getHeadOfPath() {
        return headOfPath;
    }

    public ArrayList<MazeTile> getPath() {
        return path;
    }

    public void setIllegalDirection(Direction direction) {
        switch (direction) {
            case UP:
                illegalDirection = Direction.DOWN;
                break;
            case DOWN:
                illegalDirection = Direction.UP;
                break;
            case LEFT:
                illegalDirection = Direction.RIGHT;
                break;
            case RIGHT:
                illegalDirection = Direction.LEFT;
                break;
        }
    }

    private void makeRightCircularPath(ArrayList<MazeTile> completeCircularPath) {
        int i = completeCircularPath.indexOf(headOfPath);
        int size = completeCircularPath.size();

        while (!headOfPath.isGoalTile()) {
            path.add(headOfPath);
            headOfPath = completeCircularPath.get((++i) % size);
        }
    }

    public void createRandomPathWithDirection(int distance) {
        hasMoved = false;
        ArrayList<Direction> availableDirections = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            if (direction != illegalDirection) {
                availableDirections.add(direction);
            }
        }

        int r = random.nextInt(availableDirections.size());
        Direction direction = availableDirections.get(r);

        int startPosH = headOfPath.getJ();
        int startPosV = headOfPath.getI();

        switch (direction) {
            case UP:
                createUpPath(startPosH, startPosV, distance);
                break;
            case DOWN:
                createDownPath(startPosH, startPosV, distance);
                break;
            case LEFT:
                createLeftPath(startPosH, startPosV, distance);
                break;
            case RIGHT:
                createRightPath(startPosH, startPosV, distance);
                break;
        }
        this.direction = direction;
        if (hasMoved) {
            this.setIllegalDirection(direction);
        }
    }

    public void createRandomPath(int distance) {
        int startPosH = headOfPath.getJ();
        int startPosV = headOfPath.getI();

        int direction = random.nextInt(4);
        switch (direction) {
            case 0:
                createUpPath(startPosH, startPosV, distance);
                break;
            case 1:
                createDownPath(startPosH, startPosV, distance);
                break;
            case 2:
                createLeftPath(startPosH, startPosV, distance);
                break;
            case 3:
                createRightPath(startPosH, startPosV, distance);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + direction);
        }
    }

    private void createRightPath(int startPosH, int startPosV, int distance) {
        for (int j = startPosH + 1; j <= startPosH + distance; j++) {
            if (maze.getMazeSize() < j) break;
            if (maze.getTile(startPosV, j).isPlayer()) break;
            headOfPath = maze.getTile(startPosV, j);
            if (headOfPath.isGoalTile()) break;
            else {
                path.add(headOfPath);
            }
            hasMoved = true;
        }
    }

    private void createLeftPath(int startPosH, int startPosV, int distance) {
        for (int j = startPosH - 1; j >= startPosH - distance; j--) {
            if (j < 1) break;
            if (maze.getTile(startPosV, j).isPlayer()) break;
            headOfPath = maze.getTile(startPosV, j);
            if (headOfPath.isGoalTile()) break;
            else {
                path.add(headOfPath);
            }
            hasMoved = true;
        }
    }

    private void createDownPath(int startPosH, int startPosV, int distance) {
        for (int i = startPosV + 1; i <= startPosV + distance; i++) {
            if (maze.getMazeSize() < i) break;
            if (maze.getTile(i, startPosH).isPlayer()) break;
            headOfPath = maze.getTile(i, startPosH);
            if (headOfPath.isGoalTile()) break;
            else {
                path.add(headOfPath);
            }
            hasMoved = true;
        }
    }

    private void createUpPath(int startPosH, int startPosV, int distance) {
        for (int i = startPosV - 1; i >= startPosV - distance; i--) {
            if (i < 1) break;
            if (maze.getTile(i, startPosH).isPlayer()) break;
            headOfPath = maze.getTile(i, startPosH);
            if (headOfPath.isGoalTile()) break;
            else {
                path.add(headOfPath);
            }
            hasMoved = true;
        }
    }

    public void createCircularPathToGoalTile(ArrayList<MazeTile> completeCircularPath) {
        if (random.nextInt(2) == 0) makeRightCircularPath(completeCircularPath);
        else makeLeftCircularPath(completeCircularPath);
    }

    private void makeLeftCircularPath(ArrayList<MazeTile> completeCircularPath) {
        int i = completeCircularPath.indexOf(headOfPath);
        int size = completeCircularPath.size();

        while (!headOfPath.isGoalTile()) {
            path.add(headOfPath);
            headOfPath = completeCircularPath.get(i--);
            if (i < 0) i = size - 1;
        }
    }

    public void reset() {
        path.clear();
        headOfPath = maze.getTile(maze.getCenter(), maze.getCenter());
    }
}
