package com.library.library_management.dto.mapper;

import com.library.library_management.dto.BorrowResponse;
import com.library.library_management.entity.Borrow;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BorrowMapper {

    public BorrowResponse toResponse(Borrow borrow) {
        return new BorrowResponse(
                borrow.getId(),
                borrow.getBook().getId(),
                borrow.getMember().getId(),
                borrow.getBorrowDate(),
                borrow.getReturnDate()
        );
    }
}
