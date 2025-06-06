package com.library.library_management.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateMemberRequest(
        @NotBlank(message = "Name is required")
        String name
) {
}
