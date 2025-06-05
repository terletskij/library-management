package com.library.library_management.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateBookRequest(
        @NotBlank(message = "Title is required")
        @Size(min = 3, message = "Title must be at least 3 characters")
        @Pattern(regexp = "^[A-Z].*", message = "Title must start with a capital letter")
        String title,

        @NotBlank(message = "Author is required")
        @Pattern(regexp = "^[A-Z][a-z]+\\s[A-Z][a-z]+$", message = "Author must be in format 'Name Surname' with capital letters")
        String author,

        @Min(value = 0, message = "Amount cannot be negative")
        int amount
) {
}
