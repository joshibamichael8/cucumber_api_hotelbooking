package com.booking.runners;

import org.testng.annotations.DataProvider;
import io.cucumber.testng.AbstractTestNGCucumberTests;
// import org.junit.Test;
import io.cucumber.testng.CucumberOptions;


/**
 * TestRunner: Cucumber test runner for TestNG
 */
@CucumberOptions(
        features = "src/test/resources/features/",
        glue = {"com.booking.stepDefinitions", "com.booking.hooks"},
        plugin = {
                "pretty",
                "html:reports/cucumber-report.html",
                "json:reports/cucumber.json",
                "junit:reports/cucumber.xml"
        },
        tags = "@Login or @checkRoomAvailability or @getRoomDetails or @createBooking or @updateBookingDetails or @cancelBooking or @getBookingDetails or @getBookingReport",
        //@Login or @checkRoomAvailability or @getRoomDetails or @createBooking or @updateBookingDetails or @cancelBooking or @getBookingDetails or @getBookingReport
        dryRun = false,
        monochrome = true,
        publish = true
)

public class TestRunner extends AbstractTestNGCucumberTests{

    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }

    
}
