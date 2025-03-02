package utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.reporter.configuration.ViewName;
import org.apache.poi.ss.formula.functions.EDate;
//import org.testng.ITestResult;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class ExtentDriver {

    private  static final Properties prop = new Properties();
    static ExtentSparkReporter htmpReport;

    public static ExtentReports getInstance(String path, String reportName, String documentTitle) throws IOException{

        prop.load(new FileInputStream("src/test/resources/properties/config.properties"));
        SimpleDateFormat dataFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();

        htmpReport = new ExtentSparkReporter(path);
        htmpReport.viewConfigurer().viewOrder().as(new ViewName[] {ViewName.DASHBOARD,ViewName.TEST});
        htmpReport.config().setDocumentTitle(documentTitle);
        htmpReport.config().setReportName(reportName);
        htmpReport.config().setTheme(Theme.DARK);
        htmpReport.config().setTimelineEnabled(false);

        ExtentReports extent = new ExtentReports();
        extent.attachReporter(htmpReport);
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Executed by", System.getProperty("user.name"));
        extent.setSystemInfo("Environment", prop.getProperty("environment"));
        extent.setSystemInfo("Execution Date", dataFormat.format(date));
        extent.setSystemInfo("Execution Time", timeFormat.format(date));

        return extent;
    }

//    public static void verifyResult(ITestResult result, ExtentTest test){
//        if(result.getStatus() == ITestResult.FAILURE){
//            test.log(Status.FAIL, "Test Case Failed: " + result.getName());
//            test.log(Status.FAIL, "Test Case Failed with : " + result.getThrowable());
//        }
//        else if(result.getStatus() == ITestResult.SKIP)
//            test.log(Status.SKIP, "Test case Skipped is : " + result.getName());
//        else if(result.getStatus() == ITestResult.SUCCESS)
//            test.log(Status.PASS, "Test case Skipped is : " + result.getName());
//    }
}
