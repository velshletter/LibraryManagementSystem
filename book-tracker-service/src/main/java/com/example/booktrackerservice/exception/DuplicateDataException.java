package com.example.booktrackerservice.exception;

public class DuplicateDataException extends RuntimeException {
    public DuplicateDataException(String msg) {
        super(msg);
    }
}
