package org.krzywanski;

import java.util.ArrayList;
import java.util.List;

public class TypedFrameworkConfiguration {
    public static final List<String> resourceBundles = new ArrayList<>();

    public static void addResourceBundle(String resourceBundle) {
        resourceBundles.add(resourceBundle);
    }
}
