package com.booking.stepDefinitions;


import com.booking.utilities.APIUtility;
import com.booking.utilities.CommonUtilities;
import com.booking.utilities.TokenManager;

import io.cucumber.java.en.*;
import io.restassured.response.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import io.cucumber.datatable.DataTable;

    
public class E2EHotelBooking {

    private static final Logger LOGGER = LogManager.getLogger(RoomManagementAPI.class);
    private APIUtility apiUtility;
    private Response response;
    private int booking_statusCode;
    private Response bookingResponse;
    private Response updateResponse;
    private CommonUtilities commonUtilities;

    private Map<String, String> bookingData;
    private String bookingId;


    public E2EHotelBooking() {
        this.apiUtility = new APIUtility();
        this.commonUtilities = new CommonUtilities();
        this.bookingData = new HashMap<>();
    }

    @When("User authenticates as user credentials:")
    public void User_authenticates_as_admin_credentials(DataTable dataTable) {
        List<Map<String, String>> credentials = dataTable.asMaps(String.class, String.class);
        String username = credentials.get(0).get("username");
        String password = credentials.get(0).get("password");
        Map<String, String> authBody = new HashMap<>();
        authBody.put("username", username);
        authBody.put("password", password);
        response = apiUtility.post("/auth/login", authBody);
        apiUtility.setResponse(response);
        apiUtility.setStatusCode(response.getStatusCode());
        LOGGER.info("API Response Status: " + response.getStatusCode());
    }

    @When("User stores the authentication token")
    public void User_stores_the_authentication_token() {
        LOGGER.info("Step: Storing authentication token from response");
        try {

            String token = response.jsonPath().getString("token");
            
            Assert.assertNotNull(token, "Authentication token should not be null");
            Assert.assertFalse(token.isEmpty(), "Authentication token should not be empty");
            
            TokenManager.setAuthToken(token);
            // Also set token in APIUtility for subsequent requests
            apiUtility.setAuthToken(token);
            LOGGER.info("Authentication token stored successfully");
        } catch (Exception e) {
            LOGGER.error("Error storing authentication token: " + e.getMessage());
            Assert.fail("Could not store authentication token: " + e.getMessage());
        }
    }

    @Then("API response status code for E2E should be {int}")
    public void API_response_status_code_for_E_E_should_be(int expectedStatus) {
         LOGGER.info("Step: Verifying response status code is " + expectedStatus);
         int actualStatus = response.getStatusCode();
         apiUtility.setStatusCode(actualStatus);
        
        Assert.assertEquals(actualStatus, expectedStatus, 
            "Response status code mismatch. Expected: " + expectedStatus + 
            " Actual: " + response.getStatusCode() + 
            " Body: " + response.getBody().asString());
        LOGGER.info("Status code verified successfully");
    }

    @When("User enters search criteria with checkin {string} and checkout {string}")
    public void User_enters_search_criteria_with_checkin_and_checkout(String checkin, String checkout) {
        LOGGER.info("Step: User enters search criteria with checkin: " + checkin + " and checkout: " + checkout);
        // Store search criteria for later use
        Map<String, String> searchCriteria = new HashMap<>();
        searchCriteria.put("checkin", checkin);
        searchCriteria.put("checkout", checkout);
        bookingData.putAll(searchCriteria);
        LOGGER.info("Search criteria stored successfully");
    }

    private Map<String, Object> prepareBookingRequestBody(Map<String, String> bookingDetails) {
        Map<String, Object> requestBody = new HashMap<>();
        
        requestBody.put("roomid", Integer.parseInt(bookingDetails.get("roomid")));
        requestBody.put("firstname", bookingDetails.get("firstname"));
        requestBody.put("lastname", bookingDetails.get("lastname"));
        requestBody.put("email", bookingDetails.get("email"));
        requestBody.put("phone", bookingDetails.get("phone"));
        requestBody.put("depositpaid", Boolean.parseBoolean(bookingDetails.get("depositpaid")));
        
        Map<String, String> bookingdates = new HashMap<>();
        bookingdates.put("checkin", bookingDetails.get("checkin"));
        bookingdates.put("checkout", bookingDetails.get("checkout"));
        
        requestBody.put("bookingdates", bookingdates);
        
        return requestBody;
    }

    @When("User creates a new booking with following details and submits the booking:")
    public void User_creates_a_new_booking_with_following_details_and_submits_the_booking(DataTable dataTable) {
        Map<String, String> bookingDetailsList = new HashMap<>(dataTable.asMap(String.class, String.class));
        LOGGER.info("Step: Creating new booking with details: " + bookingDetailsList);
        

        // Store the booking data for later use
        String firstname = bookingDetailsList.get("firstname");
        String lastname = bookingDetailsList.get("lastname");
        String email = bookingDetailsList.get("email");
        String phone = bookingDetailsList.get("phone");
        String checkin = bookingDetailsList.get("checkin");
        String checkout = bookingDetailsList.get("checkout");
        String depositpaid = bookingDetailsList.get("depositpaid");
        String roomid = commonUtilities.generateRandomRoomId();
        bookingDetailsList.put("roomid", roomid);

        this.bookingData.put("firstname", firstname);
        this.bookingData.put("lastname", lastname);
        this.bookingData.put("email", email);
        this.bookingData.put("phone", phone);
        this.bookingData.put("roomid", roomid);
        this.bookingData.put("checkin", checkin);
        this.bookingData.put("checkout", checkout);
        this.bookingData.put("depositpaid", depositpaid);

        // Prepare the request body
        Map<String, Object> requestBody = prepareBookingRequestBody(bookingDetailsList);
        
        System.out.println("Authentication Token: Cookie , token=" + TokenManager.getAuthToken());
        System.out.println("requestBody: " + requestBody);
        System.out.println("Endpoint: reservation/" + roomid + "?checkin=" + checkin + "&checkout=" + checkout);

        bookingResponse = apiUtility.postWithContentTypeAuthSetUp(requestBody, "Cookie", "token=" + TokenManager.getAuthToken(), "Authorization", "automationintesting.online");
       

        LOGGER.info("API Response Status: " + bookingResponse.getStatusCode());
        
        // Extract and store booking ID if available
        booking_statusCode = bookingResponse.getStatusCode();
        if (booking_statusCode == 201) {
            try {
                this.bookingId = bookingResponse.jsonPath().getString("bookingid");
                LOGGER.info("Booking ID extracted: " + bookingId);
            } catch (Exception e) {
                LOGGER.warn("Could not extract booking ID from response");
            }
        }else {
            LOGGER.warn("Booking creation failed with status code: " + booking_statusCode);
        }
    }

    private Map<String, Object> prepareBookingUpdateBodyForId(String bookingId, Map<String, String> updateDetails) {
        Map<String, Object> requestBody = new HashMap<>();
        
        requestBody.put("bookingid", Integer.parseInt(bookingId));
        requestBody.put("roomid", updateDetails.containsKey("roomid") ? Integer.parseInt(updateDetails.get("roomid")) : 1);
        requestBody.put("firstname", updateDetails.getOrDefault("firstname", ""));
        requestBody.put("lastname", updateDetails.getOrDefault("lastname", ""));
        requestBody.put("depositpaid", Boolean.parseBoolean(updateDetails.getOrDefault("depositpaid", "false")));
        
        System.out.println(requestBody);
        Map<String, String> bookingdates = new HashMap<>();
        String checkin = updateDetails.getOrDefault("checkin", "2025-07-17");
        String checkout = updateDetails.getOrDefault("checkout", "2025-07-18");
        bookingdates.put("checkin", checkin);
        bookingdates.put("checkout", checkout);
        
        requestBody.put("bookingdates", bookingdates);
        
        return requestBody;
    }

    @Then("API response status code should be {int} for booking creation")
    public void API_response_status_code_should_be_for_booking_creation(int expectedStatus) {
        LOGGER.info("Step: Verifying response status code is " + expectedStatus);
         int actualStatus = booking_statusCode;
         apiUtility.setStatusCode(actualStatus);
        
        Assert.assertEquals(actualStatus, expectedStatus, 
            "Response status code mismatch. Expected: " + expectedStatus + 
            " Actual: " + bookingResponse.getStatusCode() + 
            " Body: " + bookingResponse.getBody().asString());
        LOGGER.info("Status code verified successfully");
    }

    @Then("User should see booking confirmation page with booking ID displayed")
    public void User_should_see_booking_confirmation_page_with_booking_ID_displayed() {
        Assert.assertTrue(bookingResponse.getBody().asString().contains("bookingid"),
                "Response does not indicate missing parameter. Actual response: " + bookingResponse.getBody().asString());
   
    }

    @When("User attempts to update booking ID with following details:")
    public void User_attempts_to_update_booking_ID_with_following_details(DataTable dataTable) {
         LOGGER.info("Step: Attempting to update booking with ID: " + bookingId);
       
       try {
       List<Map<String, String>> bookingDetailsList = dataTable.asMaps(String.class, String.class);
        String bookingId = bookingResponse.getBody().jsonPath().getString("bookingid");
            Map<String, String> updateDetails = bookingDetailsList.get(0);
            
        Map<String, Object> updateBody = prepareBookingUpdateBodyForId(bookingId,   updateDetails);
        
            updateResponse = apiUtility.putWithContentTypeAuthSetUp("/booking/" + bookingId, updateBody,"Cookie", "token=" + TokenManager.getAuthToken(),"Authorization", "automationintesting.online");
            
            LOGGER.info("Update booking response status: " + updateResponse.getStatusCode());
        } catch (Exception e) {
            LOGGER.error("Error attempting to update booking: " + e.getMessage());
            Assert.fail("Could not attempt to update booking: " + e.getMessage());
        }
    }

    @Then("Booking update should be successful with status {int}")
    public void Booking_update_should_be_successful_with_status(int exceptedStatus) {
        LOGGER.info("Step: Verifying booking update response status code is " + exceptedStatus);
        int actualStatus = updateResponse.getStatusCode();
        Assert.assertEquals(actualStatus, exceptedStatus, 
            "Booking update failed. Expected status: " + exceptedStatus + 
            " Actual status: " + actualStatus + 
            " Body: " + updateResponse.getBody().asString());
        LOGGER.info("Booking update verified successfully");
    }

    @Then("Updated booking details should be reflected in system")
    public void Updated_booking_details_should_be_reflected_in_system() {
        System.out.println(updateResponse.getBody().asString());
            Assert.assertTrue(updateResponse.getBody().asString().contains("success"),
                "Response does not indicate missing parameter. Actual response: " + updateResponse.getBody().asString());

    }

    @When("User retrieves booking details using stored booking ID via API")
    public void User_retrieves_booking_details_using_stored_booking_ID_via_API() {
        LOGGER.info("Step: Retrieving booking details using stored booking ID: " + bookingId);
        try {
            response = apiUtility.getWithHeader("/booking/" + bookingId, "Cookie", "token=" + TokenManager.getAuthToken());
            LOGGER.info("Retrieve booking response status: " + response.getStatusCode());
        } catch (Exception e) {
            LOGGER.error("Error retrieving booking details: " + e.getMessage());
            Assert.fail("Could not retrieve booking details: " + e.getMessage());
        }
    }

    @Then("API response should contain updated booking details with status code {int}")
    public void API_response_should_contain_updated_booking_details_with_status_code(int statusCode) {
        LOGGER.info("Step: Verifying API response contains updated booking details with status code " + statusCode);
        int actualStatus = response.getStatusCode();
        Assert.assertEquals(actualStatus, statusCode, 
            "Response status code mismatch. Expected: " + statusCode + 
            " Actual: " + actualStatus + 
            " Body: " + response.getBody().asString());
        
        // Additional assertions to verify updated details in response body
        try {
            String responseBody = response.getBody().asString();
            Assert.assertTrue(responseBody.contains("firstname"), "Response should contain firstname");
            Assert.assertTrue(responseBody.contains("lastname"), "Response should contain lastname");
            Assert.assertTrue(responseBody.contains("bookingdates"), "Response should contain bookingdates");
            LOGGER.info("Updated booking details verified in response body");
        } catch (Exception e) {
            LOGGER.error("Error verifying updated booking details in response: " + e.getMessage());
            Assert.fail("Could not verify updated booking details in response: " + e.getMessage());
        }
    }

    @When("User attempts to cancel booking with ID via API")
    public void User_attempts_to_cancel_booking_with_ID_via_API() {
        LOGGER.info("Step: Attempting to cancel booking with ID: " + bookingId);
        try {
            response = apiUtility.deleteWithHeader("/booking/" + bookingId, "Cookie", "token=" + TokenManager.getAuthToken());
            LOGGER.info("Cancel booking response status: " + response.getStatusCode());
        } catch (Exception e) {
            LOGGER.error("Error attempting to cancel booking: " + e.getMessage());
            Assert.fail("Could not attempt to cancel booking: " + e.getMessage());
        }
    }


    @Then("Booking cancellation should be successful with status {int}")
    public void Booking_cancellation_should_be_successful_with_status(int statusCode) {
         LOGGER.info("Step: Verifying booking cancellation response status code is " + statusCode);
        int actualStatus = response.getStatusCode();
        Assert.assertEquals(actualStatus, statusCode, 
            "Booking cancellation failed. Expected status: " + statusCode + 
            " Actual status: " + actualStatus + 
            " Body: " + response.getBody().asString());
        LOGGER.info("Booking cancellation verified successfully");
    }

    @Then("API response should return {int} when retrieving cancelled booking details")
    public void API_response_should_return_when_retrieving_cancelled_booking_details(int expCode) {
        LOGGER.info("Step: Verifying API response when retrieving cancelled booking details");
        try {
            response = apiUtility.getWithHeader("/booking/" + bookingId, "Cookie", "token=" + TokenManager.getAuthToken());
            int actualStatus = response.getStatusCode();
            Assert.assertEquals(actualStatus, expCode, 
                "Expected status code " + expCode + " for cancelled booking, but got: " + actualStatus + 
                " Body: " + response.getBody().asString());
            LOGGER.info("Verified that cancelled booking cannot be retrieved");
        } catch (Exception e) {
            LOGGER.error("Error verifying retrieval of cancelled booking details: " + e.getMessage());
            Assert.fail("Could not verify retrieval of cancelled booking details: " + e.getMessage());
        }
    }

    @Then("Booking should contain all submitted details")
    public void Booking_should_contain_all_submitted_details() {
        LOGGER.info("Step: Verifying booking contains all submitted details");
        try {
            String responseBody = bookingResponse.getBody().asString();
            
            // Verify all submitted booking details are present in response
            // API returns: bookingid, roomid, firstname, lastname, depositpaid, bookingdates
            Assert.assertTrue(responseBody.contains(bookingData.get("firstname")), 
                "Response should contain firstname: " + bookingData.get("firstname"));
            Assert.assertTrue(responseBody.contains(bookingData.get("lastname")), 
                "Response should contain lastname: " + bookingData.get("lastname"));
            Assert.assertTrue(responseBody.contains(bookingData.get("checkin")), 
                "Response should contain checkin date: " + bookingData.get("checkin"));
            Assert.assertTrue(responseBody.contains(bookingData.get("checkout")), 
                "Response should contain checkout date: " + bookingData.get("checkout"));
            
            // Verify depositpaid field
            String depositpaid = bookingData.get("depositpaid");
            Assert.assertTrue(responseBody.contains("\"depositpaid\":" + depositpaid.toLowerCase()), 
                "Response should contain depositpaid: " + depositpaid);
            
            // Note: API does not return email and phone fields in booking response
            // so we only verify the fields that are present in the response
            
            LOGGER.info("All booking details verified successfully in response");
        } catch (Exception e) {
            LOGGER.error("Error verifying booking details in response: " + e.getMessage());
            Assert.fail("Could not verify booking details in response: " + e.getMessage());
        }
    }

    @When("User attempts to create a new booking with missing required fields:")
    public void User_attempts_to_create_a_new_booking_with_missing_required_fields(DataTable dataTable) {
        LOGGER.info("Step: Attempting to create a new booking with missing required fields");
        try {
            Map<String, String> bookingDetailsList = new HashMap<>(dataTable.asMap(String.class, String.class));
            LOGGER.info("Step: Creating new booking with details: " + bookingDetailsList);
            
            // Extract booking details
            String firstname = bookingDetailsList.get("firstname");
            String lastname = bookingDetailsList.get("lastname");
            String email = bookingDetailsList.get("email");
            String phone = bookingDetailsList.get("phone");
            String checkin = bookingDetailsList.get("checkin");
            String checkout = bookingDetailsList.get("checkout");
            String depositpaid = bookingDetailsList.get("depositpaid");
            String roomid = commonUtilities.generateRandomRoomId();
            
            // Store booking data for later verification
            this.bookingData.put("firstname", firstname != null ? firstname : "");
            this.bookingData.put("lastname", lastname != null ? lastname : "");
            this.bookingData.put("email", email != null ? email : "");
            this.bookingData.put("phone", phone != null ? phone : "");
            this.bookingData.put("roomid", roomid);
            this.bookingData.put("checkin", checkin);
            this.bookingData.put("checkout", checkout);
            this.bookingData.put("depositpaid", depositpaid);

            // Prepare the request body with nested bookingdates
            Map<String, Object> requestBody = new HashMap<>();
            if (roomid != null && !roomid.isEmpty()) {
                requestBody.put("roomid", Integer.parseInt(roomid));
            }
            if (firstname != null && !firstname.isEmpty()) {
                requestBody.put("firstname", firstname);
            }
            if (lastname != null && !lastname.isEmpty()) {
                requestBody.put("lastname", lastname);
            }
            if (email != null && !email.isEmpty()) {
                requestBody.put("email", email);
            }
            if (phone != null && !phone.isEmpty()) {
                requestBody.put("phone", phone);
            }
            if (depositpaid != null && !depositpaid.isEmpty()) {
                requestBody.put("depositpaid", Boolean.parseBoolean(depositpaid));
            }

            // Create nested bookingdates object
            Map<String, String> bookingdates = new HashMap<>();
            bookingdates.put("checkin", checkin);
            bookingdates.put("checkout", checkout);
            requestBody.put("bookingdates", bookingdates);

            LOGGER.info("Request body: " + requestBody.toString());
            LOGGER.info("Endpoint: /booking");

            try {
                // Make POST request to create booking with missing/invalid fields
                bookingResponse = apiUtility.postWithContentTypeAuthSetUp(requestBody, "Cookie", "token=" + TokenManager.getAuthToken(), "Authorization", "automationintesting.online");
                booking_statusCode = bookingResponse.getStatusCode();
                
                LOGGER.info("API Response Status: " + booking_statusCode);
                LOGGER.info("API Response Body: " + bookingResponse.getBody().asString());
            } catch (Exception e) {
                LOGGER.error("Error creating booking: " + e.getMessage());
                Assert.fail("Failed to create booking: " + e.getMessage());
            }
        } catch (Exception e) {
            LOGGER.error("Error attempting to create booking with missing fields: " + e.getMessage());
            Assert.fail("Could not attempt to create booking with missing fields: " + e.getMessage());
        }
    }

    @Then("API response status code should be {int} for bad request")
    public void API_response_status_code_should_be_for_bad_request(int expStatusCode) {
        LOGGER.info("Step: Verifying response status code for bad request is " + expStatusCode);
        int actualStatus = bookingResponse.getStatusCode();
        Assert.assertEquals(actualStatus, expStatusCode, 
            "Expected status code " + expStatusCode + " for bad request, but got: " + actualStatus + 
            " Body: " + bookingResponse.getBody().asString());
        LOGGER.info("Verified that API returns correct status code for bad request");
    }

    @Then("User gets {string} error message for missing or invalid input fields")
    public void User_gets_error_message_for_missing_or_invalid_input_fields(String expectedErrorMessage) {
        LOGGER.info("Step: Verifying response contains expected error message: " + expectedErrorMessage);
        try {
            String responseBody = bookingResponse.getBody().asString();
            LOGGER.info("Response Body: " + responseBody);
            
            // Check if error message is in the response (case-insensitive)
            boolean errorFound = responseBody.toLowerCase().contains(expectedErrorMessage.toLowerCase());
            
            // If not found in main response, check if 'errors' array exists and contains the message
            if (!errorFound) {
                try {
                    Object errorsObj = bookingResponse.jsonPath().get("errors");
                    if (errorsObj instanceof java.util.List) {
                        java.util.List<?> errors = (java.util.List<?>) errorsObj;
                        for (Object error : errors) {
                            String errorMsg = error.toString().toLowerCase();
                            if (errorMsg.contains(expectedErrorMessage.toLowerCase())) {
                                errorFound = true;
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    LOGGER.debug("Could not extract errors array from response");
                }
            }
            
            Assert.assertTrue(errorFound, 
                "Response should contain error message '" + expectedErrorMessage + "'. Actual response: " + responseBody);
            LOGGER.info("Verified that response contains error message for bad request");
        } catch (Exception e) {
            LOGGER.error("Error verifying error message in response: " + e.getMessage());
            Assert.fail("Could not verify error message in response: " + e.getMessage());
        }
    }


}
