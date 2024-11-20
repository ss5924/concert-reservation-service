package me.songha.concert.reservation.pending;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationPendingProducer {
    private final KafkaTemplate<String, ReservationPendingProducerRequest> kafkaTemplate;
    private static final String TOPIC_NAME = "reservation-topic";

    public void sendToQueue(String requestId, Long userId, Long concertId) {
        ReservationPendingProducerRequest request =
                new ReservationPendingProducerRequest(concertId, userId, requestId);
        kafkaTemplate.send(TOPIC_NAME, requestId, request);
        log.info("Sent reservation to Kafka. Request ID: {}", requestId);
    }
}