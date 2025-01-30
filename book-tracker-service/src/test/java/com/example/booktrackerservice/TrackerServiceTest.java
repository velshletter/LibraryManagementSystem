package com.example.booktrackerservice;

import com.example.booktrackerservice.dto.TrackerDto;
import com.example.booktrackerservice.entity.Tracker;
import com.example.booktrackerservice.exception.DuplicateDataException;
import com.example.booktrackerservice.exception.NotFoundException;
import com.example.booktrackerservice.mapper.TrackerMapper;
import com.example.booktrackerservice.repository.TrackerRepository;
import com.example.booktrackerservice.service.TrackerService;
import com.example.booktrackerservice.service.impl.TrackerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrackerServiceTest {

    @Mock
    private TrackerRepository repository;

    @InjectMocks
    private TrackerServiceImpl service;

    private Tracker tracker1;
    private Tracker tracker2;
    private TrackerDto trackerDto1;
    private TrackerDto trackerDto2;

    @BeforeEach
    void setUp() {
        tracker1 = new Tracker(1L, "AVAILABLE");
        tracker2 = new Tracker(2L, "BORROWED");

        trackerDto1 = TrackerMapper.mapToBookTrackerDto(tracker1);
        trackerDto2 = TrackerMapper.mapToBookTrackerDto(tracker2);
    }

    @Test
    void testFindAvailableBooks() {
        when(repository.findByStatus("AVAILABLE")).thenReturn(Arrays.asList(tracker1));

        List<TrackerDto> availableBooks = service.findAvailableBooks();

        assertEquals(1, availableBooks.size());
        assertEquals(trackerDto1, availableBooks.get(0));
    }

    @Test
    void testSave_Success() {
        when(repository.findByBookId(anyLong())).thenReturn(Optional.empty());
        when(repository.save(any(Tracker.class))).thenReturn(tracker1);

        assertDoesNotThrow(() -> service.save(1L));

        verify(repository, times(1)).save(any(Tracker.class));
    }

    @Test
    void testSave_DuplicateDataException() {
        when(repository.findByBookId(anyLong())).thenReturn(Optional.of(tracker1));

        DuplicateDataException exception = assertThrows(DuplicateDataException.class,
                () -> service.save(1L));

        assertEquals("Tracker with book id: 1 already exists", exception.getMessage());
    }

    @Test
    void testUpdate_Success() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(tracker1));
        when(repository.save(any(Tracker.class))).thenReturn(tracker1);

        TrackerDto updatedTrackerDto = service.update(1L, trackerDto1);

        assertNotNull(updatedTrackerDto);
        assertEquals(trackerDto1, updatedTrackerDto);

        verify(repository, times(1)).save(any(Tracker.class));
    }

    @Test
    void testUpdate_NotFoundException() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> service.update(0, trackerDto1));

        assertEquals("Tracker with id: 0 does not exists", exception.getMessage());
    }

    @Test
    void testDeleteByBookId_Success() {
        when(repository.findByBookId(anyLong())).thenReturn(Optional.of(tracker1));

        assertDoesNotThrow(() -> service.deleteByBookId(1L));

        verify(repository, times(1)).deleteByBookId(1L);
    }

    @Test
    void testDeleteByBookId_NotFoundException() {
        when(repository.findByBookId(anyLong())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> service.deleteByBookId(1L));

        assertEquals("Tracker with book id: 1 does not exists", exception.getMessage());
    }

    @Test
    void testDeleteById_Success() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(tracker1));

        assertDoesNotThrow(() -> service.deleteById(1L));

        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteById_NotFoundException() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> service.deleteById(1L));

        assertEquals("Tracker with id: 1 does not exists", exception.getMessage());

        verify(repository, times(1)).findById(1L);
        verify(repository, times(0)).deleteById(1L);
    }
}