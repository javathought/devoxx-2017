package io.github.javathought.devoxx;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

public class AppSteps {

    private static boolean initialized = false;
    private WebDriver driver;

    public AppSteps() {
        ChromeDriverManager.getInstance().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

        initialized = true;
    }

    public WebDriver getDriver() {
        return driver;
    }
    
}
