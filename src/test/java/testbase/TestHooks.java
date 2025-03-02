package testbase;


import io.cucumber.java.*;
import org.openqa.selenium.WebDriver;
import testbase.ScenarioContext;
import testbase.StaticObjectRepo;
import testbase.TestInitialise;

public class TestHooks extends TestInitialise {

        WebDriver driver;

        @Before()
        public void TestInitialise(Scenario scenario) {
            try
            {
                InitialiseTest();
                ExtractScenarioName(scenario);
                StaticObjectRepo.sContext = new ScenarioContext(driver);
            }
            catch (Exception e)
            {
                System.out.println("Exception occured during Test Initialisation!! \n\n {0}" + e);
                CloseBrowser();
            }

        }

        @After(order=1)
        public void teardown() {
            //System.out.println("\n I am inside teardown");
            System.out.println("\n Closing browser.......");
            CloseBrowser();
        }

        @BeforeStep
        public void beforeSteps(Scenario scenario) {
            //System.out.println("I am inside beforeSteps ----");
            System.out.println("Executing scenario: " + scenario.getName() + " ----");
        }

        @AfterStep
        public void afterSteps(Scenario scenario) {
            //System.out.println("I am inside afterSteps ==== \n");
            AttachScreenshot();
        }

        @BeforeAll
        public static void before_all() {
            //System.out.println(" I am inside before All ----");
            SetEnvironment();
            //InitialiseReport();
        }

        @AfterAll
        public static void after_all() {
            //System.out.println(" I am inside after All ----");
            //WriteToReport();
            UpdateReportProps();

        }


}
