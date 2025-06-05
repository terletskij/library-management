package com.library.library_management.service;

import com.library.library_management.dto.BorrowResponse;

import java.util.List;

public interface BorrowService {

    void borrowBook(Long memberId, Long bookId);

    void returnBook(Long borrowId);

    List<BorrowResponse> getBorrowedBooksByMember(Long memberId);

    boolean isBookCurrentlyBorrowed(Long bookId);
}