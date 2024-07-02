package com.orangehrm.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

public class BaseClass {
    public static Properties prop;
    public static WebDriver driver;

    @BeforeTest
    public void setup() {
        loadConfig();
        launchApp();
    }

    public void loadConfig() {
        try (FileInputStream ip = new FileInputStream(
                System.getProperty("user.dir") + File.separator + "Configuration" + File.separator + "config.properties")) {
            prop = new Properties();
            System.out.println("super constructor invoked");
            prop.load(ip);
            System.out.println("driver: " + driver);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Configuration file not found");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error loading configuration file");
        }
    }

    public void launchApp() {
        WebDriverManager.chromedriver().setup();
        String browserName = prop.getProperty("browser");
        switch (browserName) {
            case "Chrome":
                driver = new ChromeDriver();
                break;
            case "FireFox":
                driver = new FirefoxDriver();
                break;
            case "IE":
                driver = new InternetExplorerDriver();
                break;
            default:
                throw new RuntimeException("Unsupported browser: " + browserName);
        }
        driver.manage().window().maximize();
        driver.get(prop.getProperty("url"));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test(priority = 1)
    public void loginTestWithValidCredentials() {
        driver.findElement(By.xpath("//input[@placeholder='Username']")).sendKeys(prop.getProperty("username"));
        driver.findElement(By.xpath("//input[@placeholder='Password']")).sendKeys(prop.getProperty("password"));
        driver.findElement(By.xpath("//button[@type='submit']")).submit();

        String pageTitle = driver.getTitle();

        //directly logs into Employeemanagement page
        Assert.assertEquals("Employee Management",pageTitle);
        ///logout();
    }

    @Test(priority=2)
    public void addEmployee()
    {
        driver.findElement(By.xpath("//a[text()='Employee List ']")).click();
        driver.findElement(By.xpath("//a[@id='addEmployeeButton']")).click();

        driver.findElement(By.xpath("//input[@placeholder='First Name']")).sendKeys(prop.getProperty("FirstName"));
        driver.findElement(By.xpath("//input[@placeholder='Last Name']")).sendKeys(prop.getProperty("LastName"));

        //driver.findElement(By.xpath("//div[@class='input-group-append']//i[contains(text(),'arrow_drop_down')]")).click();
        driver.findElement(By.xpath("//input[@value='Canadian Development Center']")).click();
        driver.findElement(By.xpath("//button[text()='Next']")).click();





    }

    @Test(priority = 3)
    public void loginTestWithInvalidCredentials()
    {
        driver.findElement(By.xpath("//input[@placeholder='Username']")).sendKeys(prop.getProperty("username"));
        driver.findElement(By.xpath("//input[@placeholder='Password']")).sendKeys("12345");
        driver.findElement(By.xpath("//button[@type='submit']")).submit();

        String expected_message="Invalid Credentials";
        String actual_message=driver.findElement(By.xpath("//div[@class='toast-message']")).getText();

        //System.out.println(actual_message);
        Assert.assertEquals(actual_message,expected_message);
    }

    @AfterTest
    public void tearDown() throws InterruptedException {


        Thread.sleep(5000);
        driver.quit();
    }

    public void logout()
    {
        driver.findElement(By.xpath("//a[@id='logout-options-dropdown-button']")).click();
        driver.findElement(By.xpath("//div[@class='logout-options-menu-item']")).click();
    }
}
