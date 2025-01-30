package com.example.booktrackerservice.dto;

import java.time.LocalDateTime;


public record TrackerDto(
    Long id,

    Long bookId,

    String status,

    LocalDateTime takenAt,

    LocalDateTime returnBy
){
}
