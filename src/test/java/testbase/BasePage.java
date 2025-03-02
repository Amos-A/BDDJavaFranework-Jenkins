package testbase;

import com.github.javafaker.Faker;
import freemarker.template.utility.NullArgumentException;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

//-------------------------------------------------------------------------------------//
//                     Base page class to extend the page class used                   // 
//-------------------------------------------------------------------------------------//
public class BasePage
{

    public static WebDriver driver;

//    public static ScenarioContext SContext;

    public TestInitialise ti = new TestInitialise();

    Properties prop;

    protected static String env;

    public BasePage(WebDriver _driver) throws IOException {
        driver = _driver;
//        SContext = scenariocontext;
    }


    public String getPageTitle()
    {
        return driver.getTitle();
    }

    public void WaitForPageToLoad() throws InterruptedException {
        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(
                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
        Thread.sleep(1500);
    }

    public void GoToURL()
    {
        driver.navigate().to(ti.GetURL());
    }

    public void GoToURL(String actor)
    {
        //Navigating to URL..........

        if(actor.isEmpty())
            driver.navigate().to(ti.GetURL());
        else
            driver.navigate().to(actor);
        //return new LandingPage(driver);
    }


    public String GetEnvURL(String role, String env)
    {
        return ti.GetURL(role, env);
    }

    public String GetEnvURL()
    {
        return(ti.GetURL());
    }


    public void ClickElement(WebElement element)
    {
        try
        {
            element.click();
        }
        catch (Exception e)
        {
            System.out.println("\n Element to be clicked not found: " + e.getMessage());
            System.out.println("\n Element to be clicked not found: " + e.getMessage());
        }
    }


    //public void WaitForPageToLoad(WebDriver driver, int timeout = 15)
    //{
    //    driver.WaituntilElementClickable(d =>
    //    {
    //        String state = d.ExecuteJs("return document.readyState").ToString();
    //        return state == "complete";
    //    }, timeout);
    //}


    //this will search for the element until a timeout is reached
    public void WaituntilElementClickable(By elementLocator, int timeout)
    {
        try
        {
            var wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
            wait.until(ExpectedConditions.elementToBeClickable(elementLocator));
        }
        catch (NoSuchElementException e)
        {
            System.out.println("Element with locator: '" + elementLocator + "' was not found.");
            e.printStackTrace();
        }
    }

    public void WaituntilElementVisible(By elementLocator, int timeout)
    {
        try
        {
            var wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
            wait.until(ExpectedConditions.presenceOfElementLocated(elementLocator));
        }
        catch (NoSuchElementException ex)
        {
            System.out.println("Element with locator: '" + elementLocator + "' was not found.");
        }
    }

    public void WaituntilElementInvisible(By elementLocator, int timeout)
    {
        try
        {
            var wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
            wait.until(ExpectedConditions.visibilityOfElementLocated(elementLocator));
        }
        catch (Exception e)
        {
            System.out.println("Element with locator: '" + elementLocator + "' was found on Page. \n Exception: "+ e);
            throw e;
        }
    }

    public void WaitByPolling(By SearchElement, int WaitTimeSec, int PollMilliSec)
    {
        WebElement element = null;
        try
        {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WaitTimeSec));
            wait.pollingEvery(Duration.ofSeconds(PollMilliSec));
            wait.until(ExpectedConditions.elementToBeClickable(SearchElement));

        }
        catch (Exception e)
        {
            System.out.println("Element not found!! " + e.getMessage());
        }

    }


    public void findElementAndsendKeys(WebDriver driver, By by, String text)
    {
        try
        {
            driver.findElement(by).sendKeys(text);
        }
        catch (NoSuchElementException ex)
        {
            System.out.println("\n Exception occured: " + ex);
        }
    }

    public Object ExecuteJs(WebDriver driver, String script)
    {
        try
        {
            return ((JavascriptExecutor)StaticObjectRepo.Driver).executeScript(script);
        }
        catch (Exception e)
        {
            System.out.println("\n Exception occured: " + e);
            return null;
        }

    }

    public void ClickOnElement(WebElement webElement)
    {
        try
        {
            var eText = webElement.getText();
            webElement.click();
            //StaticObjectRepo.Scenario.Log(Status.Pass, $"Successfully clicked on {webElement.Text}");
        }
        catch (NoSuchElementException ex)
        {
//            TestInitialise.saveScreenshot(TestInitialise.errorDirectory, "Error_" + );
//            StaticObjectRepo.Error = $"Element not found to click {ex.Message}";
        }
    }

    public void DoubleClickElement(By by)
    {
        Actions actions = new Actions(driver);
        WaituntilElementClickable(by, 20);
        WebElement elementLocator = driver.findElement(by);
        actions.doubleClick(elementLocator).perform();
    }

    public void EnterText(WebElement webElement, String text)
    {
        try
        {
            ScrollAndClickElement(webElement);
            webElement.sendKeys(Keys.CONTROL + "a" + Keys.DELETE);
            webElement.sendKeys(text);
        }
        catch (NoSuchElementException ex)
        {
//            TestInitialise.saveScreenshot(TestInitialise.errorDirectory, "Error_" + DateTime.Now);
//            StaticObjectRepo.Error = $"Element not found to enter text {ex.Message}";
        }
    }

    public void EnterText(WebElement webElement, int text)
    {
        try
        {
            ScrollAndClickElement(webElement);
            webElement.sendKeys(Keys.CONTROL + "a" + Keys.DELETE);
            webElement.sendKeys(String.valueOf(text));
        }
        catch (NoSuchElementException ex)
        {
//            TestInitialise.saveScreenshot(TestInitialise.errorDirectory, "Error_" + DateTime.Now);
//            StaticObjectRepo.Error = $"Element not found to enter text {ex.Message}";
        }
    }

    public Boolean IsElementisDisplayed(WebElement webElement)
    {
        try
        {
            //            StaticObjectRepo.Scenario.Log(Status.Pass, $"Element is present {webElement.Text}");
            return webElement.isDisplayed();
        }
        catch (NoSuchElementException ex)
        {
//            TestInitialise.saveScreenshot(TestInitialise.errorDirectory, "Error_" + DateTime.Now);
//            StaticObjectRepo.Scenario.Log(Status.Fail, $"Element not found {ex.Message}");
            return false;
        }
    }

    public Boolean IsElementisDisplayed(By webElement)
    {
        boolean IsElementisDisplayed;
        try
        {
            IsElementisDisplayed = driver.findElement(webElement).isDisplayed();
            return IsElementisDisplayed;
        }
        catch (NoSuchElementException ex)
        {
            return false;
        }
    }

    public Boolean IsElementPresent(By by)
    {
        return driver.findElements(by).size() != 0;
    }

    public void ScrollAndClickElement(WebElement webElement)
    {
        try
        {
            long scrollHeight = 0;
            do
            {
                var js = (JavascriptExecutor)StaticObjectRepo.Driver;
                var newScrollHeight = (long)js.executeScript("window.scrollTo(0, document.body.scrollHeight); return document.body.scrollHeight;");

                if (newScrollHeight == scrollHeight)
                {
                    break;
                }
                else
                {
                    scrollHeight = newScrollHeight;
                    Thread.sleep(2000);
                }
            } while (true);
            webElement.click();
        }
        catch (NoSuchElementException ex)
        {
//            TestInitialise.saveScreenshot(TestInitialise.errorDirectory, "Error_" + DateTime.Now);
//            StaticObjectRepo.Scenario.Log(Status.Fail, $"Element not found {ex.Message}");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public String GetSelectedListOption(WebElement ListElement)
    {
        String SelectedText = "";
        try
        {
            if (!ListElement.getText().equals(""))
            {
                Select select = new Select(ListElement);
                SelectedText = select.getFirstSelectedOption().getText();
            }
        }
        catch (NoSuchElementException ex)
        {
//            TestInitialise.saveScreenshot(TestInitialise.errorDirectory, "Error_" + DateTime.Now);
//            StaticObjectRepo.Scenario.Log(Status.Fail, $"Element not found {ex.Message}");
        }
        return SelectedText;
    }

    // Method to select valude from dropdown by index

    public void SelectListOption(WebElement ListElement, int index)
    {
        try
        {
            Select select = new Select(ListElement);
            select.selectByIndex(index);
        }
        catch (NoSuchElementException ex)
        {
//            TestInitialise.saveScreenshot(TestInitialise.errorDirectory, "Error_" + DateTime.Now);
//            StaticObjectRepo.Scenario.Log(Status.Fail, $"Element not found {ex.Message}");
        }
    }

    // Method to select valude from dropdown by value or text. By default if value is used for selection.
    // At least one of the parameter need to be passed (value or text), if none are passed an exception is thrown
    public void SelectListOption(WebElement ListElement, String text) {
        try {
            Select select = new Select(ListElement);

            //if (!String.IsNullOrEmpty(value))
            //{
            //    selectElement.SelectByValue(value);
            //}
            //else
                if (!text.isEmpty())
                    select.selectByVisibleText(text);
            } catch (NoSuchElementException ex) {
//                if (!ex.getMessage().isEmpty()) {
//                }
            System.out.println("Error : Element not found!!");
            } catch (NullArgumentException e) {
//                if (!e.getMessage().isEmpty()) {
//                }
            System.out.println("Error : Argument cannot be null!");
            }

    }

    public static void WaitforFewSeconds(Long mseconds) throws InterruptedException {
        Thread.sleep(mseconds * 1000);
    }

    public static void SelectRamdomListItem(WebElement element, int StartAt)
    {
        var rand = new Faker();
        var oSelect = new Select(element);
        oSelect.selectByIndex(rand.random().nextInt(StartAt, oSelect.getOptions().size() - 1));
    }

    public static void ClickStaleElement(WebElement el, ScenarioContext sContext)
    {
        for (int i = 0; i < 4; i++)
            try
            {
                BasePage basePage = new BasePage(StaticObjectRepo.Driver);
                basePage.ScrollAndClickElement(el);
                break;
            }
            catch (StaleElementReferenceException e)
            {
                //e.toString();
                System.out.println("Trying to recover from a stale element :" + e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public static void ClearField(WebElement field)
    {
        field.sendKeys(Keys.CONTROL + "a" + Keys.DELETE);
    }

    public static void SelectListOptionByPartialText(WebElement ListElement, String text) {
        int i = 1;
        WebElement[] ListOptions = (WebElement[]) ListElement.findElements(By.cssSelector("option")).toArray();
        Select selectElement = new Select(ListElement);
        for (; i <= ListOptions.length; i++) {
            if (ListOptions[i].getText().contains(text)) {
                selectElement.selectByIndex(i);
                break;
            }
        }
    }

    public static void ClickUsingJavaScript(WebDriver _driver, WebElement confirmDataCheck)
    {
        JavascriptExecutor executor = (JavascriptExecutor)_driver;
    executor.executeScript("arguments[0].click();", confirmDataCheck);
    }

    public static void EnterTextUsingJavaScript(WebDriver _driver, WebElement el, String txt)
    {
        JavascriptExecutor executor = (JavascriptExecutor)_driver;

        executor.executeScript("arguments[0].value = '" + txt + "';", el);
    }


}
