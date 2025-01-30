package com.example.booktrackerservice.controller;


import com.example.booktrackerservice.dto.TrackerDto;
import com.example.booktrackerservice.service.TrackerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trackers")
public class TrackerController {

    private final TrackerService trackerService;

    public TrackerController(TrackerService trackerService) {
        this.trackerService = trackerService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<TrackerDto>> getAllTrackers() {
        List<TrackerDto> trackers = trackerService.findAvailableBooks();
        return new ResponseEntity<>(trackers, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrackerDto> updateTrackerStatus(@PathVariable long id, @RequestBody TrackerDto trackerDto) {
        TrackerDto updatedTracker = trackerService.update(id, trackerDto);
        return new ResponseEntity<>(updatedTracker, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTracker(@PathVariable long id) {
        trackerService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
