package com.example.bookstorageservice.mapper;

import com.example.bookstorageservice.dto.BookDto;
import com.example.bookstorageservice.entity.Book;

public class BookMapper {

    public static Book mapToBook(BookDto bookDto){

        return new Book(
                bookDto.id(),
                bookDto.isbn(),
                bookDto.title(),
                bookDto.genre(),
                bookDto.description(),
                bookDto.author()
        );
    }

    public static BookDto mapToBookDto(Book book){

        return new BookDto(
                book.getId(),
                book.getIsbn(),
                book.getTitle(),
                book.getGenre(),
                book.getDescription(),
                book.getAuthor()
        );
    }
}
