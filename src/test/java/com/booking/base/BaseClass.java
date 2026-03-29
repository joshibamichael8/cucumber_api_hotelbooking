package com.booking.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

/**
 * BaseClass: Contains common setup and teardown logic
 * This class manages WebDriver initialization and cleanup
 */
public class BaseClass {

    public static WebDriver driver;
    public static final Logger LOGGER = LogManager.getLogger(BaseClass.class);
    
    /**
     * Get current WebDriver instance
     */
    public static WebDriver getDriver() {
        return driver;
    }

    
    
}
