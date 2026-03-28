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
        features = "src/test/resources/features/login.feature",
        glue = {"com.booking.stepDefinitions", "com.booking.hooks"},
        plugin = {
                "pretty",
                "html:reports/cucumber-report.html",
                "json:reports/cucumber.json",
                "junit:reports/cucumber.xml"
        },
        tags = "@Login",
        monochrome = true
)

public class TestRunner extends AbstractTestNGCucumberTests{

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }
}
