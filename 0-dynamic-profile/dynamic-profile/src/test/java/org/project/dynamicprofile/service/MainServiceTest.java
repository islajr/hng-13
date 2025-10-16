package org.project.dynamicprofile.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.project.dynamicprofile.dto.response.CatFact;
import org.project.dynamicprofile.dto.response.MainResponse;
import org.project.dynamicprofile.dto.response.Me;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MainServiceTest {

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    MainService mainService;

    /* Test Data Builders */

    @Value("${api.cat.fact}")
    String catFactAPI;

    @Value("${user.email}")
    String email;

    @Value("${user.name}")
    String name;

    @Value("${user.stack}")
    String stack;

    ResponseEntity<CatFact> generateSampleCatFact() {
        return ResponseEntity.ok(
                CatFact.builder()
                        .fact("cats are ass")
                        .length(12)
                        .build()
        );
    }

     ResponseEntity<CatFact> mimicFactGenerationError() {
        return ResponseEntity.ok(
                CatFact.builder()
                        .fact("")
                        .length(0)
                        .build()
        );
    }

    @Test
    void testShouldGenerateMainResponse() {

        /* initial setup */
        ResponseEntity<CatFact> thirdPartyResponse = generateSampleCatFact();
        assertNotNull(thirdPartyResponse.getBody());

        /* mocking external dependencies */
        when(restTemplate.getForEntity(catFactAPI, CatFact.class)).thenReturn(thirdPartyResponse);

        /* declaring expected values */
        ResponseEntity<MainResponse> expected = ResponseEntity.ok(
            MainResponse.builder()
                .status("success")
                .user(new Me(email, name, stack))
                .timestamp(Instant.now().toString())
                .fact(thirdPartyResponse.getBody().fact())
            .build()
        );

        ResponseEntity<MainResponse> actual = mainService.generateMainResponse();

        assertEquals(expected.getStatusCode(), actual.getStatusCode());
        assertNotNull(actual.getBody());
        assertNotNull(actual.getBody().fact());
        assertNotNull(expected.getBody());
        assertEquals(expected.getBody().fact(), actual.getBody().fact());

    }

    @Test
    void testShouldHandleFactGenerationError() {

        /* initial setup */
        ResponseEntity<CatFact> thirdPartyResponse = mimicFactGenerationError();
        assertNotNull(thirdPartyResponse.getBody());

        /* mocking external dependencies */
        when(restTemplate.getForEntity(catFactAPI, CatFact.class)).thenReturn(thirdPartyResponse);

        /* declaring expected values */
        ResponseEntity<MainResponse> expected = ResponseEntity.ok(
            MainResponse.builder()
                .status("success")
                .user(new Me(email, name, stack))
                .timestamp(Instant.now().toString())
                .fact("Nothing to display!")
            .build()
        );

        ResponseEntity<MainResponse> actual = mainService.generateMainResponse();

        assertEquals(expected.getStatusCode(), actual.getStatusCode());
        assertNotNull(actual.getBody());
        assertNotNull(actual.getBody().fact());
        assertNotNull(expected.getBody());
        assertEquals(expected.getBody().fact(), actual.getBody().fact());

    }
}