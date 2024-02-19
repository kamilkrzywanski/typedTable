package org.krzywanski.table.utils;

import static java.util.Locale.ENGLISH;

public class ReflectionUtils {

    public static final String IS_PREFIX = "is";

    /**
     * Returns a String which capitalizes the first letter of the string.
     */
    public static String capitalize(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        return name.substring(0, 1).toUpperCase(ENGLISH) + name.substring(1);
    }
}
