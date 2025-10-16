package org.project.dynamicprofile.dto.response;

import java.time.Instant;

import lombok.Builder;

@Builder
public record MainResponse(
    String status,
    Me user,
    String timestamp,
    String fact
) {
    public static MainResponse generate(CatFact catFact, Me me) {
        return MainResponse.builder()
                .status("success")
                .user(me)
                .timestamp(Instant.now().toString())
                .fact(catFact.length() != 0 ? catFact.fact() : "Nothing to display!")
            .build();
    }
}
