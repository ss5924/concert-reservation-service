package me.songha.concert.reservation.pending;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ReservationRequestStatusService {
    private final RedisTemplate<String, String> redisTemplate;

    public void saveStatus(String requestId, String status) {
        redisTemplate.opsForValue().set("status:" + requestId, status, 10, TimeUnit.MINUTES);
    }

    public String getStatus(String requestId) {
        return redisTemplate.opsForValue().get("status:" + requestId);
    }

    public String getReservationIdByRequestId(String requestId) {
        String key = "reservation-processing-requestId:" + requestId;
        return redisTemplate.opsForValue().get(key);
    }
}