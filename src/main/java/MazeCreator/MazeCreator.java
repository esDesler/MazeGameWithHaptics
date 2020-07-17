package MazeCreator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MazeCreator {

    private ArrayList<ArrayList<String>> mapLayout;
    private Random random;

    public MazeCreator() {
    }

    public ArrayList<ArrayList<String>> createMaze() {
        random = new Random();
        mapLayout = new ArrayList<>();
        for (int i = 0; i < 13; i++) {
            mapLayout.add(new ArrayList<>(Arrays.asList("H,H,H,H,H,H,H,H,H,H,H,H,H".split(","))));
        }
        int centerV = mapLayout.size() / 2;
        int centerH = mapLayout.get(0).size() / 2;

        mapLayout.get(centerV).set(centerH, "1");

        placeRandomCheckpoint(mapLayout.size() - 3, mapLayout.get(0).size() - 3);
        String headOfPath = createRandomPath(centerV, centerH);
        createCircularPathToGoalTile(headOfPath);
        return mapLayout;
    }

    private void createCircularPathToGoalTile(String startPosition) {
        if (random.nextInt(2) == 0) findAndMakeRightCircularPath(startPosition);
        else findAndMakeLeftCircularPath(startPosition);
    }

    private void findAndMakeLeftCircularPath(String startPosition) {
        ArrayList<String> completeCircularPath = createOuterRingOfMapArray();

        int i = completeCircularPath.indexOf(startPosition);
        int size = completeCircularPath.size();
        String position = startPosition;

        while (!positionContainsGoalTile(position)) {
            fillPositionOnMap(position);
            position = completeCircularPath.get(i--);
            if (i < 0) i = size - 1;
        }
    }

    private void fillPositionOnMap(String position) {
        String[] iAndJ = position.split(" ");
        int i = Integer.parseInt(iAndJ[0]);
        int j = Integer.parseInt(iAndJ[1]);
        mapLayout.get(i).set(j, "-1");
    }

    private void findAndMakeRightCircularPath(String startPosition) {
        ArrayList<String> completeCircularPath = createOuterRingOfMapArray();

        int i = completeCircularPath.indexOf(startPosition);
        int size = completeCircularPath.size();
        String position = startPosition;

        while (!positionContainsGoalTile(position)) {
            fillPositionOnMap(position);
            position = completeCircularPath.get((i++) % size);
        }
    }

    private boolean positionContainsGoalTile(String position) {
        String[] iAndJ = position.split(" ");
        int i = Integer.parseInt(iAndJ[0]);
        int j = Integer.parseInt(iAndJ[1]);
        return mapLayout.get(i).get(j).equals("C");
    }

    private ArrayList<String> createOuterRingOfMapArray() {
        ArrayList<String> outerRingOfMap = new ArrayList<>();
        int maxH = mapLayout.get(0).size() - 2;
        int maxV = mapLayout.size() - 2;

        for (int j = 1; j < maxH; j++) outerRingOfMap.add("1 " + j);
        for (int i = 1; i < maxV; i++) outerRingOfMap.add(i + " " + maxH);
        for (int j = maxH; j > 1; j--) outerRingOfMap.add(maxV + " " + j);
        for (int i = maxV; i > 1; i--) outerRingOfMap.add(i + " 1");

        return outerRingOfMap;
    }

    private String createRandomPath(int startPosV, int startPosH) {
        int direction = random.nextInt(4);

        switch (direction) {
            case 0:
                return createUpPath(startPosV, startPosH, mapLayout.size() / 2);
            case 1:
                return createDownPath(startPosV, startPosH, mapLayout.size() / 2);
            case 2:
                return createLeftPath(startPosV, startPosH, mapLayout.get(0).size() / 2);
            case 3:
                return createRightPath(startPosV, startPosH, mapLayout.get(0).size() / 2);
            default:
                throw new IllegalStateException("Unexpected value: " + direction);
        }
    }

    private String createRightPath(int startPosV, int startPosH, int distance) {
        String headOfPath = "";
        for (int j = startPosH + 1; j < startPosH + distance; j++) {
            mapLayout.get(startPosV).set(j, "-1");
            headOfPath = startPosV + " " + j;
        }
        return headOfPath;
    }

    private void printMap() {
        for (ArrayList<String> mapLine : mapLayout) {
            System.out.println(mapLine);
        }
    }

    private String createLeftPath(int startPosV, int startPosH, int distance) {
        String headOfPath = "";
        for (int j = startPosH - 1; j > startPosH - distance; j--) {
            mapLayout.get(startPosV).set(j, "-1");
            headOfPath = startPosV + " " + j;
        }
        return headOfPath;
    }

    private String createDownPath(int startPosV, int startPosH, int distance) {
        String headOfPath = "";
        for (int i = startPosV + 1; i < startPosH + distance; i++) {
            mapLayout.get(i).set(startPosH, "-1");
            headOfPath = i + " " + startPosH;
        }
        return headOfPath;
    }

    private String createUpPath(int startPosV, int startPosH, int distance) {
        String headOfPath = "";
        for (int i = startPosV - 1; i > startPosH - distance; i--) {
            mapLayout.get(i).set(startPosH, "-1");
            headOfPath = i + " " + startPosH;
        }
        return headOfPath;
    }

    private void placeRandomCheckpoint(int mapSizeV, int mapSizeH) {
        // (i,j) can be in either of the four corners
        int i = random.nextInt(2) * mapSizeV + 1;
        int j = random.nextInt(2) * mapSizeH + 1;

        mapLayout.get(i).set(j, "C");
    }
}
