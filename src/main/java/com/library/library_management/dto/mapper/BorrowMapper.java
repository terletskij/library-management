package com.library.library_management.dto.mapper;

import com.library.library_management.dto.BorrowResponse;
import com.library.library_management.entity.Borrow;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BorrowMapper {

    @Autowired
    private ModelMapper modelMapper;

    public Borrow fromResponse(BorrowResponse response) {
        return modelMapper.map(response, Borrow.class);
    }

    public BorrowResponse toResponse(Borrow borrow) {
        return modelMapper.map(borrow, BorrowResponse.class);
    }
}
