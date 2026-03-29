package com.booking.utilities;

import java.util.Random;

/**
 * CommonUtilities: Additional utility methods for common operations
 */
public class CommonUtilities {
   
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
