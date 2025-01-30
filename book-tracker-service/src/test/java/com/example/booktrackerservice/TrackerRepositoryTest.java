package com.example.booktrackerservice;

import com.example.booktrackerservice.entity.Tracker;
import com.example.booktrackerservice.repository.TrackerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TrackerRepositoryTest {

    @Autowired
    private TrackerRepository repository;

    private Tracker tracker1;
    private Tracker tracker2;

    @BeforeEach
    void setUp() {
        tracker1 = new Tracker(1L, "AVAILABLE");
        tracker2 = new Tracker(2L, "BORROWED");

        repository.save(tracker1);
        repository.save(tracker2);
    }

    @Test
    void testFindByStatus() {
        List<Tracker> availableTrackers = repository.findByStatus("AVAILABLE");
        assertEquals(1, availableTrackers.size());
        assertEquals(tracker1, availableTrackers.get(0));

        List<Tracker> borrowedTrackers = repository.findByStatus("BORROWED");
        assertEquals(1, borrowedTrackers.size());
        assertEquals(tracker2, borrowedTrackers.get(0));
    }

    @Test
    void testFindByBookId_Exists() {
        Optional<Tracker> optionalTracker = repository.findByBookId(1L);
        assertTrue(optionalTracker.isPresent());
        assertEquals(tracker1, optionalTracker.get());
    }

    @Test
    void testFindByBookId_NotExists() {
        Optional<Tracker> optionalTracker = repository.findByBookId(3L);
        assertFalse(optionalTracker.isPresent());
    }

    @Test
    void testDeleteByBookId_Exists() {
        repository.deleteByBookId(1L);

        Optional<Tracker> optionalTracker = repository.findByBookId(1L);
        assertFalse(optionalTracker.isPresent());

        List<Tracker> allTrackers = repository.findAll();
        assertEquals(1, allTrackers.size());
        assertEquals(tracker2, allTrackers.get(0));
    }

    @Test
    void testDeleteByBookId_NotExists() {
        repository.deleteByBookId(3L);

        List<Tracker> allTrackers = repository.findAll();
        assertEquals(2, allTrackers.size());
        assertTrue(allTrackers.contains(tracker1));
        assertTrue(allTrackers.contains(tracker2));
    }
}