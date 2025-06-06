package com.library.library_management.dto;

import java.time.LocalDate;

public record MemberResponse(
        Long id,
        String name,
        LocalDate membershipDate
) {
}
