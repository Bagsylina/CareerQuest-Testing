package ro.unibuc.careerquest.e2e.steps;

//import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import ro.unibuc.careerquest.e2e.util.ResponseResults;
import ro.unibuc.careerquest.e2e.util.HeaderSetup;
import ro.unibuc.careerquest.e2e.util.ResponseErrorHandler;

public class CareerQuestSteps {
    public static ResponseResults latestResponse = null;

    @Autowired
    protected RestTemplate restTemplate;

    @Given("^the employer posts first job to /job$")
    public void the_client_posts_a_job_to_job() throws JsonProcessingException {
        String url = "http://localhost:8080/job";
        Map<String, Object> jobPayload = new HashMap<>();
        jobPayload.put("title", "Software developer");
        jobPayload.put("description", "8 hr workdays, a year at least");
        jobPayload.put("employer", "Adobe");
        jobPayload.put("salary", 5000);
        jobPayload.put("location", "San Francisco");
        jobPayload.put("abilities", List.of("Java", "C"));
        jobPayload.put("domains", List.of("SWE"));
        jobPayload.put("characteristics", List.of("paid leave", "benefits"));

        String jsonPayload = new ObjectMapper().writeValueAsString(jobPayload);

        executePost(url, jsonPayload);
    }

    // @Given("^the employer posts second job to /job$")
    // public void the_client_posts_another_job_to_job() throws JsonProcessingException {
    //     String url = "http://localhost:8080/job";
    //     Map<String, Object> jobPayload = new HashMap<>();
    //     jobPayload.put("title", "Software developer");
    //     jobPayload.put("description", "8 hr workdays, a year at least");
    //     jobPayload.put("employer", "Adobe");
    //     jobPayload.put("salary", 5000);
    //     jobPayload.put("location", "San Francisco");
    //     jobPayload.put("abilities", List.of("Java", "C"));
    //     jobPayload.put("domains", List.of("SWE"));
    //     jobPayload.put("characteristics", List.of("paid leave", "benefits"));

    //     String jsonPayload = new ObjectMapper().writeValueAsString(jobPayload);

    //     executePost(url, jsonPayload);
    // }

    @Then("^the employer receives status code of (\\d+)$")
    public void the_employer_receives_status_code_of(int statusCode) throws Throwable {
        final HttpStatusCode currentStatusCode = latestResponse.getTheResponse().getStatusCode();
        assertThat("status code is correct : " + latestResponse.getBody(), currentStatusCode.value(), is(statusCode)); // why incorrect?
    }

    // @And("^the client receives response (.+)$")
    // public void the_client_receives_response(String response) throws JsonProcessingException {
    //     String latestResponseBody = latestResponse.getBody();
    //     Greeting greeting = new ObjectMapper().readValue(latestResponseBody, Greeting.class);
    //     assertThat("Response received is incorrect", greeting.getContent(), is(response));
    // }

    public void executePost(String url, String jsonPayload) {
        final Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        final HeaderSetup requestCallback = new HeaderSetup(headers);
        final ResponseErrorHandler errorHandler = new ResponseErrorHandler();

        restTemplate.setErrorHandler(errorHandler);
        latestResponse = restTemplate.execute(url, HttpMethod.POST, requestCallback, response -> {
            if (errorHandler.getHadError()) {
                return errorHandler.getResults();
            } else {
                return new ResponseResults(response);
            }
        });
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

}
