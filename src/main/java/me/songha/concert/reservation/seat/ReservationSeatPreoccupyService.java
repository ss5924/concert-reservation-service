package me.songha.concert.reservation.seat;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Transactional
@RequiredArgsConstructor
@Service
public class ReservationSeatPreoccupyService {
    private final StringRedisTemplate redisTemplate;
    private final ReservationSeatRepository reservationSeatRepository;

    /**
     * key: concert:%d:seat:%s | value: userId
     */
    public boolean preoccupySeats(Long concertId, String seatNumber, Long userId) {
        String key = String.format("concert:%d:seat:%s", concertId, seatNumber);
        Boolean isReserved = redisTemplate.opsForValue().setIfAbsent(key, String.valueOf(userId), 10, TimeUnit.MINUTES);

        return Objects.equals(isReserved, true);
    }

    public List<String> getSoldSeatsByConcert(Long concertId) {
        return reservationSeatRepository.findSeatNumbersByConcertId(concertId);
    }

    public Set<String> getPreoccupiedSeatsByConcert(Long concertId) {
        String pattern = String.format("concert:%d:seat:*", concertId);
        return redisTemplate.keys(pattern);
    }

    public String getUserIdByConcertAndSeatNumber(Long concertId, String seatNumber) {
        String seatKey = String.format("concert:%d:seat:%s", concertId, seatNumber);
        return redisTemplate.opsForValue().get(seatKey);
    }

    public boolean isSeatAvailable(Long concertId, String seatNumber) {
        String seatKey = String.format("concert:%d:seat:%s", concertId, seatNumber);
        List<String> soldSeats = getSoldSeatsByConcert(concertId);
        Set<String> preoccupiedSeats = getPreoccupiedSeatsByConcert(concertId);

        boolean isSold = soldSeats.contains(seatNumber);
        boolean isReserved = preoccupiedSeats.contains(seatKey);

        return !(isSold || isReserved);
    }

    public void isValidatedSeatForCurrentUser(Long concertId, Long userId, List<String> seatNumbers) {
        for (String seatNumber : seatNumbers) {
            boolean isValidatedSeatForCurrentUser = String.valueOf(userId).equals(getUserIdByConcertAndSeatNumber(concertId, seatNumber));
            if (!isValidatedSeatForCurrentUser) {
                throw new IllegalArgumentException("isNotValidatedSeatForCurrentUser");
            }
        }
    }

}
