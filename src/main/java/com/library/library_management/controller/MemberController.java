package com.library.library_management.controller;

import com.library.library_management.dto.BorrowResponse;
import com.library.library_management.dto.CreateMemberRequest;
import com.library.library_management.dto.MemberResponse;
import com.library.library_management.dto.UpdateMemberRequest;
import com.library.library_management.service.BorrowService;
import com.library.library_management.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/members")
@RestController
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private BorrowService borrowService;

    @PostMapping
    public ResponseEntity<MemberResponse> createMember(@Valid @RequestBody CreateMemberRequest request) {
        MemberResponse member = memberService.createMember(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(member);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> getMemberById(@PathVariable Long id) {
        MemberResponse member = memberService.getMemberById(id);
        return ResponseEntity.ok(member);
    }

    @GetMapping
    public ResponseEntity<List<MemberResponse>> getAllMembers() {
        List<MemberResponse> members = memberService.getAllMembers();
        return ResponseEntity.ok(members);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MemberResponse> updateMember(@PathVariable Long id, @Valid @RequestBody UpdateMemberRequest request) {
        MemberResponse updated = memberService.updateMemberById(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMemberById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/borrowed-books")
    public ResponseEntity<List<BorrowResponse>> getBorrowedBooksByMember(@PathVariable Long id) {
        List<BorrowResponse> borrowedBooks = borrowService.getBorrowedBooksByMember(id);
        return ResponseEntity.ok(borrowedBooks);
    }


    @GetMapping("/by-name/{name}/borrowed-books")
    public ResponseEntity<List<BorrowResponse>> getBorrowedBooksByMemberName(@PathVariable String name) {
        List<BorrowResponse> borrowedBooks = borrowService.getBorrowedBooksByMemberName(name);
        return ResponseEntity.ok(borrowedBooks);
    }


}
