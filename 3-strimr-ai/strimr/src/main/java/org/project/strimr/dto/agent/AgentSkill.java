package org.project.strimr.dto.agent;

import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record AgentSkill(
        String id,
        String name,
        String description,
        List<String> inputModes,
        List<String> outputModes,
        List<Map<String, Map<String, List<Map<String, String>>>>> examples
) {
}
