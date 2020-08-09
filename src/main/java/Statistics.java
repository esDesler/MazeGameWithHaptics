import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

public class Statistics {
    private static int clearedMazesLvl1;
    private static int totalPlayedMazesLvl1;
    private static int triesOnCurrentMazeLvl1;
    private static double averageTriesLvl1 = 1;
    private static int clearedMazesLvl2;
    private static int totalPlayedMazesLvl2;
    private static int triesOnCurrentMazeLvl2;
    private static double averageTriesLvl2;
    private static long startTime;
    private static double averageTimeLvl1;
    private static double averageTimeLvl2;

    public static void incrementClearedMazes(int level) {
        if (level == 1) {
            clearedMazesLvl1++;
        } else {
            clearedMazesLvl2++;
        }
    }

    public static void incrementTotalPlayedMazes(int level) {
        if (level == 1) {
            totalPlayedMazesLvl1++;
        } else {
            totalPlayedMazesLvl2++;
        }
    }

    public static void resetTriesOnCurrentMaze(int level) {
        if (level == 1) {
            triesOnCurrentMazeLvl1 = 1;
        } else {
            triesOnCurrentMazeLvl2 = 1;
        }
    }

    public static void incrementTriesOnCurrentMaze(int level) {
        if (level == 1) {
            triesOnCurrentMazeLvl1++;
        } else {
            triesOnCurrentMazeLvl2++;
        }
    }

    public static void updateAverageTriesPerMaze(int level) {
        if (level == 1) {
            averageTriesLvl1 = (clearedMazesLvl1 * averageTriesLvl1 + triesOnCurrentMazeLvl1) / (clearedMazesLvl1 + 1);
        } else {
            averageTriesLvl2 = (clearedMazesLvl2 * averageTriesLvl2 + triesOnCurrentMazeLvl2) / (clearedMazesLvl2 + 1);

        }
    }

    public static void setStartTime() {
        startTime = System.currentTimeMillis();
    }

    public static void updateAverageTimePerMaze(int level) {
        long endTime = System.currentTimeMillis();
        double elapsedTime = (double) (endTime - startTime) / 1000;
        if (level == 1) {
            averageTimeLvl1 = (clearedMazesLvl1 * averageTimeLvl1 + elapsedTime) / (clearedMazesLvl1 + 1);
        } else {
            averageTimeLvl2 = (clearedMazesLvl2 * averageTimeLvl2 + elapsedTime) / (clearedMazesLvl2 + 1);
        }
    }

    public static void updateStatisticsFile(int level) {
        LocalDate date = LocalDate.now();
        boolean dateExist = false;

        //Get data from file
        JSONArray data = getDataFromFile();

        if (data != null) {

            //Iterates the JSONObjects in the JSONArray
            for (JSONObject dataElement : (Iterable<JSONObject>) data) {
                // If the current date is in the file update data
                if (date.toString().equals(dataElement.get("Date"))) {
                    dateExist = true;
                    updateDataElement(dataElement, level);
                    break;
                }
            }

            if (!dateExist) {
                data.add(generateData(level));
            }

            //Write data to file
            try (FileWriter file = new FileWriter(System.getProperty("user.dir") + "/statistics.txt")) {
                file.write(data.toJSONString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static JSONObject generateData(int level) {
        JSONObject data = new JSONObject();

        // Put the date
        data.put("Date", LocalDate.now().toString());

        JSONObject level1 = new JSONObject();
        JSONObject level2 = new JSONObject();
        if (level == 1) {
            // Create JSONArray from level 1 statistical information
            createLevelInformation(clearedMazesLvl1, totalPlayedMazesLvl1, averageTriesLvl1, averageTimeLvl1, level1);
            createLevelInformation(0, 0, 0, 0, level2);
        } else {
            createLevelInformation(0, 0, 0, 0, level1);
            createLevelInformation(clearedMazesLvl2, totalPlayedMazesLvl2, averageTriesLvl2, averageTimeLvl2, level2);
        }
        data.put("Level 1", level1);
        data.put("Level 2", level2);

        return data;
    }

    private static void createLevelInformation(int clearedMazes, int totalPlayedMazes, double averageTries, double averageTime, JSONObject level) {
        level.put("Success Rate", String.format("%.2f", (double) clearedMazes / totalPlayedMazes * 100) + " %");
        level.put("Average tries per maze", String.format("%.2f", averageTries));
        level.put("Average time per maze", String.format("%.2f", averageTime));
    }

    private static void updateDataElement(JSONObject dataElement, int level) {
        if (level == 1) {
            JSONObject level1 = (JSONObject) dataElement.get("Level 1");
            updateLevelInformation(clearedMazesLvl1, totalPlayedMazesLvl1, averageTriesLvl1, averageTimeLvl1, level1);
        } else {
            JSONObject level2 = (JSONObject) dataElement.get("Level 2");
            updateLevelInformation(clearedMazesLvl2, totalPlayedMazesLvl2, averageTriesLvl2, averageTimeLvl2, level2);
        }
    }

    private static void updateLevelInformation(int clearedMazes, int totalPlayedMazes, double averageTries, double averageTime, JSONObject level) {
        level.replace("Success Rate", String.format("%.2f", (double) clearedMazes / totalPlayedMazes * 100) + " %");
        level.replace("Average tries per maze", String.format("%.2f", averageTries));
        level.replace("Average time per maze", String.format("%.2f", averageTime));
    }

    private static JSONArray getDataFromFile() {
        //Parses data from file to JSONArray
        try {
            JSONParser parser = new JSONParser();
            Object getData = parser.parse(new FileReader(System.getProperty("user.dir") + "/statistics.txt"));
            return (JSONArray) getData;
        } catch (ParseException | IOException e) {
            return new JSONArray();
        }
    }
}
