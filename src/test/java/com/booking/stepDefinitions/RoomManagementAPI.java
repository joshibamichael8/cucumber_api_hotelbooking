package com.booking.stepDefinitions;

import com.booking.utilities.APIUtility;
import com.booking.utilities.CommonUtilities;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

public class RoomManagementAPI {

    private static final Logger LOGGER = LogManager.getLogger(RoomManagementAPI.class);
    private APIUtility apiUtility;
    private CommonUtilities commonUtilities;
    private Response response;


    public RoomManagementAPI() {
        this.apiUtility = new APIUtility();
        this.commonUtilities = new CommonUtilities();
    }

    @When("User requests to get list of all rooms")
    public void User_requests_to_get_list_of_all_rooms() {
         LOGGER.info("Step: Requesting list of all rooms");
         response = apiUtility.get("/room");
         System.out.println(response.getBody().asString());
         LOGGER.info("API Response Status: " + response.getStatusCode());
    }

    @Then("Response should contain list of rooms")
    public void Response_should_contain_list_of_rooms() {
        LOGGER.info("Step: Verifying response contains list of rooms");
        try {
            int roomCount = response.jsonPath().getList("rooms").size();
            Assert.assertTrue(roomCount > 0, "Response should contain at least one room");
            LOGGER.info("Response contains " + roomCount + " rooms");
        } catch (Exception e) {
            Assert.fail("Response does not contain valid room list: " + e.getMessage());
        }
    }

    @Then("Each room should have room details")
    public void Each_room_should_have_room_details() {
        LOGGER.info("Step: Verifying each room has required details");
        // Verify response body contains room information
        Assert.assertNotNull(response.getBody(), "Response body should not be null");
        LOGGER.info("Room details verified");
    }

    @When("User requests to get room details for roomid {string}")
    public void User_requests_to_get_room_details_for_roomid(String roomId) {
        LOGGER.info("Step: Requesting room details for roomid: " + roomId);
        response = apiUtility.get("/room/" + roomId);
        LOGGER.info("API Response Status: " + response.getStatusCode());
    }

    @Then("Response should contain room information")
    public void Response_should_contain_room_information() {
        LOGGER.info("Step: Verifying response contains room information");
        String responseBody = response.getBody().asString();
        Assert.assertNotNull(responseBody, "Response body should not be null");
        Assert.assertFalse(responseBody.isEmpty(), "Response body should not be empty");
        LOGGER.info("Room information verified");
    }

    @Then("Room details should contain {string}")
    public void Room_details_should_contain(String fieldName) {
        LOGGER.info("Step: Verifying room details contains field: " + fieldName);
        try {
            String value = response.jsonPath().getString(fieldName);
            Assert.assertNotNull(value, "Field " + fieldName + " should not be null");
            LOGGER.info("Field " + fieldName + " found in response");
        } catch (Exception e) {
            Assert.fail("Field " + fieldName + " not found in response");
        }
    }

    @Then("API response status code of RM should be {int}")
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

    @Then("Response should indicate room not found {string}")
    public void Response_should_indicate_room_not_found(String errorMessage) {
        LOGGER.info("Step: Verifying response indicates room not found");
        try{
            String statusLine = response.getStatusLine();
            Assert.assertTrue(statusLine.contains(errorMessage), "Expected status line to contain: " + errorMessage + " but was: " + statusLine);
            LOGGER.info("Room not found error verified with status: " + statusLine);
        } catch (Exception e) {
            Assert.fail("Could not verify room not found error: " + e.getMessage());
        }
    }

    @When("User requests to check room availability with checkin {string} and checkout {string}")
    public void User_requests_to_check_room_availability_with_checkin_and_checkout(String checkin, String checkout) {
        LOGGER.info("Step: Checking availability for checkin: " + checkin + " checkout: " + checkout);
        response = apiUtility.getWithQueryParams("/room", "checkin", checkin, "checkout", checkout);
        LOGGER.info("API Response Status: " + response.getStatusCode());
    }

    @Then("Response should contain available rooms")
    public void Response_should_contain_available_rooms() {
        try {
            LOGGER.info("Step: Verifying response contains available rooms");
            Object rooms = response.jsonPath().get("$");
            Assert.assertNotNull(rooms, "Response should contain available rooms");
            LOGGER.info("Available rooms found in response");
        } catch (Exception e) {
            Assert.fail("Response does not contain available rooms: " + e.getMessage());
        }
    }

    @Then("Available rooms list should not be empty")
    public void Available_rooms_list_should_not_be_empty() {
        LOGGER.info("Step: Verifying rooms list is not empty");
        try {
            int roomCount = response.jsonPath().getList("rooms").size();
            Assert.assertTrue(roomCount > 0, "Rooms list should not be empty");
            LOGGER.info("Rooms list contains " + roomCount + " rooms");
        } catch (Exception e) {
            Assert.fail("Error verifying rooms list: " + e.getMessage());
        }
    }

}
