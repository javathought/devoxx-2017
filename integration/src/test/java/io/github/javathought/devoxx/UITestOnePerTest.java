package io.github.javathought.devoxx;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.VncRecordingContainer;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;


public class UITestOnePerTest {

    private WebDriver driver;
    private static String baseUrl;



//    static File recordingDir = new File("build/recording-" + System.currentTimeMillis());

    @Rule
    public BrowserWebDriverContainer chrome = new BrowserWebDriverContainer()
            .withDesiredCapabilities(DesiredCapabilities.chrome())
            .withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL, new File("target"));


    @Before
    public void setUp() throws Exception {

        //  ****   Selenium with testcontainers
        driver = chrome.getWebDriver();
        baseUrl = "https://" + chrome.getTestHostIpAddress() + ":9090/";

        // **** Selenium Native
//        ChromeDriverManager.getInstance().setup();
//        System.setProperty("webdriver.chrome.driver", "C:\\dvi\\tools\\selenium\\drivers\\chromedriver.exe");

//        System.setProperty("webdriver.chrome.driver", "/Applications/chromedriver");
//        driver = new ChromeDriver();
//        baseUrl = "https://localhost:9090/";


//        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
    }

    @Test
    public void connection_should_be_accepted_with_correct_credentials() {
        driver.get(baseUrl);

        WebDriverWait wait1 = new WebDriverWait(driver, 3);
        WebElement element1 = wait1.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("username"))));


        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys("admin");
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("admin");
        driver.findElement(By.id("submit")).click();
//        wait1.until(ExpectedConditions.attributeToBeNotEmpty(driver.findElement(By.id("connected_user")), "value") );
        wait1.until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return ! d.findElement(By.id("connected_user")).getText().isEmpty();
            }
        });
        assertEquals("admin", driver.findElement(By.id("connected_user")).getText());
    }

    @Test
    public void connection_should_be_rejected_with_wrong_credentials() {
        driver.get(baseUrl);

        WebDriverWait wait1 = new WebDriverWait(driver, 3);
        WebElement element1 = wait1.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("username"))));


        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys("admin");
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("wrong pwd");
        driver.findElement(By.id("submit")).click();
        wait1.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("login-error"))));
        assertThat(driver.findElement(By.id("login-error")).getText(), containsString("Username or password is incorrect"));
    }

    @After
    public void teardown() {

//        if (driver != null) {
//            driver.quit();
//        }
    }
    
    }
