package com.library.library_management.service.impl;

import com.library.library_management.dto.CreateMemberRequest;
import com.library.library_management.dto.MemberResponse;
import com.library.library_management.dto.UpdateMemberRequest;
import com.library.library_management.entity.Member;
import com.library.library_management.exception.MemberNotFoundException;
import com.library.library_management.repository.MemberRepository;
import com.library.library_management.service.BorrowService;
import com.library.library_management.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BorrowService borrowService;

    @Override
    public MemberResponse createMember(CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.name());
        return toResponse(member);
    }

    @Override
    public MemberResponse getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Member not found"));
        return toResponse(member);
    }

    @Override
    public List<MemberResponse> getAllMembers() {
        return memberRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public MemberResponse updateMemberById(Long id, UpdateMemberRequest request) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Member not found"));
        member.setName(request.name());
        memberRepository.save(member);
        return toResponse(member);
    }

    @Override
    public void deleteMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Member not found"));
        if (borrowService.isMemberCurrentlyBorrowing(id)) {
            throw new RuntimeException("Member has borrowed books and cannot be deleted");
        }
        memberRepository.delete(member);
    }

    @Override
    public Member getMemberEntityById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Member not found"));
    }

    @Override
    public boolean existsById(Long id) {
        return memberRepository.existsById(id);
    }

    private MemberResponse toResponse(Member member) {
        return new MemberResponse(member.getId(), member.getName(), member.getMembershipDate());
    }
}
