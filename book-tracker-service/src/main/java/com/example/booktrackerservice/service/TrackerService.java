package com.example.booktrackerservice.service;

import com.example.booktrackerservice.dto.TrackerDto;

import java.util.List;

public interface TrackerService {

    void save(long bookId);

    List<TrackerDto> findAvailableBooks();

    TrackerDto update(long id, TrackerDto trackerDto);

    void deleteByBookId(long id);

    void deleteById(long id);
}
