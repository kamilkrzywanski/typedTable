package org.krzywanski;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class TypedFrameworkConfiguration {
    public static final List<String> resourceBundles = new ArrayList<>();

    /**
     * List of field resolvers - to resolve field names
     */
    public static final List<Function<Field, String>> fieldResolvers = new LinkedList<>();

    public static void addResourceBundle(String resourceBundle) {
        resourceBundles.add(resourceBundle);
    }

    public static void addFieldResolver(Function<Field, String> fieldResolver) {
        fieldResolvers.add(fieldResolver);
    }

}
