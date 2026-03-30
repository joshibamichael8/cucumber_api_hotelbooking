package com.booking.runners;


import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.TestNG;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * DirectTestRunner: Direct execution of Cucumber tests via TestNG programmatically
 * This bypasses Maven Surefire provider detection issues
 */

@CucumberOptions(
        features = "src/test/resources/features/getBookingReport.feature",
        glue = {"com.booking.stepDefinitions", "com.booking.hooks"},
        plugin = {
                "pretty",
                "html:reports/cucumber-report.html",
                "json:reports/cucumber.json",
                "junit:reports/cucumber.xml"
        },
        tags = "@GBR or @BR_1",
        dryRun = false,
        monochrome = true,
        publish = true
)
public class DirectTestRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }

    @Test(dataProvider = "scenarios")
    public void runScenario(io.cucumber.testng.PickleWrapper pickle, io.cucumber.testng.FeatureWrapper feature) {
        super.runScenario(pickle, feature);
    }

    public static void main(String[] args) {
        TestNG testNG = new TestNG();
        testNG.setTestClasses(new Class[]{DirectTestRunner.class});
        testNG.setDefaultSuiteName("CucumberSuite");
        testNG.setDefaultTestName("CucumberTests");
        testNG.run();
    }
}
