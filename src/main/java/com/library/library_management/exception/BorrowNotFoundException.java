package com.library.library_management.exception;

public class BorrowNotFoundException extends RuntimeException {
    public BorrowNotFoundException(String message) {
        super(message);
    }
}
