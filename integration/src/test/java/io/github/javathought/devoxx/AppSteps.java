package io.github.javathought.devoxx;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

//import static io.github.javathought.devoxx.BDDRunnerTest.chrome;

public class AppSteps {

    private static boolean initialized = false;
    private WebDriver driver;
    private String baseUrl;
    private String runningMode;

    public AppSteps() {

        runningMode = "live";
//        runningMode = "container";

        if ("container".equalsIgnoreCase(runningMode)) {

            driver = BDDRunnerTest.chrome.getWebDriver();

            baseUrl = "https://" + BDDRunnerTest.chrome.getTestHostIpAddress() + ":9090/";
//        baseUrl = "https://" + "192.168.99.1" + ":9090/";
//        baseUrl = "https://docker.for.mac.localhost:9090";
        } else {
//            ChromeDriverManager.getInstance().setup();
            System.setProperty("webdriver.chrome.driver", "/Applications/chromedriver");
            driver = new ChromeDriver();
            baseUrl = "https://localhost:9090";

        }

        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

        initialized = true;


    }

    public WebDriver getDriver() {
        return driver;
    }

    public void setBaseUrl(String baseUrl) {
//        this.baseUrl = baseUrl;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

}
