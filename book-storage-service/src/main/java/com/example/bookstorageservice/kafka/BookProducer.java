package com.example.bookstorageservice.kafka;

import com.example.basedomains.event.BookEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class BookProducer {

    private final NewTopic topic;

    private final KafkaTemplate<String, BookEvent> kafkaTemplate;

    public BookProducer(NewTopic topic, KafkaTemplate<String, BookEvent> kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(BookEvent event) {

        Message<BookEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, topic.name())
                .build();
        kafkaTemplate.send(message);

    }
}
