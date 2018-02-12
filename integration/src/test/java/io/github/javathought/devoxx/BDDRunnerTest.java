package io.github.javathought.devoxx;

import cucumber.api.CucumberOptions;
import io.github.javathought.devoxx.run.ApiServer;
import io.github.javathought.devoxx.tools.CucumberWithWatcher;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.Network;

import java.io.IOException;
//import org.testcontainers.containers.MySQLContainer;


@RunWith(CucumberWithWatcher.class)
@CucumberOptions(strict = false, plugin = {"pretty", "json:target/cucumber-reports/rules.json" }
//, tags = {"@Todos"}
)
public class BDDRunnerTest {


    @ClassRule
    public static BrowserWebDriverContainer chrome = new BrowserWebDriverContainer<>()
            .withDesiredCapabilities(DesiredCapabilities.chrome())
            .withNetwork(Network.SHARED)
            .withNetworkAliases("vnchost")
            .withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.SKIP, null)
//            .withRecordingFileFactory(new CucumberRecordingFileFactory())
            ;

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

/*
    @Rule
    public VncRecordingContainer vnc = new VncRecordingContainer(chrome) {
        @Override
        protected void failed(Throwable e, Description description) {
            saveRecordingToFile(new File(recordingDir, "FAILED-" + description.getMethodName() + ".flv"));
            super.failed(e, description);
        }
    };
*/

//    @ClassRule
//    public static MySQLContainer mysql = new MySQLContainer("mysql:5.7");

    @BeforeClass
    public static void startHttp() throws IOException {
        ApiServer.start();
    }

}
