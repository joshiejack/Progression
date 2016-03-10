package joshie.progression.helpers;

public class SplitHelper {
    public static String[] splitStringEvery(String string, int interval) {
        int arrayLength = (int) Math.ceil(((string.length() / (double) interval)));
        String[] result = new String[arrayLength];

        int j = 0;
        int lastIndex = result.length - 1;
        for (int i = 0; i < lastIndex; i++) {
            result[i] = string.substring(j, j + interval);
            j += interval;
        }

        result[lastIndex] = string.substring(j);
        return result;
    }
}
