package com.booking.utilities;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.booking.base.BaseClass;

/**
 * CommonUtilities: Additional utility methods for common operations
 */
public class CommonUtilities {
    private static final Logger LOGGER = LogManager.getLogger(CommonUtilities.class);
    private static WebDriver driver = BaseClass.getDriver();
   
    /**
     * generateRandomRoomId() method is used to generate random no.
     *
     * @return String
     */
    public String generateRandomRoomId() {

        final Random random = new Random();
        return String.valueOf(2000 + random.nextInt(900));
    }



}
