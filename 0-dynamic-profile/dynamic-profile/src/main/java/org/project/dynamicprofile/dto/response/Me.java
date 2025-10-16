package org.project.dynamicprofile.dto.response;

import lombok.Builder;

@Builder
public record Me(
        String email,
        String name,
        String stack
) {
    public static Me organize(String name, String email, String stack) {
        return new Me(name, email, stack);
    }
}
