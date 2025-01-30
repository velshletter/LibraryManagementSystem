package com.example.bookstorageservice;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.bookstorageservice.entity.Book;

import com.example.bookstorageservice.repository.BookStorageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookStorageRepositoryTest {

    @Autowired
    private BookStorageRepository bookStorageRepository;

    @Test
    void test_SaveBook_ShouldPersistBook_WhenValidBookIsProvided() {

        Book book = new Book(null, "123-456-789", "Test Book", "Fiction", "A test book.", "John Doe");

        Book savedBook = bookStorageRepository.save(book);

        assertThat(savedBook).isNotNull();
        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getIsbn()).isEqualTo("123-456-789");
    }

    @Test
    void test_FindByIsbn_ShouldReturnBook_WhenBookExists() {

        Book book = new Book(null, "123-456-789", "Test Book", "Fiction", "A test book.", "John Doe");
        bookStorageRepository.save(book);

        Optional<Book> foundBook = bookStorageRepository.findByIsbn("123-456-789");

        assertThat(foundBook).isPresent();
        assertThat(foundBook.get().getTitle()).isEqualTo("Test Book");
    }

    @Test
    void test_FindByIsbn_ShouldReturnEmptyOptional_WhenBookDoesNotExist() {

        Optional<Book> foundBook = bookStorageRepository.findByIsbn("non-existent-isbn");

        assertThat(foundBook).isNotPresent();
    }

    @Test
    void test_FindById_ShouldReturnBook_WhenBookExists() {

        Book book = new Book(null, "123-456-789", "Test Book", "Fiction", "A test book.", "John Doe");
        Book savedBook = bookStorageRepository.save(book);

        Optional<Book> foundBook = bookStorageRepository.findById(savedBook.getId());

        assertThat(foundBook).isPresent();
        assertThat(foundBook.get().getAuthor()).isEqualTo("John Doe");
    }

    @Test
    void test_FindById_ShouldReturnEmptyOptional_WhenBookDoesNotExist() {

        Optional<Book> foundBook = bookStorageRepository.findById(999L);

        assertThat(foundBook).isNotPresent();
    }

    @Test
    void test_UpdateBook_ShouldModifyBookDetails_WhenValidBookIsProvided() {

        Book book = new Book(null, "123-456-789", "Test Book", "Fiction", "A test book.", "John Doe");
        Book savedBook = bookStorageRepository.save(book);

        savedBook.setTitle("Updated Test Book");
        Book updatedBook = bookStorageRepository.save(savedBook);

        assertThat(updatedBook.getTitle()).isEqualTo("Updated Test Book");
    }

    @Test
    void test_DeleteBook_ShouldRemoveBook_WhenBookExists() {

        Book book = new Book(null, "123-456-789", "Test Book", "Fiction", "A test book.", "John Doe");
        Book savedBook = bookStorageRepository.save(book);

        bookStorageRepository.delete(savedBook);

        Optional<Book> deletedBook = bookStorageRepository.findById(savedBook.getId());
        assertThat(deletedBook).isNotPresent();
    }

    @Test
    void test_DeleteBook_ShouldDoNothing_WhenBookDoesNotExist() {

        bookStorageRepository.deleteById(999L);

        assertThat(bookStorageRepository.findById(999L)).isNotPresent();
    }
}
