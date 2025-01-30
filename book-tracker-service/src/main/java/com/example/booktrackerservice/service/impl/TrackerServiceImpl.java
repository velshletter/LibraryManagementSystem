package com.example.booktrackerservice.service.impl;

import com.example.booktrackerservice.dto.TrackerDto;
import com.example.booktrackerservice.entity.Tracker;
import com.example.booktrackerservice.exception.DuplicateDataException;
import com.example.booktrackerservice.exception.NotFoundException;
import com.example.booktrackerservice.mapper.TrackerMapper;
import com.example.booktrackerservice.repository.TrackerRepository;
import com.example.booktrackerservice.service.TrackerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TrackerServiceImpl implements TrackerService {

    private final TrackerRepository repository;

    public TrackerServiceImpl(TrackerRepository repository) {
        this.repository = repository;
    }

    public List<TrackerDto> findAvailableBooks() {
        List<Tracker> books = repository.findByStatus("AVAILABLE");
        return books.stream().map(TrackerMapper::mapToBookTrackerDto).collect(Collectors.toList());
    }

    @Transactional
    public void save(long bookId) {
        if (repository.findByBookId(bookId).isPresent()) {
            throw new DuplicateDataException("Tracker with Book ID:" + bookId + " already exists");
        }
        Tracker tracker = new Tracker(bookId,"AVAILABLE");
        TrackerMapper.mapToBookTrackerDto(repository.save(tracker));
    }

    @Transactional
    public void updateTrackerStatus(long bookId, String status) {
        int updatedRows = repository.updateStatus(bookId, status);
        if (updatedRows == 0) {
            throw new NotFoundException("Tracker with book id:" + bookId + "does not exists");
        }
    }

    @Transactional
    public void deleteByBookId(long id) {
        repository.findByBookId(id)
                .orElseThrow(() -> new NotFoundException("Tracker with book id:" + id + "does not exists"));
        repository.deleteByBookId(id);
    }

    @Transactional
    public void deleteById(long id) {
        repository.findByBookId(id)
                .orElseThrow(() -> new NotFoundException("Tracker with id:" + id + "does not exists"));
        repository.deleteById(id);
    }
}
