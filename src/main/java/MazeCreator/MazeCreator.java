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
        maze.clearMaze();
        random = new Random();

        maze.setCenter("1");

        //createLevelOneMaze();
        createLevelTwoMaze();
        return maze.toMapLayout();
    }

    private void createLevelTwoMaze() {
        maze.placeRandomGoalTile();
        Path path = new Path(maze.getTile(maze.getCenter(), maze.getCenter()), null, maze);

        //for (int i = 0; i < 30; i++)
        do {
            path.reset();
            while (!path.getHeadOfPath().isGoalTile()) {
                path.createRandomPathWithDirection(random.nextInt(4) + 2);
            }
        System.out.println(path.getPath().size());
        } while (path.getPath().size() < 50 || 150 < path.getPath().size());
        maze.addPath(path);
    }

    private void createLevelOneMaze() {
        maze.placeGoalTileInRandomCorner();
        Path path = new Path(maze.getTile(maze.getCenter(), maze.getCenter()), null, maze);
        path.createRandomPath(maze.getMazeSize() / 2);
        path.createCircularPathToGoalTile(maze.createOuterRingOfMapArray());
        maze.addPath(path);
    }

    private void fillPositionOnMap(MazeTile position) {
        maze.updatePosition(position.getI(), position.getJ(), "-1");
    }
}
