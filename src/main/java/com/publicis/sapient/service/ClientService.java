package com.publicis.sapient.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public interface ClientService {
    ResponseEntity<String> getResponse(HttpHeaders headers);
}
