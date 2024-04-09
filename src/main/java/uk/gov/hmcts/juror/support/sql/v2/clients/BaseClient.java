package uk.gov.hmcts.juror.support.sql.v2.clients;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import uk.gov.hmcts.juror.support.sql.v2.support.JwtDetails;

import java.net.URI;
import java.util.Map;

@Component
@Slf4j
public class BaseClient {

    private static final String API_URL = "http://localhost:8080";
    protected final RestTemplateBuilder restTemplateBuilder;
    protected final RestTemplate restTemplate;

    @Autowired
    public BaseClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplateBuilder = restTemplateBuilder;
        this.restTemplate = restTemplateBuilder.build();
    }

    protected <P, R> R triggerApi(
        HttpMethod method,
        String url,
        P payload,
        Map<String, String> pathParams,
        Map<String, String> queryParams,
        JwtDetails jwtDetails,
        ParameterizedTypeReference<R> responseType) {
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", jwtDetails.getJwt());
        HttpEntity<?> requestEntity = new HttpEntity<>(payload, headers);

        log.info("Calling API: {}", createPopulatedUrl(url, pathParams, queryParams));
        ResponseEntity<R> response = restTemplate
            .exchange(toUri(url, pathParams, queryParams),
                method,
                requestEntity,
                responseType);
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("Error calling API: {} error {}",url, response.getBody());
            throw new RuntimeException("Error calling API");
        }
        return response.getBody();
    }


    private String createPopulatedUrl(String url, Map<String, String> pathParams, Map<String, String> queryParams) {
        StringBuilder builtUrl = new StringBuilder(API_URL).append(url);
        for (Map.Entry<String, String> entry : pathParams.entrySet()) {
            builtUrl = new StringBuilder(builtUrl.toString().replace("{" + entry.getKey() + "}", entry.getValue()));
        }
        if (queryParams != null && !queryParams.isEmpty()) {
            builtUrl.append("?");
            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                builtUrl.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            builtUrl = new StringBuilder(builtUrl.substring(0, builtUrl.length() - 1));
        }
        return builtUrl.toString();
    }

    private URI toUri(String url, Map<String, String> pathParams, Map<String, String> queryParams) {
        return URI.create(createPopulatedUrl(url, pathParams, queryParams));
    }

    protected <P> void triggerApiVoidReturn(
        HttpMethod method,
        String url,
        P payload,
        Map<String, String> pathParams,
        Map<String, String> queryParams,
        JwtDetails jwtDetails) {
        triggerApi(
            method,
            url,
            payload,
            pathParams,
            queryParams,
            jwtDetails,
            new ParameterizedTypeReference<Void>() {
            }
        );
    }
}
