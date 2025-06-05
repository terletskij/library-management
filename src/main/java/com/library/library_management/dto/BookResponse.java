package com.library.library_management.dto;

public record BookResponse(
        Long id,
        String title,
        String author,
        int amount
) {
}
