package com.library.library_management.controller;

import com.library.library_management.dto.BorrowResponse;
import com.library.library_management.dto.CreateMemberRequest;
import com.library.library_management.dto.MemberResponse;
import com.library.library_management.dto.UpdateMemberRequest;
import com.library.library_management.service.BorrowService;
import com.library.library_management.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/members")
@RestController
@Tag(name = "Members", description = "Member REST calls")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private BorrowService borrowService;

    @Operation(summary = "Create a new member")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Member created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid member data")
    })
    @PostMapping
    public ResponseEntity<MemberResponse> createMember(@Valid @RequestBody CreateMemberRequest request) {
        MemberResponse member = memberService.createMember(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(member);
    }

    @Operation(summary = "Get a member by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Member retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Member with given ID not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> getMemberById(@PathVariable Long id) {
        MemberResponse member = memberService.getMemberById(id);
        return ResponseEntity.ok(member);
    }

    @Operation(summary = "Get all members")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All members retrieved")
    })
    @GetMapping
    public ResponseEntity<List<MemberResponse>> getAllMembers() {
        List<MemberResponse> members = memberService.getAllMembers();
        return ResponseEntity.ok(members);
    }

    @Operation(summary = "Update a member by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Member updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid member data"),
            @ApiResponse(responseCode = "404", description = "Member with given ID not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MemberResponse> updateMember(@PathVariable Long id, @Valid @RequestBody UpdateMemberRequest request) {
        MemberResponse updated = memberService.updateMemberById(id, request);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete a member by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Member deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Member has borrowed books and cannot be deleted"),
            @ApiResponse(responseCode = "404", description = "Member with given ID not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMemberById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get borrowed books by member ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Borrowed books retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Member with given ID not found")
    })
    @GetMapping("/{id}/borrowed-books")
    public ResponseEntity<List<BorrowResponse>> getBorrowedBooksByMember(@PathVariable Long id) {
        List<BorrowResponse> borrowedBooks = borrowService.getBorrowedBooksByMember(id);
        return ResponseEntity.ok(borrowedBooks);
    }

    @Operation(summary = "Get borrowed books by member name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Borrowed books retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Member with given name not found")
    })
    @GetMapping("/by-name/{name}/borrowed-books")
    public ResponseEntity<List<BorrowResponse>> getBorrowedBooksByMemberName(@PathVariable String name) {
        List<BorrowResponse> borrowedBooks = borrowService.getBorrowedBooksByMemberName(name);
        return ResponseEntity.ok(borrowedBooks);
    }


}
