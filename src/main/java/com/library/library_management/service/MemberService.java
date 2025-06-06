package com.library.library_management.service;

import com.library.library_management.dto.CreateMemberRequest;
import com.library.library_management.dto.MemberResponse;
import com.library.library_management.dto.UpdateMemberRequest;
import com.library.library_management.entity.Member;

import java.util.List;

public interface MemberService {
    MemberResponse createMember(CreateMemberRequest request);

    MemberResponse getMemberById(Long id);

    List<MemberResponse> getAllMembers();

    MemberResponse updateMemberById(Long id, UpdateMemberRequest request);

    void deleteMemberById(Long id);

    Member getMemberEntityById(Long id);

    boolean existsById(Long id);
}
