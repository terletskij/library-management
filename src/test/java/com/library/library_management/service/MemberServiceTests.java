package com.library.library_management.service;

import com.library.library_management.dto.CreateMemberRequest;
import com.library.library_management.dto.MemberResponse;
import com.library.library_management.dto.UpdateMemberRequest;
import com.library.library_management.entity.Member;
import com.library.library_management.exception.MemberNotFoundException;
import com.library.library_management.repository.BorrowRepository;
import com.library.library_management.repository.MemberRepository;
import com.library.library_management.service.impl.MemberServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTests {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BorrowRepository borrowRepository;

    @InjectMocks
    private MemberServiceImpl memberService;

    @Test
    void createMember_shouldReturnCreatedMemberResponse() {
        CreateMemberRequest request = new CreateMemberRequest("John Doe");

        Member savedMember = new Member();
        savedMember.setId(1L);
        savedMember.setName("John Doe");
        ReflectionTestUtils.setField(savedMember, "membershipDate", LocalDate.now());

        when(memberRepository.save(any(Member.class))).thenReturn(savedMember);

        MemberResponse response = memberService.createMember(request);

        assertNotNull(response);
        assertEquals(savedMember.getId(), response.id());
        assertEquals("John Doe", response.name());
        assertEquals(savedMember.getMembershipDate(), response.membershipDate());

        verify(memberRepository).save(any(Member.class));
    }

    @Test
    void getMemberById_existingId_shouldReturnMemberResponse() {
        Member member = new Member();
        member.setId(1L);
        member.setName("Jane Doe");
        ReflectionTestUtils.setField(member, "membershipDate", LocalDate.of(2025, 1, 1));

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        MemberResponse response = memberService.getMemberById(1L);

        assertEquals(1L, response.id());
        assertEquals("Jane Doe", response.name());
        assertEquals(LocalDate.of(2025, 1, 1), response.membershipDate());
    }

    @Test
    void getMemberById_nonExistingId_shouldThrowException() {
        when(memberRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class, () -> memberService.getMemberById(99L));
    }

    @Test
    void getAllMembers_shouldReturnListOfMemberResponses() {
        Member member1 = new Member();
        member1.setId(1L);
        member1.setName("Alice");
        ReflectionTestUtils.setField(member1, "membershipDate", LocalDate.of(2023, 5, 1));
        Member member2 = new Member();
        member2.setId(2L);
        member2.setName("Bob");
        ReflectionTestUtils.setField(member2, "membershipDate", LocalDate.of(2023, 6, 1));

        when(memberRepository.findAll()).thenReturn(List.of(member1, member2));

        List<MemberResponse> responses = memberService.getAllMembers();

        assertEquals(2, responses.size());
        assertEquals("Alice", responses.get(0).name());
        assertEquals("Bob", responses.get(1).name());
    }

    @Test
    void updateMemberById_existingId_shouldReturnUpdatedMember() {
        UpdateMemberRequest request = new UpdateMemberRequest("Updated Name");

        Member existingMember = new Member();
        existingMember.setId(1L);
        existingMember.setName("Old Name");
        ReflectionTestUtils.setField(existingMember, "membershipDate", LocalDate.of(2024, 1, 1));

        when(memberRepository.findById(1L)).thenReturn(Optional.of(existingMember));
        when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MemberResponse updated = memberService.updateMemberById(1L, request);

        assertEquals(1L, updated.id());
        assertEquals("Updated Name", updated.name());
        assertEquals(existingMember.getMembershipDate(), updated.membershipDate());

        verify(memberRepository).save(existingMember);
    }

    @Test
    void updateMemberById_nonExistingId_shouldThrowException() {
        UpdateMemberRequest request = new UpdateMemberRequest("Name");

        when(memberRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class, () -> memberService.updateMemberById(99L, request));
    }

    @Test
    void deleteMemberById_existingMemberWithoutBorrows_shouldDelete() {
        Member member = new Member();
        member.setId(1L);

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(borrowRepository.existsByMemberIdAndReturnDateIsNull(1L)).thenReturn(false);

        memberService.deleteMemberById(1L);

        verify(memberRepository).delete(member);
    }

    @Test
    void deleteMemberById_existingMemberWithBorrows_shouldThrowException() {
        Member member = new Member();
        member.setId(1L);

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(borrowRepository.existsByMemberIdAndReturnDateIsNull(1L)).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> memberService.deleteMemberById(1L));
        assertEquals("Member has borrowed books and cannot be deleted", exception.getMessage());

        verify(memberRepository, never()).delete(any());
    }

    @Test
    void deleteMemberById_nonExistingMember_shouldThrowException() {
        when(memberRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class, () -> memberService.deleteMemberById(99L));
    }

    @Test
    void existsById_shouldReturnTrueIfExists() {
        when(memberRepository.existsById(1L)).thenReturn(true);

        assertTrue(memberService.existsById(1L));
    }

    @Test
    void existsById_shouldReturnFalseIfNotExists() {
        when(memberRepository.existsById(99L)).thenReturn(false);

        assertFalse(memberService.existsById(99L));
    }
}
