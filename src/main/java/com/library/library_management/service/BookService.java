package com.library.library_management.service;

import com.library.library_management.dto.BookResponse;
import com.library.library_management.dto.CreateBookRequest;
import com.library.library_management.dto.UpdateBookRequest;
import com.library.library_management.entity.Book;

import java.util.List;

public interface BookService {
    BookResponse createBook(CreateBookRequest request);

    BookResponse getBookById(Long id);

    List<BookResponse> getAllBooks();

    BookResponse updateBookById(Long id, UpdateBookRequest request);

    void deleteBookById(Long id);

    boolean existsById(Long id);

    int getAmountById(Long id);
}
