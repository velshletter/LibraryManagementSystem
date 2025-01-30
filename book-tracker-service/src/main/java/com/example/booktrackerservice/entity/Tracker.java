package com.example.booktrackerservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "trackers")
public class Tracker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tracker_id")
    private Long id;

    @Column(name = "book_id", unique = true)
    private Long bookId;

    private String status;

    @Column(name = "taken_at")
    private LocalDateTime takenAt;

    @Column(name = "return_by")
    private LocalDateTime returnBy;

    public Tracker(Long bookId, String status) {
        this.bookId = bookId;
        this.status = status;
    }
}