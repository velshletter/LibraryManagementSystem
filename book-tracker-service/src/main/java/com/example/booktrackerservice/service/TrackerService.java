package com.example.booktrackerservice.service;

import com.example.booktrackerservice.dto.TrackerDto;

import java.util.List;

public interface TrackerService {

    void save(long bookId);

    List<TrackerDto> findAvailableBooks();

    void updateTrackerStatus(long id, String status);

    void deleteByBookId(long id);

    void deleteById(long id);
}
