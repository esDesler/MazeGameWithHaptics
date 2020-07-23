package MazeCreator;

import java.util.ArrayList;
import java.util.Random;

public class MazeCreator {
    private Random random;

    private Maze maze;

    public MazeCreator(int mazeSize) {
        this.maze = new Maze(mazeSize);
    }

    public ArrayList<ArrayList<String>> createMaze() {
        random = new Random();

        maze.setCenter("1");

        createLevelOneMaze();
        //createLevelTwoMaze();
        return maze.toMapLayout();
    }

    /*private void createLevelTwoMaze() {
        maze.placeRandomGoalTile();

        int i = centerV;
        int j = centerH;
        String posOfHead = i + " " + j;

        for (int n = 0; n < 20; n++) {
            System.out.println(posOfHead);
            try {
                posOfHead = createRandomPath(i, j, 4);
            } catch (ArrayIndexOutOfBoundsException ignore) {
            }
            String[] position = posOfHead.split(" ");
            i = Integer.parseInt(position[0]);
            j = Integer.parseInt(position[1]);
        }

    }*/

    private void createLevelOneMaze() {
        maze.placeRandomGoalTile();
        MazeTile headOfPath = createRandomPath(maze.getCenter(), maze.getCenter(), maze.getMazeSize() / 2);
        createCircularPathToGoalTile(headOfPath);
    }

    private void createCircularPathToGoalTile(MazeTile startPosition) {
        if (random.nextInt(2) == 0) findAndMakeRightCircularPath(startPosition);
        else findAndMakeLeftCircularPath(startPosition);
    }

    private void findAndMakeLeftCircularPath(MazeTile startPosition) {
        ArrayList<MazeTile> completeCircularPath = maze.createOuterRingOfMapArray();

        int i = completeCircularPath.indexOf(startPosition);
        int size = completeCircularPath.size();
        MazeTile position = startPosition;

        while (!position.isGoalTile()) {
            fillPositionOnMap(position);
            position = completeCircularPath.get(i--);
            if (i < 0) i = size - 1;
        }
    }

    private void fillPositionOnMap(MazeTile position) {
        maze.updatePosition(position.getI(), position.getJ(), "-1");
    }

    private void findAndMakeRightCircularPath(MazeTile startPosition) {
        ArrayList<MazeTile> completeCircularPath = maze.createOuterRingOfMapArray();
        int i = completeCircularPath.indexOf(startPosition);
        int size = completeCircularPath.size();
        MazeTile position = startPosition;

        while (!position.isGoalTile()) {
            fillPositionOnMap(position);
            position = completeCircularPath.get((++i) % size);
        }
    }

    private MazeTile createRandomPath(int startPosV, int startPosH, int distance) {
        int direction = random.nextInt(4);
        switch (direction) {
            case 0:
                return createUpPath(startPosV, startPosH, distance);
            case 1:
                return createDownPath(startPosV, startPosH, distance);
            case 2:
                return createLeftPath(startPosV, startPosH, distance);
            case 3:
                return createRightPath(startPosV, startPosH, distance);
            default:
                throw new IllegalStateException("Unexpected value: " + direction);
        }
    }

    private MazeTile createRightPath(int startPosV, int startPosH, int distance) {
        MazeTile headOfPath = new MazeTile(startPosV, startPosH);
        if (maze.getMazeSize() < startPosH + distance) return headOfPath;
        for (int j = startPosH + 1; j <= startPosH + distance; j++) {
            if (headOfPath.isGoalTile()) break;
            maze.updatePosition(startPosV, j, "-1");
            headOfPath = maze.getTile(startPosV, j);
        }
        return headOfPath;
    }

    private MazeTile createLeftPath(int startPosV, int startPosH, int distance) {
        MazeTile headOfPath = new MazeTile(startPosV, startPosH);
        if (startPosH - distance < 0) return headOfPath;
        for (int j = startPosH - 1; j >= startPosH - distance; j--) {
            if (headOfPath.isGoalTile()) break;
            maze.updatePosition(startPosV, j, "-1");
            headOfPath = maze.getTile(startPosV, j);
        }
        return headOfPath;
    }

    private MazeTile createDownPath(int startPosV, int startPosH, int distance) {
        MazeTile headOfPath = new MazeTile(startPosV, startPosH);
        if (maze.getMazeSize() < startPosV + distance) return headOfPath;
        for (int i = startPosV + 1; i <= startPosH + distance; i++) {
            if (headOfPath.isGoalTile()) break;
            maze.updatePosition(i, startPosH, "-1");
            headOfPath = maze.getTile(i, startPosH);
        }
        return headOfPath;
    }

    private MazeTile createUpPath(int startPosV, int startPosH, int distance) {
        MazeTile headOfPath = new MazeTile(startPosV, startPosH);
        if (startPosV - distance < 0) return headOfPath;
        for (int i = startPosV - 1; i >= startPosH - distance; i--) {
            if (headOfPath.isGoalTile()) break;
            maze.updatePosition(i, startPosH, "-1");
            headOfPath = maze.getTile(i, startPosH);
        }

        return headOfPath;
    }

    private void printMap() {
        ArrayList<ArrayList<String>> mapLayout = maze.toMapLayout();
        for (ArrayList<String> mapLine : mapLayout) {
            System.out.println(mapLine);
        }
    }
}
