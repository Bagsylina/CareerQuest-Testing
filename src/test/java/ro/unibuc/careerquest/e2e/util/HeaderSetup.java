package ro.unibuc.careerquest.e2e.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.web.client.RequestCallback;

import org.springframework.lang.NonNull;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HeaderSetup implements RequestCallback {

    private final Map<String, String> requestHeaders;
    private final String body; // Nullable

    public HeaderSetup(Map<String, String> headers) {
        this(headers, null);
    }

    public HeaderSetup(Map<String, String> headers, String body) {
        this.requestHeaders = headers;
        this.body = body;
    }

    @Override
    public void doWithRequest(@NonNull ClientHttpRequest request) throws IOException {
        final HttpHeaders clientHeaders = request.getHeaders();

        for (final Map.Entry<String, String> entry : requestHeaders.entrySet()) {
            clientHeaders.add(entry.getKey(), entry.getValue());
        }

        if (body != null) {
            try (OutputStream os = request.getBody()) {
                os.write(body.getBytes(StandardCharsets.UTF_8));
            }
        }
    }

    // @Override
    // public void doWithRequest(@NonNull ClientHttpRequest request) {
    //     final HttpHeaders clientHeaders = request.getHeaders();

    //     for (final Map.Entry<String, String> entry : requestHeaders.entrySet()) {
    //         clientHeaders.add(entry.getKey(), entry.getValue());
    //     }
    // }
}
