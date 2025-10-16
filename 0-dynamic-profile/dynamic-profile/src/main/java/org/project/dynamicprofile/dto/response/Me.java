package org.project.dynamicprofile.dto.response;

import lombok.Builder;

@Builder
public record Me(
        String email,
        String name,
        String stack
) {
    public static Me organize(String email, String name, String stack) {
        return Me.builder()
            .email(email)
            .name(name)
            .stack(stack)
        .build();    
    }
}
