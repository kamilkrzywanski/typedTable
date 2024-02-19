package org.krzywanski;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * This class is responsible for translating the resource bundles for labels and other properties
 */
public class BundleTranslator {
    final Map<String, String> bundleMap;

    public BundleTranslator(Locale locale, List<String> resourceBundles) {
        bundleMap = new java.util.HashMap<>();
        for (String resourceBundle : resourceBundles) {
            java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle(resourceBundle, locale);
            for (String key : bundle.keySet()) {
                bundleMap.put(key, bundle.getString(key));
            }
        }
    }

    public String getTranslation(String key) {
        return bundleMap.getOrDefault(key, key);
    }
}
