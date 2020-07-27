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
        while (!path.getHeadOfPath().isGoalTile())
        {
            path.createRandomPathWithDirection(random.nextInt(4) + 2);
        }
    }

    private void createLevelOneMaze() {
        maze.placeRandomGoalTile();
        Path path = new Path(maze.getTile(maze.getCenter(), maze.getCenter()), null, maze);
        path.createRandomPath(maze.getMazeSize() / 2);
        path.createCircularPathToGoalTile(maze.createOuterRingOfMapArray());
        maze.addPath(path);
    }

    private void fillPositionOnMap(MazeTile position) {
        maze.updatePosition(position.getI(), position.getJ(), "-1");
    }
}
