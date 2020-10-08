package org.example;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

public class LoginTest {

    WebDriver driver;

    @DataProvider(name = "Credentials")
    public Object[][] getData() {
        Object[][] data = {{Util.username, Util.password}, {"invalid", Util.password}, {Util.username, "invalid"}, {"invalid", "invalid"}};
        return data;
    }

    @BeforeMethod()
    public void setup() {
        driver = new ChromeDriver();
        driver.get(Util.baseURL);
    }

    @Test(dataProvider = "Credentials")
    public void login(String username, String pwd) {
        String expected_msg = "Manger Id : mngr286381";
        String expected_alert = "User or Password is not valid";


        driver.findElement(By.name("uid")).clear();
        driver.findElement(By.name("uid")).sendKeys(username);

        driver.findElement(By.name("password")).clear();
        driver.findElement(By.name("password")).sendKeys(pwd);

        driver.findElement(By.name("btnLogin")).click();

        try {
            Alert alert = driver.switchTo().alert();
            String alert_actual = alert.getText();
            Assert.assertEquals(alert_actual, expected_alert, "Invalid username or password");
            alert.accept();
        } catch (NoAlertPresentException e) {
            String actual = driver.findElement(By.cssSelector("tr.heading3>td")).getText();
            Assert.assertEquals(actual, expected_msg, "Incorrect message");

            try {
                File scrFile = ( (TakesScreenshot) driver ).getScreenshotAs(OutputType.FILE);
                //The below method will save the screen shot in destination directory with name "screenshot.png"
                FileHandler.copy(scrFile, new File(Util.screenshot_path));
            } catch (IOException io) {
                io.printStackTrace();
            }

        }

    }

    @AfterMethod()
    public void teardown() {
        driver.quit();
    }


}
