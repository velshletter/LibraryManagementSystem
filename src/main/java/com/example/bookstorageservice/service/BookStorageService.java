package com.example.bookstorageservice.service;

import com.example.bookstorageservice.dto.BookDto;

import java.util.List;

public interface BookStorageService {

    List<BookDto> findAll();

    BookDto findById(long id);

    BookDto findByISBN(String ISBN);

    BookDto save(BookDto bookDto);

    void update(long id, BookDto updatedBook);

    void delete(long id);
}
