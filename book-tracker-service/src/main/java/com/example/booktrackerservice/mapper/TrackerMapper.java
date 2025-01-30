package com.example.booktrackerservice.mapper;

import com.example.booktrackerservice.dto.TrackerDto;
import com.example.booktrackerservice.entity.Tracker;

public class TrackerMapper {

    public static Tracker mapToBookTracker(TrackerDto trackerDto) {
        return new Tracker(
                trackerDto.id(),
                trackerDto.bookId(),
                trackerDto.status(),
                trackerDto.takenAt(),
                trackerDto.returnBy()
        );
    }

    public static TrackerDto mapToBookTrackerDto(Tracker tracker) {
        return new TrackerDto(
                tracker.getId(),
                tracker.getBookId(),
                tracker.getStatus(),
                tracker.getTakenAt(),
                tracker.getReturnBy()
        );
    }
}
