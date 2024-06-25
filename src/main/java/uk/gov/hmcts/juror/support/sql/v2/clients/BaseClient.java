package uk.gov.hmcts.juror.support.sql.v2.clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import uk.gov.hmcts.juror.support.sql.v2.DataCreator;
import uk.gov.hmcts.juror.support.sql.v2.support.JwtDetails;
import uk.gov.hmcts.juror.support.sql.v2.support.JwtDetailsBureau;

import java.net.URI;
import java.util.Map;

@Component
@Slf4j
public class BaseClient {

    protected final RestTemplateBuilder restTemplateBuilder;
    protected final RestTemplate restTemplate;

    protected static ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();


    @Autowired
    public BaseClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplateBuilder = restTemplateBuilder;
        this.restTemplate = restTemplateBuilder
            .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
            .build();
    }

    @SneakyThrows
    protected <P, R> R triggerApi(
        HttpMethod method,
        String url,
        P payload,
        Map<String, String> pathParams,
        Map<String, String> queryParams,
        JwtDetails jwtDetails,
        ParameterizedTypeReference<R> responseType) {
        try {
            LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add("Authorization", jwtDetails.getJwt());
            HttpEntity<?> requestEntity = new HttpEntity<>(payload, headers);

            log.info("Calling API: {} {} with payload: {}", method, createPopulatedUrl(url, pathParams, queryParams),
                mapper.writeValueAsString(payload));
            ResponseEntity<R> response = restTemplate
                .exchange(toUri(url, pathParams, queryParams),
                    method,
                    requestEntity,
                    responseType);
            if (response.getStatusCode().equals(HttpStatus.GATEWAY_TIMEOUT)) {
                throw new RuntimeException("timeout");
            }
            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("Error calling API: {} {} error {}", method, url, response.getBody());
                throw new RuntimeException("Error calling API");
            }
            return response.getBody();
        } catch (Throwable e) {
            log.error("Error calling API: {} {}", method, url);
            if (!DataCreator.ENV.isContinueOnAPIError()) {
                log.error("Failed pausing thread");
                Thread.sleep(Long.MAX_VALUE);
            }
            throw new RuntimeException("Error calling API", e);
        }
    }


    private String createPopulatedUrl(String url, Map<String, String> pathParams, Map<String, String> queryParams) {
        StringBuilder builtUrl = new StringBuilder(DataCreator.ENV.getUrl()).append(url);
        for (Map.Entry<String, String> entry : pathParams.entrySet()) {
            builtUrl = new StringBuilder(builtUrl.toString().replace("{" + entry.getKey() + "}", entry.getValue()));
        }
        if (queryParams != null && !queryParams.isEmpty()) {
            StringBuilder queryParamsBuilder = new StringBuilder();
            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                if (entry.getValue() != null && !entry.getValue().equals("null")) {
                    queryParamsBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                }
            }
            String queryParamsString = queryParamsBuilder.toString();
            if (StringUtils.isEmpty(queryParamsString)) {
                return builtUrl.toString();

            }
            builtUrl.append("?").append(queryParamsString, 0, queryParamsString.length() - 1);
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
