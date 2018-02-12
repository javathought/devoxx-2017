package io.github.javathought.devoxx.tools;

import cucumber.api.junit.Cucumber;
import io.github.javathought.devoxx.BDDRunnerTest;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.testcontainers.containers.ContainerListener;
import org.testcontainers.containers.GenericContainer;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class CucumberWithWatcher extends Cucumber {

    public CucumberWithWatcher(Class clazz) throws InitializationError, IOException {
        super(clazz);
    }

    @Override
    public void run(RunNotifier notifier) {
        notifier.addListener(new ContainerListener(BDDRunnerTest.chrome));
        super.run(notifier);
    }
}
