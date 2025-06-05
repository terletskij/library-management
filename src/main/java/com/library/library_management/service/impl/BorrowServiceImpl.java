package com.library.library_management.service.impl;

import com.library.library_management.dto.BookResponse;
import com.library.library_management.dto.BorrowResponse;
import com.library.library_management.dto.UpdateBookRequest;
import com.library.library_management.dto.mapper.BookMapper;
import com.library.library_management.dto.mapper.BorrowMapper;
import com.library.library_management.entity.Book;
import com.library.library_management.entity.Borrow;
import com.library.library_management.entity.Member;
import com.library.library_management.exception.BookNotAvailableException;
import com.library.library_management.exception.BorrowLimitExceededException;
import com.library.library_management.exception.BorrowNotFoundException;
import com.library.library_management.exception.MemberNotFoundException;
import com.library.library_management.repository.BorrowRepository;
import com.library.library_management.repository.MemberRepository;
import com.library.library_management.service.BookService;
import com.library.library_management.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BorrowServiceImpl implements BorrowService {

    // Limit of the borrowed books per member
    @Value("${borrow.limit}")
    private int borrowLimit;

    @Autowired
    private BorrowRepository borrowRepository;

    @Autowired
    private BookService bookService;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private BorrowMapper borrowMapper;

    // TODO: change to MemberService when its ready
    @Autowired
    private MemberRepository memberRepository;

    @Override
    public void borrowBook(Long memberId, Long bookId) {

        BookResponse bookResponse = bookService.getBookById(bookId);
        Book book = bookMapper.fromBookResponse(bookResponse);

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
        UpdateBookRequest request = bookMapper.toUpdateRequest(book);
        bookService.updateBookById(book.getId(), request);
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
        UpdateBookRequest updateRequest = bookMapper.toUpdateRequest(book);

        bookService.updateBookById(book.getId(), updateRequest);
    }

    @Override
    public List<BorrowResponse> getBorrowedBooksByMember(Long memberId) {
        List<Borrow> borrows = borrowRepository.findByMemberIdAndReturnDateIsNull(memberId);
        return borrows.stream().map(borrow -> borrowMapper.toResponse(borrow)).toList();
    }

    @Override
    public boolean isBookCurrentlyBorrowed(Long bookId) {
        return borrowRepository.existsByBookIdAndReturnDateIsNull(bookId);
    }
}
