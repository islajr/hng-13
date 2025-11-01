package org.project.strimr.controller;

import lombok.RequiredArgsConstructor;
import org.project.strimr.dto.agent.AgentCard;
import org.project.strimr.service.AgentCardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AgentCardController {

    private final AgentCardService agentCardService;

    @GetMapping("/.well-known/agent.json")
    public ResponseEntity<AgentCard> getAgentCard() {
        return agentCardService.generateCard();
    }
}
