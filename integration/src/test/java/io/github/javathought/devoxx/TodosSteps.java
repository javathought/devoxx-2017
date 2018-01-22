package io.github.javathought.devoxx;

import cucumber.api.java8.En;
import org.openqa.selenium.WebDriver;

public class TodosSteps implements En {

    private AppSteps withAppSteps;
    private WebDriver driver;

    public TodosSteps(AppSteps appSteps) {

        withAppSteps = appSteps;
        driver = withAppSteps.getDriver();
        
        When("^je vais à la page \"([^\"]*)\"$", (String arg0) -> {
            
        });


    }
}
