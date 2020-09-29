package MazeCreator;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class MazeCreator {
    private Random random;

    private Maze maze;

    public MazeCreator(int mazeSize) {
        this.maze = new Maze(mazeSize);
    }

    public ArrayList<ArrayList<String>> createMazeV2(int level) {
        maze.clearMaze();

        if (level == 1) {
            return easyMaze();
        } else {
            return hardMaze();
        }
    }

    private ArrayList<ArrayList<String>> hardMaze() {
        Random random = new Random();
        ArrayList<ArrayList<String>> maze;

        switch (random.nextInt(3)) {
            case 1:
                maze = loadMap("multiplePaths");
                break;
            case 2:
                maze = loadMap("openSpacePath");
                break;
            default:
                maze = loadMap("smallOpeningPath");
                break;
        }
        maze = rotateMaze(maze, random.nextInt(4));
        return maze;
    }

    private ArrayList<ArrayList<String>> easyMaze() {
        Random random = new Random();
        ArrayList<ArrayList<String>> maze;

        switch (random.nextInt(3)) {
            case 1:
                maze = loadMap("twoPath");
                break;
            case 2:
                maze = loadMap("narrowPath");
                break;
            default:
                maze = loadMap("onePath");
                break;
        }
        maze = rotateMaze(maze, random.nextInt(4));
        return maze;
    }

    private ArrayList<ArrayList<String>> rotateMaze(ArrayList<ArrayList<String>> maze, int rotation) {
        System.out.println("Rotating " + 90*rotation + " degrees");
        switch (rotation) {
            case 1:
                return rotate90Degrees(maze);
            case 2:
                return rotate180Degrees(maze);
            case 3:
                return rotate90DegreesCounterCW(maze);
            default:
                return maze;
        }
    }

    private ArrayList<ArrayList<String>> rotate90DegreesCounterCW(ArrayList<ArrayList<String>> maze) {
        int size = maze.size();
        ArrayList<ArrayList<String>> rotatedMaze = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            rotatedMaze.add(new ArrayList<>());
        }

        for (int i = 0; i < size; i++) {
            for (ArrayList<String> strings : maze) {
                rotatedMaze.get(i).add(strings.get(size - 1 - i));
            }
        }

        return rotatedMaze;
    }

    private ArrayList<ArrayList<String>> rotate180Degrees(ArrayList<ArrayList<String>> maze) {
        int size = maze.size();
        ArrayList<ArrayList<String>> rotatedMaze = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            rotatedMaze.add(new ArrayList<>());
        }

        for (int i = size - 1; i >= 0; i--) {
            for (int j = size - 1; j >= 0; j--) {
                rotatedMaze.get(size - 1 - i).add(maze.get(i).get(j));
            }
        }

        return rotatedMaze;
    }

    private ArrayList<ArrayList<String>> rotate90Degrees(ArrayList<ArrayList<String>> maze) {
        int size = maze.size();
        ArrayList<ArrayList<String>> rotatedMaze = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            rotatedMaze.add(new ArrayList<>());
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                rotatedMaze.get(i).add(maze.get(size - j - 1).get(i));
            }
        }

        return rotatedMaze;
    }

    private ArrayList<ArrayList<String>> loadMap(String fileName) {
        ArrayList<ArrayList<String>> mapLayout = new ArrayList<>();

        try {
            File myObj = new File("src/main/resources/maps/" + fileName + ".csv");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                mapLayout.add(new ArrayList<>(Arrays.asList(data.split(","))));
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return mapLayout;
    }

    private ArrayList<ArrayList<String>> loadMap(BufferedReader bufferedReader) {
        // Parsing map data from file
        ArrayList<ArrayList<String>> mapLayout = new ArrayList<>();
        try {
            String currentLine;
            while ((currentLine = bufferedReader.readLine()) != null) {
                System.out.println(currentLine);
                if (currentLine.isEmpty()) {
                    continue;
                }
                // Split row into array of strings and add to array list
                mapLayout.add(new ArrayList<>(Arrays.asList(currentLine.split(","))));
            }
        } catch (IOException | NullPointerException e) {
            System.out.println(e + ": Error parsing map data");
            e.printStackTrace();
        }
        System.out.println(mapLayout);
        return mapLayout;
    }

    public ArrayList<ArrayList<String>> createMaze(int level) {
        maze.clearMaze();
        random = new Random();

        maze.setCenter("1");

        if (level == 1) {
            createLevelOneMaze();
        } else {
            createLevelTwoMaze();
        }
        return maze.toMapLayout();
    }

    /**
     * Creates a more or less randomized maze with a certain path length.
     * Needs more work. Problems with narrow paths that are difficult to spot.
     */
    private void createLevelTwoMaze() {
        maze.placeGoalTileInRandomCorner();
        Path path = new Path(maze.getTile(maze.getCenter(), maze.getCenter()), null, maze);

        do {
            path.reset();
            while (!path.getHeadOfPath().isGoalTile()) {
                path.createRandomPathWithDirection(random.nextInt(4) + 2);
            }
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


}
