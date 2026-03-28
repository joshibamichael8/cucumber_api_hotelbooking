package com.booking.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import com.booking.utilities.ConfigReader;
import java.time.Duration;

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
