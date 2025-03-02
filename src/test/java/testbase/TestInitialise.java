package testbase;

import com.aventstack.extentreports.service.ExtentService;
import com.paulhammant.ngwebdriver.NgWebDriver;
import io.cucumber.java.Scenario;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;


import static testbase.StaticObjectRepo.*;

public class TestInitialise {

    public ScenarioContext scenarioContext;
    public WebDriver driver;
    public NgWebDriver ngDriver;

    //public static String ProjectLocation = System.getProperty("user.dir");
    //public static String ReportsDirectory = ProjectLocation + "\\resources\\Reports";

    public TestInitialise() {
    }

    public TestInitialise(ScenarioContext _scenarioContext) throws IOException {
        driver = StaticObjectRepo.Driver;
        if (ConfigReader.GetConfigValue("Angular").equalsIgnoreCase("true"))
            ngDriver = StaticObjectRepo.ngDriver;
//======= More information for using ngWebdriver ==========================//
//        https://github.com/paul-hammant/ngWebDriver/blob/master/README.md
        this.scenarioContext = _scenarioContext;
    }

    public void InitialiseTest() throws IOException {
        System.out.println("\n Initialising Test.................");
        InitialiseDriver();
        SetDriverProps();
    }

    public static void SetEnvironment() {
        System.out.println("\n Initialising Environment.................");
        try {
            prop = new Properties();
            prop.load(new FileInputStream("src/test/resources/properties/config.properties"));
            StaticObjectRepo.Environment = prop.getProperty("environment");
            envProp = new Properties();
            envProp.load(new FileInputStream("src/test/resources/properties/environment/" + StaticObjectRepo.Environment + ".properties"));
            StaticObjectRepo.Browser = envProp.getProperty("Browser");
        } catch (IOException ioEx) {
            System.out.println(ioEx.getMessage());
        }
        System.out.println("\n Stripts running in " + StaticObjectRepo.Environment + " Environment");
    }


    private void InitialiseDriver() throws IOException {
//        if (ConfigReader.GetConfigValue("ExecuteBrowserstack").equalsIgnoreCase("true"))
//        {
//            System.out.println("\n Initialising Remote Driver for Browserstack .................");
//            StaticObjectRepo.Driver = new RemoteWebDriver(URI.create(ConfigReader.GetConfigValue("BS_URL")), GetRemoteCapabilities());
//        }
//        else

        System.out.println("\n Initialising local Driver.................");
        String browser = ConfigReader.GetConfigValue("Browser").toLowerCase();
        switch (browser.toLowerCase()) {
            case "chrome" -> {
                System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chromedriver.exe");
                StaticObjectRepo.Driver = GetChromeDriver();
            }
            case "firefox" -> {
                System.setProperty("webdriver.gecko.driver", "src/test/resources/drivers/geckodriver.exe");
                StaticObjectRepo.Driver = GetFirefoxDriver();
            }
            case "edge", "ms edge", "microsoft edge" -> {
                System.setProperty("webdriver.edge.driver", "src/test/resources/drivers/msedgedriver.exe");
                StaticObjectRepo.Driver = GetEdgeDriver();
            }
            default -> {
                System.out.println("Invalid value for browser in config file: {0}" + browser);
                throw new RuntimeException("Invalid value for browser in config file");
            }
        }

        // Initalise ng driver for Angular if set in the config file
        String IsAngular = envProp.getProperty("Angular").toLowerCase();
        if (!(StaticObjectRepo.Driver == null) && IsAngular.equalsIgnoreCase("true")) {
            System.out.println("\n Initialising ngDriver for Protractor.................");
            StaticObjectRepo.ngDriver = new NgWebDriver((JavascriptExecutor)StaticObjectRepo.Driver);
        } else {
            System.out.println("\n ngDriver for Protractor not required, enable in config if required.");
        }
    }


    public void SetDriverProps() {
        System.out.println("\n Setting driver Properties.................");
        StaticObjectRepo.Driver.manage().window().maximize();
        StaticObjectRepo.Driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(Long.parseLong(envProp.getProperty("PAGE_LOAD_TIMEOUT"))));
        System.out.println("\n Page Load Timeout set to " + envProp.getProperty(("PAGE_LOAD_TIMEOUT")));
        StaticObjectRepo.Driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Long.parseLong(envProp.getProperty("IMPLICIT_WAIT"))));
        System.out.println("\n Implicit wait Timeout set to " + envProp.getProperty("IMPLICIT_WAIT"));
        driver = StaticObjectRepo.Driver;
        ngDriver = StaticObjectRepo.ngDriver;
    }


//    private Capabilities GetRemoteCapabilities()
//    {
//        System.out.println("\n Setting Browserstack capabilities .................");
//        //if (prop.getProperty(("BsBrowser").equalsIgnoreCase("chrome"))
//        //{
//        Capabilities caps = new ChromeOptions()
//
//        caps.getBrowserVersion() = prop.getProperty(("BsBrowser");
//        Dictionary<String, Object> browserstackOptions = new Dictionary<String, Object>() {
//            /**
//             * Returns the number of entries (distinct keys) in this dictionary.
//             *
//             * @return the number of keys in this dictionary.
//             */
//            @Override
//            public int size() {
//                return 0;
//            }
//
//            /**
//             * Tests if this dictionary maps no keys to value. The general contract
//             * for the {@code isEmpty} method is that the result is true if and only
//             * if this dictionary contains no entries.
//             *
//             * @return {@code true} if this dictionary maps no keys to values;
//             * {@code false} otherwise.
//             */
//            @Override
//            public boolean isEmpty() {
//                return false;
//            }
//
//            /**
//             * Returns an enumeration of the keys in this dictionary. The general
//             * contract for the keys method is that an {@code Enumeration} object
//             * is returned that will generate all the keys for which this dictionary
//             * contains entries.
//             *
//             * @return an enumeration of the keys in this dictionary.
//             * @see Dictionary#elements()
//             * @see Enumeration
//             */
//            @Override
//            public Enumeration<String> keys() {
//                return null;
//            }
//
//            /**
//             * Returns an enumeration of the values in this dictionary. The general
//             * contract for the {@code elements} method is that an
//             * {@code Enumeration} is returned that will generate all the elements
//             * contained in entries in this dictionary.
//             *
//             * @return an enumeration of the values in this dictionary.
//             * @see Dictionary#keys()
//             * @see Enumeration
//             */
//            @Override
//            public Enumeration<Object> elements() {
//                return null;
//            }
//
//            /**
//             * Returns the value to which the key is mapped in this dictionary.
//             * The general contract for the {@code isEmpty} method is that if this
//             * dictionary contains an entry for the specified key, the associated
//             * value is returned; otherwise, {@code null} is returned.
//             *
//             * @param key a key in this dictionary.
//             *            {@code null} if the key is not mapped to any value in
//             *            this dictionary.
//             * @return the value to which the key is mapped in this dictionary;
//             * @throws NullPointerException if the {@code key} is {@code null}.
//             * @see Dictionary#put(Object, Object)
//             */
//            @Override
//            public Object get(Object key) {
//                return null;
//            }
//
//            /**
//             * Maps the specified {@code key} to the specified
//             * {@code value} in this dictionary. Neither the key nor the
//             * value can be {@code null}.
//             * <p>
//             * If this dictionary already contains an entry for the specified
//             * {@code key}, the value already in this dictionary for that
//             * {@code key} is returned, after modifying the entry to contain the
//             * new element. <p>If this dictionary does not already have an entry
//             * for the specified {@code key}, an entry is created for the
//             * specified {@code key} and {@code value}, and {@code null} is
//             * returned.
//             * <p>
//             * The {@code value} can be retrieved by calling the
//             * {@code get} method with a {@code key} that is equal to
//             * the original {@code key}.
//             *
//             * @param key   the hashtable key.
//             * @param value the value.
//             * @return the previous value to which the {@code key} was mapped
//             * in this dictionary, or {@code null} if the key did not
//             * have a previous mapping.
//             * @throws NullPointerException if the {@code key} or
//             *                              {@code value} is {@code null}.
//             * @see Object#equals(Object)
//             * @see Dictionary#get(Object)
//             */
//            @Override
//            public Object put(String key, Object value) {
//                return null;
//            }
//
//            /**
//             * Removes the {@code key} (and its corresponding
//             * {@code value}) from this dictionary. This method does nothing
//             * if the {@code key} is not in this dictionary.
//             *
//             * @param key the key that needs to be removed.
//             * @return the value to which the {@code key} had been mapped in this
//             * dictionary, or {@code null} if the key did not have a
//             * mapping.
//             * @throws NullPointerException if {@code key} is {@code null}.
//             */
//            @Override
//            public Object remove(Object key) {
//                return null;
//            }
//        };
//
//        browserstackOptions.put("os", prop.getProperty(("BsOS"));
//        browserstackOptions.put("osVersion", prop.getProperty(("BsOSVersion"));
//        browserstackOptions.put("browser", prop.getProperty(("BsBrowser"));
//        browserstackOptions.put("browser_version", prop.getProperty(("BsVersion"));
//        browserstackOptions.put("browserstack.user", prop.getProperty(("USERNAME"));
//        browserstackOptions.put("browserstack.key", prop.getProperty(("AUTOMATE_KEY"));
//        browserstackOptions.put("browserstack.console", prop.getProperty(("ConsoleLogs"));
//        browserstackOptions.put("name", sContext.setContext().Test.MethodName);
//        caps("bstack:options", browserstackOptions);
//
//        return caps;
//    }



//    public static void ChangeBrowserstackTestStatus(String new_status, String reason = "") throws URISyntaxException {
//        String text = ((RemoteWebDriver)StaticObjectRepo.Driver).getSessionId().toString();
//        String s = "{\"status\":\"" + new_status + "\", \"reason\":\"" + reason + "\"}";
//        byte[] bytes = encoding.UTF8.GetBytes(s);
//        URI uri = new URI(String.format("https://www.browserstack.com/automate/sessions/" + text + ".json"));
//        WebRequest webRequest = WebRequest.Create(uri);
//        HttpRequest httpWebRequest = (HttpRequest)webRequest;
//        webRequest.ContentType = "application/json";
//        webRequest.Method = "PUT";
//        webRequest.ContentLength = bytes.Length;
//        using (Stream stream = webRequest.GetRequestStream())
//        {
//            stream.Write(bytes, 0, bytes.Length);
//        }
//
//        NetworkCredential cred = new NetworkCredential(prop.getProperty(("USERNAME"), prop.getProperty(("AUTOMATE_KEY"));
//        CredentialCache credentialCache = new CredentialCache();
//        credentialCache.Add(uri, "Basic", cred);
//        httpWebRequest.PreAuthenticate = true;
//        httpWebRequest.Credentials = credentialCache;
//        webRequest.GetResponse().Close();
//    }

    private static EdgeDriver GetEdgeDriver() {
        System.out.println("\n Getting Edge driver.................");
        return new EdgeDriver(GetEdgeOptions());
    }

    private static ChromeDriver GetChromeDriver() {
        System.out.println("\n Getting Chrome driver.................");
        return new ChromeDriver(GetChromeOptions());
    }

    private static FirefoxDriver GetFirefoxDriver() {
        System.out.println("\n Getting Firefox driver.................");
        //FirefoxDriver result = new FirefoxDriver();
        return new FirefoxDriver(GetFirefoxOptions());
    }

    private static FirefoxOptions GetFirefoxOptions() {
        System.out.println("\n Firefox Driver running in normal mode");
        FirefoxOptions foptions = new FirefoxOptions();
        foptions.setAcceptInsecureCerts(true);
//        foptions.addArguments("--disable-extensions");
//        foptions.addArguments("--no-default-browser-check");
        //edgeOptions.addArguments("test-type");
        foptions.addArguments("start-maximized");
        //edgeOptions.addArguments("--window-size=1920,1080");
//        foptions.addArguments("--enable-precise-memory-info");
//        foptions.addArguments("--disable-popup-blocking");
//        foptions.addArguments("--remote-allow-origins=*");
        //edgeOptions.addArguments("--disable-default-apps");
        //edgeOptions.addArguments("test-type=browser");
        return foptions;
    }

    private static EdgeOptions GetEdgeOptions() {
        System.out.println("\n Setting Edge options.................");
        EdgeOptions edgeOptions = new EdgeOptions();
        if (envProp.getProperty("Headless").equalsIgnoreCase("true")) {
            System.out.println("\n Edge Driver running in headless mode");
            edgeOptions.addArguments("headless");
            edgeOptions.addArguments("disable-gpu");
        } else {
            edgeOptions.setAcceptInsecureCerts(true);
            edgeOptions.addArguments("--disable-extensions");
            edgeOptions.addArguments("--no-default-browser-check");
            //edgeOptions.addArguments("test-type");
            edgeOptions.addArguments("start-maximized");
            //edgeOptions.addArguments("--window-size=1920,1080");
            edgeOptions.addArguments("--enable-precise-memory-info");
            edgeOptions.addArguments("--disable-popup-blocking");
            edgeOptions.addArguments("--remote-allow-origins=*");
            //edgeOptions.addArguments("--disable-default-apps");
            //edgeOptions.addArguments("test-type=browser");
            System.out.println("\n Edge Driver running in normal mode");
        }

        if (envProp.getProperty("ExistingProfile").equalsIgnoreCase("yes")) {
            edgeOptions.addArguments("user-data-dir=" + envProp.getProperty("Profilepath"));
            edgeOptions.addArguments("profile-directory=" + envProp.getProperty("Profilefolder"));
            System.out.println("\n Edge Driver running using existing profile: " + envProp.getProperty("Profilefolder"));
        }

        return edgeOptions;
    }

    private static ChromeOptions GetChromeOptions() {
        System.out.println("\n Setting Chrome options.................");
        ChromeOptions chromeOptions = new ChromeOptions();
        if (envProp.getProperty("Headless").equalsIgnoreCase("true")) {
            chromeOptions.addArguments("headless");
            //chromeOptions.addArguments("disable-gpu");
            chromeOptions.addArguments("--remote-allow-origins=*");
            System.out.println("\n Chrome Driver running in headless mode");
        } else {
            chromeOptions.addArguments("--disable-extensions");
            chromeOptions.addArguments("--no-default-browser-check");
            //chromeOptions.addArguments("test-type");
            chromeOptions.addArguments("start-maximized");
            //chromeOptions.addArguments("--window-size=1920,1080");
            chromeOptions.addArguments("--enable-precise-memory-info");
            chromeOptions.addArguments("--disable-popup-blocking");
            chromeOptions.addArguments("--remote-allow-origins=*");
            //chromeOptions.addArguments("--disable-default-apps");
            //chromeOptions.addArguments("test-type=browser");
            System.out.println("\n Chrome Driver running in normal mode");
        }

        if (envProp.getProperty("ExistingProfile").equalsIgnoreCase("true")) {
            chromeOptions.addArguments("user-data-dir=" + envProp.getProperty("Profilepath"));
            chromeOptions.addArguments("profile-directory=" + envProp.getProperty("Profilefolder"));
            System.out.println("\n Chrome Driver running using existing profile: " + envProp.getProperty("Profilefolder"));
        }

        return chromeOptions;
    }

    public static void CloseBrowser() {
        System.out.println("Closing Browser.................");
        if (StaticObjectRepo.Driver != null) {
            StaticObjectRepo.Driver.quit();
        }
    }

    public String GetURL(String role, String env) {
        System.out.println("\n Getting Application URL for environment.................");
        String result = "";
        String text = "Url";
        String text2 = env;
        if (env.isEmpty()) {
            text2 = StaticObjectRepo.Environment.toLowerCase();
        }

        if (!role.isEmpty()) {
            text = role + "Url";
        }

        if (!text2.isEmpty()) {
            result = envProp.getProperty(text2 + text);
        } else {
            System.out.println("Valid URL not found in Config file!!");
            CloseBrowser();
        }

        return result;
    }

    public String GetURL() {
        String result="";
        System.out.println("\n Getting Application URL for environment.................");
        try{
        result = envProp.getProperty(Environment + "Url");
        } catch (Exception exception){
            System.out.println("Valid URL not found in Config file!!");
            System.out.println("Exception :\n" + exception);
            CloseBrowser();
        }
        return result;
    }

    public static void UpdateReportProps() {
        Capabilities cap = ((RemoteWebDriver)StaticObjectRepo.Driver).getCapabilities();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm");
        Date dateTime = new Date();
        ExtentService.getInstance().setSystemInfo("Tester", System.getProperty("user.name"));
        ExtentService.getInstance().setSystemInfo("OS", System.getProperty("os.name"));
        //ExtentService.getInstance().setSystemInfo("OS Version", System.getProperty("os.version"));
        ExtentService.getInstance().setSystemInfo("Browser", StaticObjectRepo.Browser.toUpperCase());
        ExtentService.getInstance().setSystemInfo("Browser Version", cap.getBrowserVersion());
        ExtentService.getInstance().setSystemInfo("Environment", StaticObjectRepo.Environment.toUpperCase());
        ExtentService.getInstance().setSystemInfo("Date",simpleDateFormat.format(dateTime));
        ExtentService.getInstance().setSystemInfo("Time", simpleTimeFormat.format(dateTime));
    }

//    public static void InitialiseReport() {
//        String pattern = " dd-MM-yyyy hh-mm";
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
//        LocalDateTime dateTime = java.time.LocalDateTime.now();
//        String dttm = simpleDateFormat.format(new Date());
//        String date = dateTime.getDayOfMonth() + "-" + dateTime.getMonthValue() + "-" + dateTime.getYear();
//        String time = dateTime.getHour() + ":" + dateTime.getMinute() + ":" + dateTime.getSecond();
//
//        System.out.println("\n Initialising Report .................");
//        ExtentSparkReporter spark  = new ExtentSparkReporter(ReportsDirectory + "\\AutomationTestReport_" + dttm + ".html");
//        Reporter.attachReporter(spark);
//        spark.config().setTheme(Theme.DARK);
//        spark.config().setReportName(envProp.getProperty("Project") + " Report");
//        spark.config().setDocumentTitle("Automation Testing Report");
//        spark.config().setEncoding("utf-8");
//        spark.config().setTimeStampFormat("MMM dd, yyyy HH:mm:ss");
//        Reporter.setSystemInfo("Tester", System.getProperty("user.name"));
//
//        if (envProp.getProperty("ExecuteBrowserstack").equalsIgnoreCase("true")) {
//            StaticObjectRepo.Reporter.setSystemInfo("OS", envProp.getProperty("BsOS").toUpperCase() + " " + envProp.getProperty("BsOSVersion").toUpperCase());
//            StaticObjectRepo.Reporter.setSystemInfo("Browser", envProp.getProperty("BsBrowser").toUpperCase() + " " + envProp.getProperty("BsVersion").toUpperCase());
//        } else {
//            //StaticObjectRepo.Reporter.setSystemInfo("Machine", System.getProperty("os.name"));
//            StaticObjectRepo.Reporter.setSystemInfo("OS", System.getProperty("os.name"));
//            StaticObjectRepo.Reporter.setSystemInfo("Browser", envProp.getProperty("Browser").toUpperCase());
//        }
//
//        StaticObjectRepo.Reporter.setSystemInfo("Environment", Environment);
//        StaticObjectRepo.Reporter.setSystemInfo("Execution Date", date);
//        StaticObjectRepo.Reporter.setSystemInfo("Execution Time", time);
//    }

    public static void ExtractFeatureName() {
        System.out.println("\n Getting Feature details.................");
    }

    public void ExtractScenarioName(Scenario scenario) {
        System.out.println("\n Getting Scenario details.................");
        StaticObjectRepo.scenario = scenario;
    }

    public static void WriteToReport() {
        System.out.println("\n Writing to report.................");
        StaticObjectRepo.Reporter.flush();
    }


    public static void AttachScreenshot() {
        if(scenario.isFailed()) {
            final byte[] screenshot = ((TakesScreenshot) Driver).getScreenshotAs((OutputType.BYTES));
            scenario.attach(screenshot, "image/png", "image");
        }
    }

}

