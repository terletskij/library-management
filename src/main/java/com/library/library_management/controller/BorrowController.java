package com.library.library_management.controller;

import com.library.library_management.service.BorrowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/api/v1/borrows")
@RestController
@Tag(name = "Borrows", description = "Borrowing and returning books")
public class BorrowController {

    @Autowired
    private BorrowService borrowService;

    @Operation(summary = "Borrow a book by member ID and book ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book successfully borrowed"),
            @ApiResponse(responseCode = "400", description = "Validation failed or borrow limit reached"),
            @ApiResponse(responseCode = "404", description = "Member or Book not found")
    })
    @PostMapping
    public ResponseEntity<Void> borrowBook(@RequestParam Long memberId,@RequestParam Long bookId) {
        borrowService.borrowBook(memberId, bookId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Return a borrowed book by borrow ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book successfully returned"),
            @ApiResponse(responseCode = "404", description = "Borrow record not found")
    })
    @PostMapping("/return/{borrowId}")
    public ResponseEntity<Void> returnBook(@PathVariable Long borrowId) {
        borrowService.returnBook(borrowId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get all distinct titles of borrowed books")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Distinct borrowed book titles retrieved")
    })
    @GetMapping("/borrowed-books/distinct-titles")
    public ResponseEntity<List<String>> getDistinctBorrowedBookTitles() {
        List<String> titles = borrowService.getDistinctBorrowedBookTitles();
        return ResponseEntity.ok(titles);
    }

    @Operation(summary = "Get borrowed book titles with total number of times borrowed")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Titles with borrow count retrieved")
    })
    @GetMapping("/borrowed-books/titles-with-count")
    public ResponseEntity<Map<String, Long>> getBorrowedBookTitlesWithCount() {
        Map<String, Long> result = borrowService.getBorrowedBookTitlesWithCount();
        return ResponseEntity.ok(result);
    }
}
