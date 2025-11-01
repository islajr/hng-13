package org.project.strimr.dto.agent;

import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record AgentCard(
        String name,
        String description,
        String url,
        Map<String, String> provider,
        String version,
        Map<String, Boolean> capabilities,
        List<String> defaultInputModes,
        List<String> defaultOutputModes,
        List<AgentSkill> skills,
        Boolean supportsAuthenticatedExtendedCard
) {
}
