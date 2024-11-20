package me.songha.concert.reservation.pending;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.songha.concert.reservation.general.ReservationDto;
import me.songha.concert.reservation.general.ReservationRepositoryService;
import me.songha.concert.reservation.general.ReservationStatus;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationPendingConsumer {
    private final ReservationRequestStatusService reservationRequestStatusService;
    private final ReservationRepositoryService reservationRepositoryService;
    private final StringRedisTemplate redisTemplate;

    @KafkaListener(topics = "reservation-topic", groupId = "reservation-group")
    public void processReservation(ReservationPendingRequest request) {
        try {
            reservationRequestStatusService.saveStatus(request.getRequestId(), ReservationStatus.PROCESSING.toString());

            ReservationDto reservationDto = ReservationDto.builder()
                    .userId(request.getUserId())
                    .concertId(request.getConcertId())
                    .reservationStatus(ReservationStatus.PROCESSING.toString())
                    .build();

            Long reservationId = reservationRepositoryService.createReservation(reservationDto);

            String key = String.format("reservation-processing-requestId:%s", request.getRequestId());
            redisTemplate.opsForValue().setIfAbsent(key, String.valueOf(reservationId), 10, TimeUnit.MINUTES);

        } catch (Exception e) {
            reservationRequestStatusService.saveStatus(request.getRequestId(), ReservationStatus.REJECTED.toString());
            log.error("[Error] Failed to process reservation. e.getMessage():{}", e.getMessage(), e);
        }
    }
}