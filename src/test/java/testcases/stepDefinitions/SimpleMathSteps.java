package testcases.stepDefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import static testbase.PageObjectRepo.superCalculatorPage;

import testbase.BasePage;
import testbase.ScenarioContext;
import testbase.StaticObjectRepo;
import testcases.pages.SuperCalculatorPage;

import java.io.IOException;

public class SimpleMathSteps {

    //private static ScenarioContext sContext;

    public SimpleMathSteps()
    {

    }

    @Given("I navigate to the url")
    public void iNavigateToTheUrl() {
        BasePage basePage = null;
        try {
            basePage = new BasePage(StaticObjectRepo.Driver);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (basePage != null)
            basePage.GoToURL();
        else
            throw new RuntimeException("Base page is not not initialised!!");

    }

    @And("I have a new calculator")
    public void iHaveANewCalculator() throws IOException {
        superCalculatorPage = new SuperCalculatorPage(StaticObjectRepo.Driver);
    }


    @When("I add {string} and {string} for {string}")
    public void iAddAndFor(String first, String second, String isAngular) throws InterruptedException {
        superCalculatorPage.Add(first, second, isAngular);
        Thread.sleep(5000);
    }

    @Then("the latest result should be {string} for {string}")
    public void theLatestResultShouldBeFor(String expectedResult, String isAngular) {
        if(isAngular.equalsIgnoreCase("true")) {
            //Assert.assertEquals(expectedResult, superCalculatorPage.LatestResultNg, "Latest results does not match exp");
            Assert.assertEquals("Latest results does not match exp", expectedResult, superCalculatorPage.LatestResult());
        }
        else
            Assert.assertEquals("Latest results does not match exp", String.valueOf(expectedResult), superCalculatorPage.LatestResult());
    }

    @When("I divide {string} by {string} for {string}")
    public void iDivideByFor(String first, String second, String isAngular) throws InterruptedException {
        superCalculatorPage.Divide(first, second, isAngular);
        Thread.sleep(5000);
    }
}
