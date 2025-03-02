package testcases.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.*;
import testbase.BasePage;
import testbase.StaticObjectRepo;

import java.io.IOException;

import static testbase.BasePage.driver;

public class SuperCalculatorPage extends BasePage {

//    private static ScenarioContext sContext;
    public SuperCalculatorPage(WebDriver _driver) throws IOException {
        super(_driver);
        driver = _driver;
//        sContext = scenariocontext;
    }

    //NgWebElement firstNg = ngdriver.findElement(NgBy.Model("first"));
    WebElement first = driver.findElement(By.cssSelector("input[ng-model='first']"));

    //NgWebElement secondNg = ngdriver.findElement(NgBy.Model("second"));
    WebElement second = driver.findElement(By.cssSelector("input[ng-model='second']"));

    //selectElement operatorSelectNg = new SelectElement(ngdriver.findElement(NgBy.Model("operator")));
    Select operatorSelect = new Select(driver.findElement(By.cssSelector("select[ng-model='operator']")));

    public String LatestResult()
    {
        return driver.findElement(By.cssSelector("h2")).getText();
    }

    public String LatestResultNg()
    {
         //return ngdriver.findElement(NgBy.Binding("latest")).Text;
        return null;
    }

    public void Add(String first, String second, String IsAngular)
    {
        DoMath(Integer.parseInt(first), Integer.parseInt(second), "+", IsAngular.equalsIgnoreCase("true"));
    }

    public void Subtract(int first, int second, Boolean IsAngular)
    {
        DoMath(first, second, "-", IsAngular);
    }

    public void Multiply(int first, int second, Boolean IsAngular)
    {
        DoMath(first, second, "*", IsAngular);
    }

    public void Divide(String first, String second, String IsAngular)
    {
        DoMath(Integer.parseInt(first), Integer.parseInt(second), "/", Boolean.getBoolean(IsAngular));
    }

    private void DoMath(int first, int second, String op, Boolean IsAngular)
    {
        SetFirst(first, IsAngular);
        SetSecond(second, IsAngular);
        SetOperator(op, IsAngular);
        ClickGo();
    }

    private void SetFirst(int number, Boolean IsAngular)
    {
        if (IsAngular)
        {
//            firstNg.Clear();
//            firstNg.SendKeys(number);
            throw new RuntimeException("Angular set to true in config!");
        }
        else
        {
            first.clear();
            first.sendKeys(String.valueOf(number));
        }
    }

    private void SetSecond(int number, Boolean IsAngular)
    {
        if (IsAngular)
        {
//            secondNg.Clear();
//            secondNg.SendKeys(number);
            throw new RuntimeException("Angular set to true in config!");
        }
        else
        {
            second.clear();
            second.sendKeys(String.valueOf(number));
        }
    }

    private void SetOperator(String op, Boolean IsAngular)
    {
        if (IsAngular)
        {
            //operatorSelectNg.SelectByText(op);
            throw new RuntimeException("Angular set to true in config!");
        }
        else
        {
            operatorSelect.selectByVisibleText(op);
        }
    }

    private void ClickGo()
    {
        driver.findElement(By.id("gobutton")).click();
    }
}
