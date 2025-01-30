package com.example.bookstorageservice.repository;

import com.example.bookstorageservice.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookStorageRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String ISBN);
}