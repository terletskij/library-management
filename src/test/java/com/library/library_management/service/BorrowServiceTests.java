package com.library.library_management.service;

import com.library.library_management.dto.BorrowResponse;
import com.library.library_management.dto.mapper.BorrowMapper;
import com.library.library_management.entity.Book;
import com.library.library_management.entity.Borrow;
import com.library.library_management.entity.Member;
import com.library.library_management.exception.*;
import com.library.library_management.repository.BookRepository;
import com.library.library_management.repository.BorrowRepository;
import com.library.library_management.repository.MemberRepository;
import com.library.library_management.service.impl.BorrowServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BorrowServiceTests {

    @InjectMocks
    private BorrowServiceImpl borrowService;

    @Mock
    private BorrowRepository borrowRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BorrowMapper borrowMapper;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(borrowService, "borrowLimit", 2);
    }

    @Test
    void borrowBook_shouldSucceed() {
        Long memberId = 1L;
        Long bookId = 1L;
        Book book = new Book("Title", "Author", 3);
        book.setId(bookId);
        Member member = new Member("John Doe");
        member.setId(memberId);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(borrowRepository.countByMemberIdAndReturnDateIsNull(memberId)).thenReturn(0);
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        borrowService.borrowBook(memberId, bookId);

        assertEquals(2, book.getAmount());
        verify(borrowRepository).save(any(Borrow.class));
        verify(bookRepository).save(book);
    }

    @Test
    void borrowBook_shouldThrow_whenBookNotFound() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, () -> borrowService.borrowBook(1L, 1L));
    }

    @Test
    void borrowBook_shouldThrow_whenBookNotAvailable() {
        Book book = new Book("Title", "Author", 0);
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        assertThrows(BookNotAvailableException.class, () -> borrowService.borrowBook(1L, 1L));
    }

    @Test
    void borrowBook_shouldThrow_whenLimitExceeded() {
        Book book = new Book("Title", "Author", 1);
        Member member = new Member("John Doe");
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(borrowRepository.countByMemberIdAndReturnDateIsNull(anyLong())).thenReturn(2);
        assertThrows(BorrowLimitExceededException.class, () -> borrowService.borrowBook(1L, 1L));
    }

    @Test
    void borrowBook_shouldThrow_whenMemberNotFound() {
        Book book = new Book("Title", "Author", 1);

        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(borrowRepository.countByMemberIdAndReturnDateIsNull(anyLong())).thenReturn(0);
        when(memberRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class, () -> borrowService.borrowBook(1L, 1L));
    }

    @Test
    void returnBook_shouldSucceed() {
        Borrow borrow = new Borrow();
        borrow.setId(1L);
        borrow.setBook(new Book("Title", "Author", 2));

        when(borrowRepository.findById(1L)).thenReturn(Optional.of(borrow));

        borrowService.returnBook(1L);

        assertNotNull(borrow.getReturnDate());
        assertEquals(3, borrow.getBook().getAmount());
        verify(bookRepository).save(borrow.getBook());
    }

    @Test
    void returnBook_shouldThrow_whenNotFound() {
        when(borrowRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(BorrowNotFoundException.class, () -> borrowService.returnBook(1L));
    }

    @Test
    void returnBook_shouldThrow_whenAlreadyReturned() {
        Borrow borrow = new Borrow();
        borrow.setReturnDate(LocalDate.now());
        when(borrowRepository.findById(anyLong())).thenReturn(Optional.of(borrow));
        assertThrows(IllegalStateException.class, () -> borrowService.returnBook(1L));
    }

    @Test
    void getBorrowedBooksByMember_shouldReturnList() {
        Borrow borrow = new Borrow();
        borrow.setId(1L);
        when(borrowRepository.findByMemberIdAndReturnDateIsNull(1L))
                .thenReturn(List.of(borrow));
        when(borrowMapper.toResponse(any())).thenReturn(new BorrowResponse(1L, 2L, 1L, LocalDate.now(), null));

        List<BorrowResponse> responses = borrowService.getBorrowedBooksByMember(1L);

        assertEquals(1, responses.size());
    }

    @Test
    void getBorrowedBooksByMemberName_shouldReturnList() {
        Member member = new Member("Alice");
        member.setId(1L);
        Borrow borrow = new Borrow();

        when(memberRepository.findByName("Alice")).thenReturn(Optional.of(member));
        when(borrowRepository.findByMemberIdAndReturnDateIsNull(1L)).thenReturn(List.of(borrow));
        when(borrowMapper.toResponse(any())).thenReturn(new BorrowResponse(1L, 2L, 1L, LocalDate.now(), null));

        List<BorrowResponse> responses = borrowService.getBorrowedBooksByMemberName("Alice");

        assertEquals(1, responses.size());
    }

    @Test
    void isBookCurrentlyBorrowed_shouldReturnTrue() {
        when(borrowRepository.existsByBookIdAndReturnDateIsNull(1L)).thenReturn(true);
        assertTrue(borrowService.isBookCurrentlyBorrowed(1L));
    }

    @Test
    void isMemberCurrentlyBorrowing_shouldReturnTrue() {
        when(borrowRepository.existsByMemberIdAndReturnDateIsNull(1L)).thenReturn(true);
        assertTrue(borrowService.isMemberCurrentlyBorrowing(1L));
    }

    @Test
    void getDistinctBorrowedBookTitles_shouldReturnDistinctList() {
        Book book1 = new Book("Title A", "Author A", 1);
        Book book2 = new Book("Title B", "Author B", 1);
        Borrow b1 = new Borrow(book1, null, LocalDate.now());
        Borrow b2 = new Borrow(book2, null, LocalDate.now());
        Borrow b3 = new Borrow(book1, null, LocalDate.now());

        when(borrowRepository.findByReturnDateIsNull()).thenReturn(List.of(b1, b2, b3));

        List<String> result = borrowService.getDistinctBorrowedBookTitles();

        assertEquals(2, result.size());
        assertTrue(result.contains("Title A"));
        assertTrue(result.contains("Title B"));
    }

    @Test
    void getBorrowedBookTitlesWithCount_shouldReturnMap() {
        Book book1 = new Book("Title A", "Author A", 1);
        Book book2 = new Book("Title B", "Author B", 1);
        Borrow b1 = new Borrow(book1, null, LocalDate.now());
        Borrow b2 = new Borrow(book2, null, LocalDate.now());
        Borrow b3 = new Borrow(book1, null, LocalDate.now());

        when(borrowRepository.findByReturnDateIsNull()).thenReturn(List.of(b1, b2, b3));

        Map<String, Long> result = borrowService.getBorrowedBookTitlesWithCount();

        assertEquals(2, result.size());
        assertEquals(2L, result.get("Title A"));
        assertEquals(1L, result.get("Title B"));
    }
}
