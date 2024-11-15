package me.songha.concert.reservationseat;

import com.github.fppt.jedismock.RedisServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataRedisTest
class ReservationSeatServiceTest {
    private static RedisServer redisServer;
    private StringRedisTemplate redisTemplate;
    private ReservationSeatRepository reservationSeatRepository;
    private ReservationSeatService reservationSeatService;

    @BeforeAll
    static void startMockRedis() throws IOException {
        redisServer = RedisServer.newRedisServer(); // Mock Redis 서버 시작
        redisServer.start();
    }

    @AfterAll
    static void stopMockRedis() {
        redisServer.stop(); // Mock Redis 서버 종료
    }

    @BeforeEach
    void setUp() {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName("localhost");
        jedisConnectionFactory.setPort(redisServer.getBindPort());
        jedisConnectionFactory.afterPropertiesSet();

        redisTemplate = new StringRedisTemplate(jedisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        reservationSeatRepository = Mockito.mock(ReservationSeatRepository.class);
        reservationSeatService = new ReservationSeatService(redisTemplate, reservationSeatRepository);
    }

    @Test
    void 좌석을_선점한다() {
        boolean result = reservationSeatService.reserveSeat(1L, 1L, "user123");
        assertTrue(result);

        String value = redisTemplate.opsForValue().get("concert:1:seat:1");
        assertEquals("user123", value);
    }

    @Test
    void 이미_좌석을_선점한_유저() {
        redisTemplate.opsForValue().set("concert:1:seat:1", "user123");

        boolean result = reservationSeatService.reserveSeat(1L, 1L, "user123");
        assertFalse(result);
    }

    @Test
    void 이미_팔린_티켓을_테스트한다_db() {
        Mockito.when(reservationSeatRepository.findIdsByConcertId(1L))
                .thenReturn(Collections.singletonList(1L));

        List<Long> soldSeats = reservationSeatService.getSoldSeats(1L);
        assertEquals(1, soldSeats.size());
        assertTrue(soldSeats.contains(1L));
    }

    @Test
    void 선점한_좌석을_확인한다_redis() {
        redisTemplate.opsForValue().set("concert:1:seat:1", "user123");
        redisTemplate.opsForValue().set("concert:1:seat:2", "user456");

        Set<String> reservedSeats = reservationSeatService.getReservedSeats(1L);
        assertEquals(2, reservedSeats.size());
        assertTrue(reservedSeats.contains("concert:1:seat:1"));
        assertTrue(reservedSeats.contains("concert:1:seat:2"));
    }

    @Test
    void 예약가능한_좌석을_확인한다() {
        Mockito.when(reservationSeatRepository.findIdsByConcertId(1L))
                .thenReturn(Collections.singletonList(1L));
        redisTemplate.opsForValue().set("concert:1:seat:2", "user123");

        assertFalse(reservationSeatService.isSeatAvailable(1L, 1L));
        assertFalse(reservationSeatService.isSeatAvailable(1L, 2L));
        assertTrue(reservationSeatService.isSeatAvailable(1L, 3L));
    }
}
