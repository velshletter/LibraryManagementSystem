package com.example.bookstorageservice.controller;

import com.example.bookstorageservice.dto.BookDto;
import com.example.bookstorageservice.service.BookStorageService;
import com.example.bookstorageservice.service.impl.BookStorageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookStorageController {

    private final BookStorageService bookStorageService;

    @Autowired
    public BookStorageController(BookStorageServiceImpl bookStorageService) {
        this.bookStorageService = bookStorageService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<BookDto>> getAllBooks() {
        List<BookDto> books = bookStorageService.findAll();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable long id) {
        BookDto foundBook = bookStorageService.findById(id);
        return ResponseEntity.ok(foundBook);
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookDto> getBookByIsbn(@PathVariable String isbn) {
        BookDto foundBook = bookStorageService.findByISBN(isbn);
        return ResponseEntity.ok(foundBook);
    }

    @PostMapping("/add")
    public ResponseEntity<BookDto> addBook(@RequestBody BookDto bookDto) {
        return new ResponseEntity<>(bookStorageService.save(bookDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDto> updateBook(@PathVariable long id, @RequestBody BookDto updatedBook) {
        bookStorageService.update(id, updatedBook);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable long id) {
        bookStorageService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
