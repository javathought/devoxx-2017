package io.github.javathought.devoxx.bdd;

import cucumber.api.PendingException;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java8.En;
import cucumber.api.java8.Fr;
import io.github.javathought.devoxx.Main;
import io.github.javathought.devoxx.model.User;
import io.restassured.RestAssured;
import io.restassured.http.Cookie;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.glassfish.grizzly.http.server.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.github.javathought.devoxx.filters.SecurityFilter.AUTHORIZATION_PROPERTY;
import static io.github.javathought.devoxx.filters.SecurityFilter.ROLES;
import static io.github.javathought.devoxx.filters.SecurityFilter.USER_ID;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;


public class ServeurSteps implements En {

    private static final Logger LOG = LoggerFactory.getLogger(ServeurSteps.class);

    private static final int TEST_PORT = 9999;
    private static final String CONTEXT_PATH = "myapp/";
    private ValidatableResponse valResponse;
    private HttpServer server;
    private RequestSpecification api;
    private Response response;
    private Cookie authCookie;

    public ServeurSteps() {

        When("^l'utilisateur se connecte avec l'identifiant \"([^\"]*)\"$",
                (String arg0) -> {
                    Response r = api
                            .header("X-Requested-By", "XMLHttpRequest")
                            .body("{\"username\": \"normal\", \"password\": \"demo\"}")
                            .contentType("application/json")
                            .when().post("myapp/authenticate");
                    String authCookie = r.getCookie(AUTHORIZATION_PROPERTY);
                    LOG.trace("Auth token = {}", authCookie);
                    r.then().statusCode(200);

                } );
        When("^l'utilisateur se connecte avec l'identifiant \"([^\"]*)\" et le mot de passe \"([^\"]*)\"$",
                (String user, String password) -> {
            // Write code here that turns the phrase above into concrete actions
            response = api
                    .header("X-Requested-By", "XMLHttpRequest")
                    .body("{\"username\": \"" + user + "\", \"password\": \"" + password + "\"}")
                    .contentType("application/json")
                    .when().post("myapp/authenticate");
                    authCookie = response.getDetailedCookie(AUTHORIZATION_PROPERTY);
//                    response = api.cookie(authCookie)
//                            .when().get(CONTEXT_PATH + "current");

//                    user = response.getBody().as(User.class);
        });
        And("^la session est présente dans le cookie \"([^\"]*)\"$", (String arg0) -> {
            // Write code here that turns the phrase above into concrete actions
            assertThat(authCookie, is(notNullValue()));

        });
        And("^le cookie de session a l'attribut htpOnly$", () -> {
            // Write code here that turns the phrase above into concrete actions
            assertThat(authCookie.isHttpOnly(), is(true));
        });
        And("^le cookie de session a l'attribut secure$", () -> assertThat(authCookie.isSecured(), is(true)));
        And("^l'utilisateur appelle l'url \"([^\"]*)\"$", (String arg0) ->
                response = api.cookie(authCookie).body("").when().get(CONTEXT_PATH + arg0));
        Then("^le code retour est (\\d+)$", (Integer arg0) -> valResponse = response.then().statusCode(arg0));
        And("^la réponse contient \"([^\"]*)\"$", (String arg0) -> valResponse.body(equalTo(arg0)));
    }

    @Before
    public void setUp() {
        // start the server
        RestAssured.proxy("localhost", 9999);
        server = Main.startServer(TEST_PORT);

        api = given().port(TEST_PORT);
    }

    @After
    public void tearDown(){
       server.shutdown();
    }
}
