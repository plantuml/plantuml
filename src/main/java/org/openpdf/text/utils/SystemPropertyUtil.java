package org.openpdf.text.utils;

public class SystemPropertyUtil {

    /**
     * Similar to {@link Boolean#getBoolean(String)} but uses the given default value if property were not set.
     *
     * @param propertyKey  the system property key
     * @param defaultValue the default value to use if property is not defined.
     * @return true if the property is defined and contains "true" (ignoring case), else if system property is not set
     * then the provided defaultValue, else false.
     */
    public static boolean getBoolean(String propertyKey, boolean defaultValue) {
        String propertyValue = System.getProperty(propertyKey);
        if (propertyValue == null) {
            return defaultValue;
        }

        return Boolean.parseBoolean(propertyValue);
    }
}
