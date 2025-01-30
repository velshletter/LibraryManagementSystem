package com.example.bookstorageservice;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import com.example.bookstorageservice.dto.BookDto;
import com.example.bookstorageservice.entity.Book;
import com.example.bookstorageservice.exception.DuplicateDataException;
import com.example.bookstorageservice.exception.NotFoundException;
import com.example.bookstorageservice.mapper.BookMapper;
import com.example.bookstorageservice.repository.BookStorageRepository;
import com.example.bookstorageservice.service.impl.BookStorageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class BookStorageServiceTest {

    @Mock
    private BookStorageRepository repository;

    @InjectMocks
    private BookStorageServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll_ShouldReturnListOfBookDtos_WhenBooksExist() {

        List<Book> books = Arrays.asList(
                new Book(1L, "123-456-789", "Test Book 1", "Fiction", "Description 1", "Author 1"),
                new Book(2L, "987-654-321", "Test Book 2", "Non-Fiction", "Description 2", "Author 2")
        );
        when(repository.findAll()).thenReturn(books);

        List<BookDto> result = service.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).isbn()).isEqualTo("123-456-789");
        verify(repository, times(1)).findAll();
    }

    @Test
    void testFindById_ShouldReturnBookDto_WhenBookExists() {

        Book book = new Book(1L, "123-456-789", "Test Book", "Fiction", "Description", "Author");
        when(repository.findById(1L)).thenReturn(Optional.of(book));

        BookDto result = service.findById(1L);

        assertThat(result.isbn()).isEqualTo("123-456-789");
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void testFindById_ShouldThrowNotFoundException_WhenBookDoesNotExist() {

        when(repository.findById(100L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Book with id:1does not exists");
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void testFindByISBN_ShouldReturnBookDto_WhenBookExists() {

        Book book = new Book(1L, "123-456-789", "Test Book", "Fiction", "Description", "Author");
        when(repository.findByIsbn("123-456-789")).thenReturn(Optional.of(book));

        BookDto result = service.findByISBN("123-456-789");

        assertThat(result.title()).isEqualTo("Test Book");
        verify(repository, times(1)).findByIsbn("123-456-789");
    }

    @Test
    void testFindByISBN_ShouldThrowNotFoundException_WhenBookDoesNotExist() {

        when(repository.findByIsbn("123-456-789")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findByISBN("123-456-789"))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Book with ISBN:123-456-789 does not exists");
        verify(repository, times(1)).findByIsbn("123-456-789");
    }

    @Test
    void testSave_ShouldPersistBook_WhenBookIsValid() {

        BookDto bookDto = new BookDto(null, "123-456-789", "Test Book", "Fiction", "Description", "Author");
        Book book = BookMapper.mapToBook(bookDto);
        when(repository.findByIsbn("123-456-789")).thenReturn(Optional.empty());
        when(repository.save(any(Book.class))).thenReturn(book);

        BookDto result = service.save(bookDto);

        assertThat(result.isbn()).isEqualTo("123-456-789");
        verify(repository, times(1)).findByIsbn("123-456-789");
        verify(repository, times(1)).save(any(Book.class));
    }

    @Test
    void testSave_ShouldThrowDuplicateDataException_WhenBookWithSameISBNExists() {

        BookDto bookDto = new BookDto(null, "123-456-789", "Test Book", "Fiction", "Description", "Author");
        when(repository.findByIsbn("123-456-789")).thenReturn(Optional.of(new Book()));

        assertThatThrownBy(() -> service.save(bookDto))
                .isInstanceOf(DuplicateDataException.class)
                .hasMessage("Book with ISBN:123-456-789 already exists");
        verify(repository, times(1)).findByIsbn("123-456-789");
        verify(repository, never()).save(any(Book.class));
    }

    @Test
    void testUpdate_ShouldModifyBook_WhenBookExists() {

        BookDto bookDto = new BookDto(null, "123-456-789", "Updated Book", "Fiction", "Updated Description", "Updated Author");
        Book book = BookMapper.mapToBook(bookDto);
        book.setId(1L);
        when(repository.save(any(Book.class))).thenReturn(book);

        service.update(1L, bookDto);

        verify(repository, times(1)).save(any(Book.class));
    }

    @Test
    void testDelete_ShouldRemoveBook_WhenBookExists() {

        Book book = new Book(1L, "123-456-789", "Test Book", "Fiction", "Description", "Author");
        when(repository.findById(1L)).thenReturn(Optional.of(book));

        service.delete(1L);

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void testDelete_ShouldThrowNotFoundException_WhenBookDoesNotExist() {

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Book with id:1does not exists");
        verify(repository, times(1)).findById(1L);
        verify(repository, never()).deleteById(1L);
    }
}
