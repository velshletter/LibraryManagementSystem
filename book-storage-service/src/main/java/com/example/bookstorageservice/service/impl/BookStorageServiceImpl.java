package com.example.bookstorageservice.service.impl;

import com.example.basedomains.event.BookEvent;
import com.example.bookstorageservice.dto.BookDto;
import com.example.bookstorageservice.entity.Book;
import com.example.bookstorageservice.exception.DuplicateDataException;
import com.example.bookstorageservice.exception.NotFoundException;
import com.example.bookstorageservice.kafka.BookProducer;
import com.example.bookstorageservice.mapper.BookMapper;
import com.example.bookstorageservice.repository.BookStorageRepository;
import com.example.bookstorageservice.service.BookStorageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class BookStorageServiceImpl implements BookStorageService {

    private final BookStorageRepository repository;
    private final BookProducer bookProducer;

    public BookStorageServiceImpl(BookStorageRepository repository, BookProducer bookProducer) {
        this.repository = repository;
        this.bookProducer = bookProducer;
    }

    public List<BookDto> findAll() {
        List<Book> books = repository.findAll();
        return books.stream().map(BookMapper::mapToBookDto).collect(Collectors.toList());
    }

    public BookDto findById(long id) {
        Book foundBook = repository.findById(id).orElseThrow(() -> new NotFoundException("Book with id:" + id + "does not exists"));
        return BookMapper.mapToBookDto(foundBook);
    }

    public BookDto findByISBN(String isbn) {
        Book foundBook = repository.findByIsbn(isbn)
                .orElseThrow(() -> new NotFoundException("Book with ISBN:" + isbn + " does not exists"));
        return BookMapper.mapToBookDto(foundBook);
    }

    @Transactional
    public BookDto save(BookDto bookDto) {

        if(repository.findByIsbn(bookDto.isbn()).isPresent()){
            throw new DuplicateDataException("Book with ISBN:" + bookDto.isbn() + " already exists");
        }
        Book book = BookMapper.mapToBook(bookDto);
        book.setId(null);
        Book savedBook = repository.save(book);

        BookEvent bookEvent = new BookEvent();
        bookEvent.setType("SAVE");
        bookEvent.setBookId(savedBook.getId());
        bookProducer.sendMessage(bookEvent);

        return BookMapper.mapToBookDto(savedBook);

    }

    @Transactional
    public void update(long id, BookDto updatedBook) {
        Book book = BookMapper.mapToBook(updatedBook);
        book.setId(id);
        repository.save(book);
    }

    @Transactional
    public void delete(long id) {
        repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book with id:" + id + "does not exists"));

        repository.deleteById(id);
        BookEvent bookEvent = new BookEvent();
        bookEvent.setType("DELETE");
        bookEvent.setBookId(id);
        bookProducer.sendMessage(bookEvent);
    }
}
