package io.github.javathought.devoxx;

import org.junit.*;
import org.junit.rules.TestName;
import org.junit.runner.Description;
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


public class UITestNative {

    private WebDriver driver;
    private static String baseUrl;



//    static File recordingDir = new File("build/recording-" + System.currentTimeMillis());
    static File recordingDir = new File("target");

    @ClassRule
    public static BrowserWebDriverContainer chrome = new BrowserWebDriverContainer<>()
            .withDesiredCapabilities(DesiredCapabilities.chrome())
            .withNetwork(Network.SHARED)
            .withNetworkAliases("vnchost")
            .withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.SKIP, null);

/*
    @Rule
    public VncRecordingContainer vnc = new VncRecordingContainer(chrome);

    @Rule
    public TestName testName = new TestName();

    @After
    public void tearDown() throws Exception {
        try(InputStream inputStream = vnc.streamRecording()) {
            File target = new File(recordingDir, testName.getMethodName() + ".flv");
            target.getParentFile().mkdirs();
            Files.copy(inputStream, target.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }
*/

    @Rule
    public VncRecordingContainer vnc = new VncRecordingContainer(chrome) {
        @Override
        protected void failed(Throwable e, Description description) {
            saveRecordingToFile(new File(recordingDir, "FAILED-" + description.getMethodName() + ".flv"));
            super.failed(e, description);
        }
    };


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
