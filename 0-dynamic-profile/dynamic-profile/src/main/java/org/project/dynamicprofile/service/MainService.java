package org.project.dynamicprofile.service;

import org.project.dynamicprofile.dto.response.CatFact;
import org.project.dynamicprofile.dto.response.MainResponse;
import org.project.dynamicprofile.dto.response.Me;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MainService {

    private final RestTemplate restTemplate;
    
    @Value("${api.cat.fact}")
    private String catFactAPI;

    @Value("${user.name}")
    private String name;

    @Value("${user.email}")
    private String email;

    @Value("${user.stack}")
    private String stack;

    public ResponseEntity<MainResponse> generateMainResponse() {
        
        log.info("querying API for new cat fact");
        ResponseEntity<CatFact> catFact = restTemplate.getForEntity(catFactAPI, CatFact.class);

        Me me = Me.organize(email, name, stack);

        switch (catFact.getStatusCode().value()) {
            case 200 -> {
                assert catFact.getBody() != null;
                log.info("successfully obtained new cat fact");
                return ResponseEntity.ok(MainResponse.generate(catFact.getBody(), me));
            }
            case 404 -> {
                assert catFact.getBody() != null;
                log.info("API failed to return new cat fact");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MainResponse.generate(catFact.getBody(), me));
            }

            default -> throw new RuntimeException("Something went wrong!");
        }

    }

}
