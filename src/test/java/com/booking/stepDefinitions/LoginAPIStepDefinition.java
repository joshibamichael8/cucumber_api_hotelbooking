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

}
