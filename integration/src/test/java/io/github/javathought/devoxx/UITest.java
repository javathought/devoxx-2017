package io.github.javathought.devoxx;

import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testcontainers.containers.BrowserWebDriverContainer;
//import org.testcontainers.containers.ContainerWatcher;

import java.io.File;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;


public class UITest {

    private WebDriver driver;
    private static String baseUrl;

    @ClassRule
    public static BrowserWebDriverContainer chrome = new BrowserWebDriverContainer()
            .withDesiredCapabilities(DesiredCapabilities.chrome())
            .withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL, new File("target"));

//    @Rule
//    public ContainerWatcher watcher = new ContainerWatcher(chrome);


    @Before
    public void setUp() throws Exception {
        // On instancie notre withSelenium, et on configure notre temps d'attente
        
//        FirefoxOptions opts = new FirefoxOptions().setBinary("C:\\Users\\C349459\\AppData\\Local\\Mozilla Firefox\\firefox.exe");
//        System.setProperty("webdriver.gecko.driver", "C:\\dvi\\tools\\selenium\\drivers\\geckodriver.exe");
//        driver = new FirefoxDriver(opts);


//        RemoteWebDriver driver = chrome.getWebDriver();
//        driver.get("https://wikipedia.org");

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
//        withSelenium = new WebDriverBackedSelenium(driver, baseUrl);

        driver.manage().deleteAllCookies();

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

    @Test
    public void non_admin_connection_should_be_accepted_with_correct_credentials() {
        driver.get(baseUrl);

        WebDriverWait wait1 = new WebDriverWait(driver, 3);
        WebElement element1 = wait1.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("username"))));


        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys("normal");
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("demo");
        driver.findElement(By.id("submit")).click();
//        wait1.until(ExpectedConditions.attributeToBeNotEmpty(driver.findElement(By.id("connected_user")), "value") );
        wait1.until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return ! d.findElement(By.id("connected_user")).getText().isEmpty();
            }
        });
        assertEquals("normal", driver.findElement(By.id("connected_user")).getText());
    }

    @Test
    public void non_admin_connection_should_be_rejected_with_wrong_credentials() {
        driver.get(baseUrl);

        WebDriverWait wait1 = new WebDriverWait(driver, 3);
        WebElement element1 = wait1.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("username"))));


        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys("normal");
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("bad");
        driver.findElement(By.id("submit")).click();
        wait1.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("login-error"))));
        assertThat(driver.findElement(By.id("login-error")).getText(), containsString("Username or password is incorrect"));
    }


/*
    @Test
    public void testSiteWithSelenium() {
        withSelenium.open(baseUrl);

//        withSelenium.findElement(By.id("username")).clear();
        withSelenium.type("id=username","admin");
//        withSelenium.findElement(By.id("password")).clear();
        withSelenium.type("id=password","admin");
        withSelenium.click("id=submit");
    }
*/


    @After
    public void teardown() {

//        if (driver != null) {
//            driver.quit();
//        }
    }
    
    }
