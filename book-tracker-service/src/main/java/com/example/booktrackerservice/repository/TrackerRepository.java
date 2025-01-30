package com.example.booktrackerservice.repository;

import com.example.booktrackerservice.entity.Tracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrackerRepository extends JpaRepository<Tracker, Long> {

    List<Tracker> findByStatus(String status);

    Optional<Tracker> findByBookId(Long bookId);

    void deleteByBookId(Long bookId);

}
