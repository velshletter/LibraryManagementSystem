package com.example.bookstorageservice.exception;

public class DuplicateDataException extends RuntimeException {
    public DuplicateDataException(String msg) {
        super(msg);
    }
}
