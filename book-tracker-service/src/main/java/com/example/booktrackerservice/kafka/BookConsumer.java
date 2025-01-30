package com.example.booktrackerservice.kafka;

import com.example.basedomains.event.BookEvent;
import com.example.booktrackerservice.service.TrackerService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class BookConsumer {

    private final TrackerService trackerService;

    public BookConsumer(TrackerService trackerService) {
        this.trackerService = trackerService;
    }

    @KafkaListener(
            topics = "${spring.kafka.topic.name}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(BookEvent event) {
        if (event.getType().equals("SAVE")) {
            trackerService.save(event.getBookId());
        } else if (event.getType().equals("DELETE")) {
            trackerService.deleteByBookId(event.getBookId());
            System.out.println("successfully deleted book");
        }
    }


}
