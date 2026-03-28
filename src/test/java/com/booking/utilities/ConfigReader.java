package com.booking.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Properties;


public class ConfigReader {

    
    private static final Logger LOGGER = LogManager.getLogger(ConfigReader.class);
    private static Properties properties;

    static {
        loadProperties();
    }

    /**
     * Load properties from application.properties file
     */
    private static void loadProperties() {
        properties = new Properties();
        try {
            FileInputStream fileInputStream = new FileInputStream("src/test/resources/config/application.properties");
            properties.load(fileInputStream);
            LOGGER.info("Properties file loaded successfully");
            fileInputStream.close();
        } catch (IOException e) {
            LOGGER.error("Error loading properties file: " + e.getMessage(), e);
        }
    }

    /**
     * Get property value by key
     */
    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            LOGGER.warn("Property not found: " + key);
        }
        return value;
    }

    /**
     * Get property value by key with default value
     */
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Get property as integer
     */
    public static int getIntProperty(String key, int defaultValue) {
        String value = properties.getProperty(key);
        if (value == null || value.isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            LOGGER.warn("Cannot parse property as integer: " + key);
            return defaultValue;
        }
    }

    /**
     * Get property as boolean
     */
    public static boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = properties.getProperty(key);
        if (value == null || value.isEmpty()) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }

    /**
     * Reload properties
     */
    public static void reloadProperties() {
        loadProperties();
        LOGGER.info("Properties reloaded");
    }

}
