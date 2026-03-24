package com.lab10.base;

import com.lab10.utils.ConfigReader;
import com.lab10.utils.DriverFactory;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BaseTest {
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    @BeforeMethod
    @Parameters({"browser"})
    public void setUp(@Optional("chrome") String browser) {
        WebDriver driver = DriverFactory.createDriver(browser);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
        DRIVER.set(driver);
        getDriver().get(ConfigReader.get("ui.baseUrl"));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        takeScreenshot(result.getName());
        if (getDriver() != null) {
            getDriver().quit();
            DRIVER.remove();
        }
    }

    protected WebDriver getDriver() {
        return DRIVER.get();
    }

    private void takeScreenshot(String testName) {
        try {
            if (getDriver() == null) {
                return;
            }
            Path screenshotDir = Path.of("target", "screenshots");
            Files.createDirectories(screenshotDir);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            File src = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
            File dest = screenshotDir.resolve(testName + "_" + timestamp + ".png").toFile();
            FileUtils.copyFile(src, dest);
        } catch (Exception e) {
            System.err.println("Không thể chụp screenshot: " + e.getMessage());
        }
    }
}
