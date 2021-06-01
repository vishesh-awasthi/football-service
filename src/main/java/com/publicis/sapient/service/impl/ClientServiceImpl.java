package com.publicis.sapient.service.impl;

import com.publicis.sapient.service.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@Slf4j
public class ClientServiceImpl implements ClientService {

    private static final RestTemplate REST_TEMPLATE = new RestTemplate();

    @Value("${api.base.url}")
    String baseUrl;

    @Value("${api.secret.key}")
    String apiSecretKey;

    @Override
    public ResponseEntity<String> getResponse(HttpHeaders headers) {
        headers.add("APIkey", apiSecretKey);
        URI uri = UriComponentsBuilder.fromUriString(baseUrl).queryParams(headers).build().toUri();
        log.debug("Making call to client with {}", uri);
        return REST_TEMPLATE.getForEntity(uri, String.class);
    }
}
