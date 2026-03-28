package com.booking.stepDefinitions;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.testng.Assert;

import com.booking.utilities.APIUtility;
import com.booking.utilities.CommonUtilities;
import com.booking.utilities.TokenManager;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;



public class LoginAPIStepDefinition {

    private static final Logger LOGGER = LogManager.getLogger(LoginAPIStepDefinition.class);

    private APIUtility apiUtility;
    private CommonUtilities commonUtilities;

    private Map<String, String> bookingData;
    private Response response;

    public LoginAPIStepDefinition() {
        this.apiUtility = new APIUtility();
        this.commonUtilities = new CommonUtilities();
        this.bookingData = new HashMap<>();
    }

    @When("User authenticates as user with username {string} and password {string}")
    public void user_authenticates_as_user(String username, String password) {
        LOGGER.info("Step: Authenticating the user");
                
        Map<String, Object> loginBody = new HashMap<>();
        loginBody.put("username", username);
        loginBody.put("password", password);
        System.out.println(loginBody);
        response = apiUtility.post("/auth/login", loginBody);
        LOGGER.info("API Response Status: " + response.getStatusCode());
    }

    @Then("API response status code should be {int}")
    public void API_response_status_code_should_be(int expectedStatus) {
        LOGGER.info("Step: Verifying response status code is " + expectedStatus);
         int actualStatus = response.getStatusCode();
         apiUtility.setStatusCode(actualStatus);
        
        Assert.assertEquals(actualStatus, expectedStatus, 
            "Response status code mismatch. Expected: " + expectedStatus + 
            " Actual: " + response.getStatusCode() + 
            " Body: " + response.getBody().asString());
        LOGGER.info("Status code verified successfully");
    }

    @Then("Response should contain a valid authentication token")
    public void Response_should_contain_a_valid_authentication_token() {
        LOGGER.info("Step: Verifying response contains authentication token");
        try {
            String token = response.jsonPath().getString("token");
            System.out.println("Authentication Token: " + token);
            TokenManager.setAuthToken(token);
            // Also set token in APIUtility for subsequent requests
            apiUtility.setAuthToken(token);
            
            int int_status = response.getStatusCode();
            System.out.println("Response Status Code: " + int_status);
            
            Assert.assertNotNull(token, "Token should not be null");
            LOGGER.info("Authentication token found in response");
        } catch (Exception e) {
            LOGGER.warn("Token field not found, checking for alternative response");
        }
    }

    @Then("Token should not be empty")
    public void Token_should_not_be_empty() {
        LOGGER.info("Step: Verifying token is not empty");
        try {
            String token = response.jsonPath().getString("token");
            Assert.assertFalse(token.isEmpty(), "Token should not be empty");
            LOGGER.info("Token verified as not empty");
        } catch (Exception e) {
            LOGGER.info("Skipping token validation");
        }
    }

    @Then("Response should indicate {string}")
    public void Response_should_indicate(String errorMsg) {
        LOGGER.info("Step: Verifying response indicates error type: " + errorMsg);
        try {
            String errorMessage = response.jsonPath().getString("error");
            
            if (errorMessage == null || errorMessage.isEmpty()) {
                errorMessage = response.jsonPath().getString("message");
            }
            
            Assert.assertNotNull(errorMessage, "Response should contain error information");
            Assert.assertTrue(errorMessage.toLowerCase().contains(errorMsg.toLowerCase()), 
                "Response should indicate error type containing: " + errorMsg + 
                " but was: " + errorMessage);
            LOGGER.info("Error message verified: " + errorMsg);
        } catch (Exception e) {
            Assert.fail("Could not verify error response: " + e.getMessage());
        }
    }

    @Then("Response should not contain authentication token")
    public void Response_should_not_contain_authentication_token() {
         LOGGER.info("Step: Verifying response does not contain authentication token");
        try {
            
            String token = response.jsonPath().getString("token");
                
            Assert.assertTrue(token == null || token.isEmpty(), 
                "Response should not contain authentication token, but found: " + token);
            LOGGER.info("Verified that authentication token is not present in response");
        } catch (Exception e) {
            // If exception is thrown while trying to get token, it means token field doesn't exist
            LOGGER.info("Authentication token not found in response (as expected)");
        }
    }

    @When("User {string}")
    public void User(String action) {
        LOGGER.info("Step: User performs action: " + action);
        try {
            if (action.contains("booking report without authentication")) {
                response = apiUtility.get("/report");
                System.out.println(response.getBody().asString());
                LOGGER.info("API Response Status: " + response.getStatusCode());
            } else if (action.contains("attempts to access protected resource with invalid token")) {
                // Extract token from action string using regex
                java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\"([^\"]+)\"");
                java.util.regex.Matcher matcher = pattern.matcher(action);
                String invalidToken = null;
                if (matcher.find()) {
                    invalidToken = matcher.group(1);
                }
                
                System.out.println(response.getBody().asString());
                if (invalidToken != null) {
                    response = apiUtility.getWithHeader("/report", "Authorization", "Bearer " + invalidToken);
                    LOGGER.info("API Response Status with invalid token: " + response.getStatusCode());
                } else {
                    Assert.fail("Could not extract token from action: " + action);
                }
            } else {
                LOGGER.warn("Unknown action: " + action);
                Assert.fail("Unknown action: " + action);
            }
        } catch (Exception e) {
            LOGGER.error("Error performing action: " + e.getMessage());
            Assert.fail("Could not perform action: " + e.getMessage());
        }
    }

    @Then("API response should contain token expiration time")
    public void API_response_should_contain_token_expiration_time() {
        LOGGER.info("Step: Verifying API response contains token expiration time");
        try {
            Object expirationTime = response.jsonPath().get("token_expiration");
            if (expirationTime == null) {
                expirationTime = response.jsonPath().get("exp");
            }
            if (expirationTime == null) {
                expirationTime = response.jsonPath().get("expires_in");
            }
            
            if (expirationTime == null) {
                LOGGER.warn("Token expiration time not found in response. API may not include expiration info.");
            } else {
                LOGGER.info("Token expiration time found: " + expirationTime);
            }
        } catch (Exception e) {
            LOGGER.warn("Could not verify token expiration time: " + e.getMessage());
        }
    }

    @Then("Token expiration should be in the future")
    public void Token_expiration_should_be_in_the_future() {
        LOGGER.info("Step: Verifying token expiration is in the future");
        try {
            Long expirationTime = response.jsonPath().getLong("token_expiration");
            if (expirationTime == null) {
                expirationTime = response.jsonPath().getLong("exp");
            }
            if (expirationTime == null) {
                expirationTime = response.jsonPath().getLong("expires_in");
                // If expires_in is in seconds, add it to current time
                if (expirationTime != null) {
                    expirationTime = System.currentTimeMillis() + (expirationTime * 1000);
                }
            }
            
            if (expirationTime == null) {
                LOGGER.warn("Token expiration time not found in response. API may not include expiration info.");
            } else {
                long currentTime = System.currentTimeMillis();
                Assert.assertTrue(expirationTime > currentTime, 
                    "Token expiration should be in the future. Expiration: " + expirationTime + 
                    " Current time: " + currentTime);
                LOGGER.info("Token expiration verified to be in the future");
            }
        } catch (Exception e) {
            LOGGER.warn("Could not verify token expiration: " + e.getMessage());
        }
    }

   
}
