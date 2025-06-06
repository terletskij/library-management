package com.library.library_management.service.impl;

import com.library.library_management.dto.BorrowResponse;
import com.library.library_management.dto.mapper.BookMapper;
import com.library.library_management.dto.mapper.BorrowMapper;
import com.library.library_management.entity.Book;
import com.library.library_management.entity.Borrow;
import com.library.library_management.entity.Member;
import com.library.library_management.exception.*;
import com.library.library_management.repository.BookRepository;
import com.library.library_management.repository.BorrowRepository;
import com.library.library_management.repository.MemberRepository;
import com.library.library_management.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BorrowServiceImpl implements BorrowService {

    // Limit of the borrowed books per member
    @Value("${borrow.limit}")
    private int borrowLimit;

    @Autowired
    private BorrowRepository borrowRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private BorrowMapper borrowMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public void borrowBook(Long memberId, Long bookId) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found"));

        if (book.getAmount() == 0) {
            throw new BookNotAvailableException("Book is not available for borrowing");
        }

        int borrowCount = borrowRepository.countByMemberIdAndReturnDateIsNull(memberId);
        if (borrowCount >= borrowLimit) {
            throw new BorrowLimitExceededException("Member has reached the max borrow limit");
        }
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found"));

        Borrow borrow = new Borrow(book, member, LocalDate.now());
        borrowRepository.save(borrow);

        book.setAmount(book.getAmount() - 1);
        bookRepository.save(book);
    }

    @Override
    public void returnBook(Long borrowId) {
        Borrow borrow = borrowRepository.findById(borrowId)
                .orElseThrow(() -> new BorrowNotFoundException("Borrow not found"));
        if (borrow.getReturnDate() != null) {
            throw new IllegalStateException("Book already returned");
        }

        borrow.setReturnDate(LocalDate.now());
        borrowRepository.save(borrow);

        Book book = borrow.getBook();
        book.setAmount(book.getAmount() + 1);
        bookRepository.save(book);
    }

    @Override
    public List<BorrowResponse> getBorrowedBooksByMember(Long memberId) {
        List<Borrow> borrows = borrowRepository.findByMemberIdAndReturnDateIsNull(memberId);
        return borrows.stream().map(borrow -> borrowMapper.toResponse(borrow)).toList();
    }

    @Override
    public List<BorrowResponse> getBorrowedBooksByMemberName(String memberName) {
        Member member = memberRepository.findByName(memberName)
                .orElseThrow(() -> new MemberNotFoundException("Member not found"));
        List<Borrow> borrows = borrowRepository.findByMemberIdAndReturnDateIsNull(member.getId());
        return borrows.stream()
                .map(borrowMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isBookCurrentlyBorrowed(Long bookId) {
        return borrowRepository.existsByBookIdAndReturnDateIsNull(bookId);
    }

    @Override
    public boolean isMemberCurrentlyBorrowing(Long memberId) {
        return borrowRepository.existsByMemberIdAndReturnDateIsNull(memberId);
    }

    @Override
    public List<String> getDistinctBorrowedBookTitles() {
        List<Borrow> borrows = borrowRepository.findByReturnDateIsNull();
        return borrows.stream()
                .map(borrow -> borrow.getBook().getTitle())
                .distinct()
                .toList();
    }

    @Override
    public Map<String, Long> getBorrowedBookTitlesWithCount() {
        List<Borrow> borrows = borrowRepository.findByReturnDateIsNull();

        return borrows.stream()
                .collect(Collectors.groupingBy(
                        borrow -> borrow.getBook().getTitle(),
                        Collectors.counting()
                ));
    }
}
