package ro.unibuc.careerquest.e2e.steps;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.spring.CucumberContextConfiguration;
import ro.unibuc.careerquest.dto.UserCreation;
import ro.unibuc.hello.dto.Greeting;
import ro.unibuc.hello.e2e.util.HeaderSetup;
import ro.unibuc.hello.e2e.util.ResponseErrorHandler;
import ro.unibuc.hello.e2e.util.ResponseResults;

@CucumberContextConfiguration
@SpringBootTest()
public class UserSteps {
    
    public static ResponseResults latestResponse = null;

    @Autowired
    protected RestTemplate restTemplate;


    @Given("^the client calls /test")
    public void the_client_issues_GET_test() {
        executeGet("http://localhost:8080/test");
    }

    @Given("^the client calls /users")
    public void the_client_issues_GET_users() {
        executeGet("http://localhost:8080/users");
    }

    /*@Given("^the client creates a user with username \"([^\"]*)\", email \"([^\"]*)\" and password \"([^\"]*)\"")
    public void the_client_creates_user(String username, String email, String password) {
        UserCreation user = new UserCreation(username, password, email);
        executePost("http://localhost:8080/user", user);
    }*/

    @Then("^the client receives status code of (\\d+)$")
    public void the_client_receives_status_code_of(int statusCode) throws Throwable {
        final HttpStatusCode currentStatusCode = latestResponse.getTheResponse().getStatusCode();
        assertThat("status code is incorrect : " + latestResponse.getBody(), currentStatusCode.value(), is(statusCode));
    }

    @And("^the client receives response (.+)$")
    public void the_client_receives_response(String response) throws JsonProcessingException {
        String latestResponseBody = latestResponse.getBody();
        Greeting greeting = new ObjectMapper().readValue(latestResponseBody, Greeting.class);
        assertThat("Response received is incorrect", greeting.getContent(), is(response));
    }

    public void executeGet(String url) {
        final Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        final HeaderSetup requestCallback = new HeaderSetup(headers);
        final ResponseErrorHandler errorHandler = new ResponseErrorHandler();

        restTemplate.setErrorHandler(errorHandler);
        latestResponse = restTemplate.execute(url, HttpMethod.GET, requestCallback, response -> {
            if (errorHandler.getHadError()) {
                return (errorHandler.getResults());
            } else {
                return (new ResponseResults(response));
            }
        });
    }

    /*public void executePost(String url, Object body) throws JsonProcessingException {
        final Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");

        final HeaderSetup requestCallback = new HeaderSetup(headers, new ObjectMapper().writeValueAsString(body));
        final ResponseErrorHandler errorHandler = new ResponseErrorHandler();

        restTemplate.setErrorHandler(errorHandler);
        latestResponse = restTemplate.execute(url, HttpMethod.POST, requestCallback, response -> {
            if (errorHandler.getHadError()) {
                return errorHandler.getResults();
            } else {
                return new ResponseResults(response);
            }
        });
    }*/
}
