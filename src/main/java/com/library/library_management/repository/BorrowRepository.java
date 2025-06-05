package com.library.library_management.repository;

import com.library.library_management.entity.Borrow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowRepository extends JpaRepository<Borrow, Long> {

    boolean existsByBookIdAndReturnDateIsNull(Long bookId);

    boolean existsByMemberIdAndReturnDateIsNull(Long memberId);

    int countByMemberIdAndReturnDateIsNull(Long memberId);

    List<Borrow> findByMemberIdAndReturnDateIsNull(Long memberId);
}
