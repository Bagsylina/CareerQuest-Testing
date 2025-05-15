package ro.unibuc.careerquest.e2e.util;

import org.springframework.http.client.ClientHttpResponse;

import org.springframework.lang.NonNull;

import java.io.IOException;

public class ResponseErrorHandler implements org.springframework.web.client.ResponseErrorHandler {

    private ResponseResults results = null;
    private Boolean hadError = false;

    public ResponseResults getResults() {
        return results;
    }

    public Boolean getHadError() {
        return hadError;
    }

    @Override
    public boolean hasError(@NonNull ClientHttpResponse response) throws IOException {
        hadError = response.getStatusCode().value() >= 400;
        return hadError;
    }

    @Override
    public void handleError(@NonNull ClientHttpResponse response) throws IOException {
        results = new ResponseResults(response);
    }
}
