package com.library.library_management.service;

import com.library.library_management.dto.BookResponse;
import com.library.library_management.dto.CreateBookRequest;
import com.library.library_management.dto.UpdateBookRequest;
import com.library.library_management.entity.Book;
import com.library.library_management.exception.BookNotAvailableException;
import com.library.library_management.exception.BookNotFoundException;
import com.library.library_management.repository.BookRepository;
import com.library.library_management.repository.BorrowRepository;
import com.library.library_management.service.impl.BookServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceTests {

    @InjectMocks
    private BookServiceImpl bookService;


    @Mock
    private BookRepository bookRepository;

    @Mock
    private BorrowRepository borrowRepository;

    @Test
    void createBook_shouldIncreaseAmountIfBookExists() {
        // given
        CreateBookRequest request = new CreateBookRequest("Clean Code", "Robert Martin");

        Book existingBook = new Book();
        existingBook.setId(1L);
        existingBook.setTitle("Clean Code");
        existingBook.setAuthor("Robert Martin");
        existingBook.setAmount(2);

        when(bookRepository.findByTitleAndAuthor("Clean Code", "Robert Martin"))
                .thenReturn(Optional.of(existingBook));

        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        BookResponse response = bookService.createBook(request);

        // then
        assertEquals("Clean Code", response.title());
        assertEquals(3, response.amount()); // incremented
        verify(bookRepository).save(existingBook);
    }

    @Test
    void createBook_shouldCreateNewIfNotExists() {
        CreateBookRequest request = new CreateBookRequest("Domain Driven Design", "Eric Evans");

        when(bookRepository.findByTitleAndAuthor("Domain Driven Design", "Eric Evans"))
                .thenReturn(Optional.empty());

        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> {
            Book book = invocation.getArgument(0);
            book.setId(10L);
            return book;
        });

        BookResponse response = bookService.createBook(request);

        assertEquals("Domain Driven Design", response.title());
        assertEquals(1, response.amount());
        assertNotNull(response.id());
    }

    @Test
    void createBook_shouldCreateNewBookIfNotExists() {
        CreateBookRequest request = new CreateBookRequest("Domain Driven Design", "Eric Evans");

        when(bookRepository.findByTitleAndAuthor("Domain Driven Design", "Eric Evans"))
                .thenReturn(Optional.empty());

        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> {
            Book book = invocation.getArgument(0);
            book.setId(10L);
            return book;
        });

        BookResponse response = bookService.createBook(request);

        assertEquals("Domain Driven Design", response.title());
        assertEquals(1, response.amount());
        assertEquals(10L, response.id());
    }


    @Test
    void getBookById_shouldReturnBookIfExists() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Clean Code");
        book.setAuthor("Robert Martin");
        book.setAmount(5);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        BookResponse response = bookService.getBookById(1L);

        assertEquals("Clean Code", response.title());
        assertEquals(5, response.amount());
    }
    @Test
    void getBookById_shouldThrowExceptionIfNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.getBookById(1L));
    }

    @Test
    void getAllBooks_shouldReturnList() {
        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Book One");
        book1.setAuthor("Author A");
        book1.setAmount(2);

        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Book Two");
        book2.setAuthor("Author B");
        book2.setAmount(3);

        when(bookRepository.findAll()).thenReturn(List.of(book1, book2));

        List<BookResponse> books = bookService.getAllBooks();

        assertEquals(2, books.size());
        assertEquals("Book One", books.get(0).title());
        assertEquals("Book Two", books.get(1).title());
    }

    @Test
    void updateBookById_shouldUpdateIfExists() {
        UpdateBookRequest request = new UpdateBookRequest("Updated Title", "Updated Author", 10);

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Old Title");
        book.setAuthor("Old Author");
        book.setAmount(2);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BookResponse updated = bookService.updateBookById(1L, request);

        assertEquals("Updated Title", updated.title());
        assertEquals("Updated Author", updated.author());
        assertEquals(10, updated.amount());
    }

    @Test
    void updateBookById_shouldThrowExceptionIfNotFound() {
        UpdateBookRequest request = new UpdateBookRequest("Title", "Author", 1);
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.updateBookById(1L, request));
    }

    @Test
    void deleteBookById_shouldDeleteIfNotBorrowed() {
        Book book = new Book();
        book.setId(1L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(borrowRepository.existsByBookIdAndReturnDateIsNull(1L)).thenReturn(false);

        bookService.deleteBookById(1L);

        verify(bookRepository).delete(book);
    }

    @Test
    void deleteBookById_shouldThrowExceptionIfBorrowed() {
        Book book = new Book();
        book.setId(1L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(borrowRepository.existsByBookIdAndReturnDateIsNull(1L)).thenReturn(true);

        assertThrows(BookNotAvailableException.class, () -> bookService.deleteBookById(1L));
    }

    @Test
    void existsById_shouldReturnTrueIfExists() {
        when(bookRepository.existsById(1L)).thenReturn(true);

        assertTrue(bookService.existsById(1L));
    }

    @Test
    void existsById_shouldReturnFalseIfNotExists() {
        when(bookRepository.existsById(1L)).thenReturn(false);

        assertFalse(bookService.existsById(1L));
    }

    @Test
    void getAmountById_shouldReturnAmountIfBookExists() {
        Book book = new Book();
        book.setId(1L);
        book.setAmount(5);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        assertEquals(5, bookService.getAmountById(1L));
    }

    @Test
    void getAmountById_shouldThrowExceptionIfNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.getAmountById(1L));
    }
}