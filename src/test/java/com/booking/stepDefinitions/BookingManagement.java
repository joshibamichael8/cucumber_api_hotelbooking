package com.booking.stepDefinitions;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import com.booking.utilities.APIUtility;
import com.booking.utilities.CommonUtilities;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

public class BookingManagement {

    private static final Logger LOGGER = LogManager.getLogger(BookingManagement.class);
    private APIUtility apiUtility;
    private CommonUtilities commonUtilities;
    private Response response;
    private String bookingId;
    private Map<String, String> bookingData;
    private int statusCode;

    public BookingManagement() {
        this.apiUtility = new APIUtility();
        this.commonUtilities = new CommonUtilities();
        this.bookingData = new HashMap<>();
    }

   

}
