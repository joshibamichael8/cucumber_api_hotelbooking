package com.booking.utilities;

public class TokenManager {

        private static String authToken;
      
        public static void setAuthToken(String token) {
            authToken = token;
        }
        public static String getAuthToken() {
            return authToken;
        }
        public static void clearAuthToken() {
            authToken = null;
        }


}
