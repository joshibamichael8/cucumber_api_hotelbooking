package com.booking.runners;

import static org.junit.Assert.assertTrue;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import io.cucumber.testng.AbstractTestNGCucumberTests;
// import org.junit.Test;
import io.cucumber.testng.CucumberOptions;


/**
 * TestRunner: Cucumber test runner for TestNG
 */
@CucumberOptions(
        features = "src/test/resources/features/checkRoomAvailability.feature",
        glue = {"com.booking.stepDefinitions", "com.booking.hooks"},
        plugin = {
                "pretty",
                "html:reports/cucumber-report.html",
                "json:reports/cucumber.json",
                "junit:reports/cucumber.xml"
        },
        tags = "@RM_5",
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

    // @Test(dataProvider = "scenarios")
    // public void runScenario(Object[] scenario) {
    //     // This method will be called for each scenario
    //     // You can add any additional setup or teardown logic here if needed
    // }
    
}
