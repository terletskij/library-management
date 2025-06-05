package com.library.library_management.dto.mapper;

import com.library.library_management.dto.BookResponse;
import com.library.library_management.dto.CreateBookRequest;
import com.library.library_management.dto.UpdateBookRequest;
import com.library.library_management.entity.Book;
import org.hibernate.sql.Update;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    @Autowired
    private ModelMapper modelMapper;

    public Book fromCreateRequest(CreateBookRequest request) {
        return modelMapper.map(request, Book.class);
    }

    public Book fromUpdateRequest(UpdateBookRequest request) {
        return modelMapper.map(request, Book.class);
    }

    public UpdateBookRequest toUpdateRequest(Book book) {
        return modelMapper.map(book, UpdateBookRequest.class);
    }

    public Book fromBookResponse(BookResponse response) {
        return modelMapper.map(response, Book.class);
    }

    public BookResponse toBookResponse(Book book) {
        return modelMapper.map(book, BookResponse.class);
    }
}
