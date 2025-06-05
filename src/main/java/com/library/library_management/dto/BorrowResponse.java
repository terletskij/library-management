package com.library.library_management.dto;

import java.time.LocalDate;

public record BorrowResponse(
        Long id,
        Long bookId,
        Long memberId,
        LocalDate borrowDate,
        LocalDate returnDate
) {
}
