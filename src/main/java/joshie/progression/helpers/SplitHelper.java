package joshie.progression.helpers;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.text.WordUtils;

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

    public static String[] splitTooltip(String text, int value) {
        return  WordUtils.wrap(StringEscapeUtils.unescapeJava(text).replace("\r", ""), value).split("\n");
    }
}
