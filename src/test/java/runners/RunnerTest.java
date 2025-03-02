package runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        tags = "@test1",
        plugin = {
                "pretty",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
//                "json:target/reports/cucumber-json-reports/json-report.json",
//                "html:target/reports/cucumber-html-reports/html-report.html"
        },
        features = {"src/test/java/testcases/features"},
        glue = {"testcases.stepDefinitions", "testbase"},
        monochrome = true
)
public class RunnerTest {
}
