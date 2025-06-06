package com.library.library_management.controller;

import com.library.library_management.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/api/v1/borrows")
@RestController
public class BorrowController {

    @Autowired
    private BorrowService borrowService;

    @PostMapping
    public ResponseEntity<Void> borrowBook(@RequestParam Long memberId,@RequestParam Long bookId) {
        borrowService.borrowBook(memberId, bookId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/return/{borrowId}")
    public ResponseEntity<Void> returnBook(@PathVariable Long borrowId) {
        borrowService.returnBook(borrowId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/borrowed-books/distinct-titles")
    public ResponseEntity<List<String>> getDistinctBorrowedBookTitles() {
        List<String> titles = borrowService.getDistinctBorrowedBookTitles();
        return ResponseEntity.ok(titles);
    }

    @GetMapping("/borrowed-books/titles-with-count")
    public ResponseEntity<Map<String, Long>> getBorrowedBookTitlesWithCount() {
        Map<String, Long> result = borrowService.getBorrowedBookTitlesWithCount();
        return ResponseEntity.ok(result);
    }
}
