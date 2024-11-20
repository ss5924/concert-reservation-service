package me.songha.concert.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import me.songha.concert.reservation.ReservationDto;
import me.songha.concert.reservation.ReservationNotFoundException;
import me.songha.concert.reservation.ReservationRepositoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static io.jsonwebtoken.Jwts.builder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class JwtAuthenticationFilterTest {

    @Autowired
    private MockMvc mockMvc;

    private SecretKey key;
    private String validToken;
    private String expiredToken;

    @MockBean
    private ReservationRepositoryService reservationRepositoryService;

    @BeforeEach
    void setUp() {
        String SECRET_KEY = "my-fixed-secret-key-my-fixed-secret-key";
        key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

        validToken = Jwts.builder()
                .setSubject("user123")
                .claim("role", "ADMIN")
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 1000))
                .signWith(key)
                .compact();

        expiredToken = Jwts.builder()
                .setSubject("user123")
                .claim("role", "USER")
                .setExpiration(new Date(System.currentTimeMillis() - 60 * 1000))
                .signWith(key)
                .compact();

        ReservationDto reservationDto = ReservationDto.builder().id(1L).userId(1L).totalAmount(100).build();

        Mockito.when(reservationRepositoryService.getReservation(1L))
                .thenReturn(reservationDto);

        Mockito.when(reservationRepositoryService.getReservation(99L))
                .thenThrow(new ReservationNotFoundException("[Error] Reservation not found."));
    }

    @Test
    void 올바른_토큰_인증_성공() throws Exception {
        mockMvc.perform(get("/reservation/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void 토큰_없음_401_응답() throws Exception {
        mockMvc.perform(get("/reservation/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void 만료된_토큰_401_응답() throws Exception {
        mockMvc.perform(get("/reservation/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + expiredToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void 잘못된_토큰_401_응답() throws Exception {
        String invalidToken = "invalid.token.value";

        mockMvc.perform(get("/reservation/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + invalidToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void 존재하지_않는_예약_404_응답() throws Exception {
        mockMvc.perform(get("/reservation/99")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void generateJWT() {
        String jwt = Jwts.builder()
                .claim("userId", 321L)
                .setSubject("userId123")
                .claim("role", "USER")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 300 * 60 * 60 * 24)) // 300일 유효
                .signWith(key)
                .compact();

        System.out.println(jwt);
    }
}
