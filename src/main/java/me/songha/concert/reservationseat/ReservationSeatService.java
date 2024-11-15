package me.songha.concert.reservationseat;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class ReservationSeatService {
    private final StringRedisTemplate redisTemplate;
    private final ReservationSeatRepository reservationSeatRepository;

    public boolean reserveSeat(Long concertId, Long seatId, String userId) {
        String key = String.format("concert:%d:seat:%d", concertId, seatId);
        Boolean isReserved = redisTemplate.opsForValue().setIfAbsent(key, userId, 10, TimeUnit.MINUTES);

        return Objects.equals(isReserved, true);
    }

    public List<Long> getSoldSeats(Long concertId) {
        return reservationSeatRepository.findIdsByConcertId(concertId);
    }

    public Set<String> getReservedSeats(Long concertId) {
        String pattern = String.format("concert:%d:seat:*", concertId);
        return redisTemplate.keys(pattern);
    }

    public boolean isSeatAvailable(Long concertId, Long seatId) {
        List<Long> soldSeats = getSoldSeats(concertId);
        if (soldSeats.contains(seatId)) {
            return false;
        }
        Set<String> reservedSeats = getReservedSeats(concertId);
        String key = String.format("concert:%d:seat:%d", concertId, seatId);
        if (reservedSeats.contains(key)) {
            return false;
        }
        return true;
    }
}
