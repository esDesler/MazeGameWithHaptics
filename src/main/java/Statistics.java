public class Statistics {
    private static int clearedMazesLvl1;
    private static int totalPlayedMazesLvl1;
    private static int triesOnCurrentMazeLvl1;
    private static double averageTriesLvl1 = 1;
    private static int clearedMazesLvl2;
    private static int totalPlayedMazesLvl2;
    private static int triesOnCurrentMazeLvl2;
    private static double averageTriesLvl2;

    public static int getClearedMazesLvl1() {
        return clearedMazesLvl1;
    }

    public static void incrementClearedMazesLvl1() {
        clearedMazesLvl1++;
    }

    public static int getTotalPlayedMazesLvl1() {
        return totalPlayedMazesLvl1;
    }

    public static void incrementTotalPlayedMazesLvl1() {
        totalPlayedMazesLvl1++;
    }

    public static void resetTriesOnCurrentMazeLvl1() {
        triesOnCurrentMazeLvl1 = 0;
    }

    public static void incrementTriesOnCurrentMazeLvl1() {
        triesOnCurrentMazeLvl1++;
    }

    public static double getAverageTriesLvl1() {
        return averageTriesLvl1;
    }

    public static void updateAverageTriesLvl1() {
        averageTriesLvl1 = (clearedMazesLvl1 * averageTriesLvl1 + triesOnCurrentMazeLvl1) / (clearedMazesLvl1 + 1);
    }

    public static int getClearedMazesLvl2() {
        return clearedMazesLvl2;
    }

    public static void incrementClearedMazesLvl2() {
        clearedMazesLvl2++;
    }

    public static int getTotalPlayedMazesLvl2() {
        return totalPlayedMazesLvl2;
    }

    public static void incrementTotalPlayedMazesLvl2() {
        totalPlayedMazesLvl2++;
    }

    public static int getTriesOnCurrentMazeLvl2() {
        return triesOnCurrentMazeLvl2;
    }

    public static void incrementTriesOnCurrentMazeLvl2() {
        triesOnCurrentMazeLvl2++;
    }

    public static double getAverageTriesLvl2() {
        return averageTriesLvl2;
    }

    public static void incrementAverageTriesLvl2() {
        averageTriesLvl2++;
    }

    public static int getTriesOnCurrentMazeLvl1() {
        return triesOnCurrentMazeLvl1;
    }
}
