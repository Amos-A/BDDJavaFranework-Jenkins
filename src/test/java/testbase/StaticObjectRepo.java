package testbase;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.paulhammant.ngwebdriver.NgWebDriver;
import io.cucumber.java.Scenario;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.devtools.v101.accessibility.Accessibility;
import java.util.Properties;

public class StaticObjectRepo {

    public static WebDriver Driver;
    public static NgWebDriver ngDriver;
    public static Scenario scenario;
    public static String FeatureName;
    public static ScenarioContext sContext;

    public static ExtentReports Reporter = new ExtentReports();

    public static String Environment;
    public static Properties prop;
    public static Properties envProp;
    public static String Error;
    public static String UserName;
    public static String Password;
    public static Accessibility Axe;
    public static String Browser;

}
