package com.library.library_management.service.impl;

import com.library.library_management.dto.BookResponse;
import com.library.library_management.dto.CreateBookRequest;
import com.library.library_management.dto.UpdateBookRequest;
import com.library.library_management.entity.Book;
import com.library.library_management.exception.BookNotAvailableException;
import com.library.library_management.exception.BookNotFoundException;
import com.library.library_management.repository.BookRepository;
import com.library.library_management.repository.BorrowRepository;
import com.library.library_management.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BorrowRepository borrowRepository;
    
    @Override
    public BookResponse createBook(CreateBookRequest request) {
        Optional<Book> existingBook = bookRepository.findByTitleAndAuthor(request.title(), request.author());

        if (existingBook.isPresent()) {
            Book presentBook = existingBook.get();
            presentBook.setAmount(presentBook.getAmount() + 1);
            Book savedBook = bookRepository.save(presentBook);
            return new BookResponse(
                    savedBook.getId(),
                    savedBook.getTitle(),
                    savedBook.getAuthor(),
                    savedBook.getAmount()
            );
        }
        Book book = new Book();
        book.setTitle(request.title());
        book.setAuthor(request.author());
        book.setAmount(1);
        Book savedBook = bookRepository.save(book);
        return new BookResponse(savedBook.getId(), savedBook.getTitle(), savedBook.getAuthor(), savedBook.getAmount());
    }

    @Override
    public BookResponse getBookById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException("Book not found"));
        return new BookResponse(book.getId(), book.getTitle(), book.getAuthor(), book.getAmount());
    }

    @Override
    public List<BookResponse> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(book -> new BookResponse(book.getId(), book.getTitle(), book.getAuthor(), book.getAmount()))
                .collect(Collectors.toList());
    }

    @Override
    public BookResponse updateBookById(Long id, UpdateBookRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found"));
        book.setTitle(request.title());
        book.setAuthor(request.author());
        book.setAmount(request.amount());
        Book savedBook = bookRepository.save(book);
        return new BookResponse(savedBook.getId(), savedBook.getTitle(), savedBook.getAuthor(), savedBook.getAmount());
    }

    @Override
    public void deleteBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found"));

        if (borrowRepository.existsByBookIdAndReturnDateIsNull(id)) {
            throw new BookNotAvailableException("Book is currently borrowed and cannot be deleted");
        }

        bookRepository.delete(book);
    }

    @Override
    public boolean existsById(Long id) {
        return bookRepository.existsById(id);
    }

    @Override
    public int getAmountById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found")).getAmount();
    }
}
