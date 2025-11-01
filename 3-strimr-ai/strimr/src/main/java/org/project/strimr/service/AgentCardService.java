package org.project.strimr.service;

import lombok.extern.slf4j.Slf4j;
import org.project.strimr.dto.agent.AgentCard;
import org.project.strimr.dto.agent.AgentSkill;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AgentCardService {

    @Value("${strimr.url}")
    private String strimrURL;

    @Value("${agent.organization.name}")
    private String organization;

    @Value("${agent.organization.url}")
    private String orgURL;

    public ResponseEntity<AgentCard> generateCard() {

        String mockRequest = "";
        String mockResponse = "";

        log.info("Generating agent card");
        return ResponseEntity.ok(
                AgentCard.builder()
                        .name("Strimr")
                        .description("Your friendly neighbourhood music agent")
                        .url(strimrURL)
                        .provider(Map.of(
                               "organization", organization,
                               "url", orgURL
                        ))
                        .capabilities(Map.of(
                                "streaming", false,
                                "pushNotifications", false,
                                "stateTransitionHistory", false
                        ))
                        .defaultInputModes(List.of("application/json"))
                        .defaultOutputModes(List.of("application/json"))
                        .skills(List.of(AgentSkill.builder()
                                        .id("1")
                                        .name("Song Look-Up")
                                        .description("Provides requested track details")
                                        .inputModes(List.of("application/json"))
                                        .outputModes(List.of("application/json"))
                                        .examples(List.of(Map.of(
                                                "input", Map.of("parts", List.of(Map.of("json", mockRequest))),
                                                "output", Map.of("parts", List.of(Map.of("json", mockResponse)))
                                                )))
                                .build()))
                        .supportsAuthenticatedExtendedCard(false)
                        .build()
        );
    }
}
