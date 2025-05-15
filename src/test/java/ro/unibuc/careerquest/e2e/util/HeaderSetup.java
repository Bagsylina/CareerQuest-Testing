package ro.unibuc.careerquest.e2e.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.web.client.RequestCallback;

import java.util.Map;

public class HeaderSetup implements RequestCallback {

    private final Map<String, String> requestHeaders;
    private final String body;

    public HeaderSetup(final Map<String, String> headers) {
        this.requestHeaders = headers;
        this.body = "";
    }

    // Constructor for POST (headers + body)
    public HeaderSetup(final Map<String, String> headers, final String body) {
        this.headers = headers;
        this.body = body;
    }

    @Override
    public void doWithRequest(ClientHttpRequest request) {
        final HttpHeaders clientHeaders = request.getHeaders();
        for (final Map.Entry<String, String> entry : requestHeaders.entrySet()) {
            clientHeaders.add(entry.getKey(), entry.getValue());
        }
    }
}
