package com.library.library_management.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateMemberRequest(
        @NotBlank(message = "Name is required")
        String name
) {
}
