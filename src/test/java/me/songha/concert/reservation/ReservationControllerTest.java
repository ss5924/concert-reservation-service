package me.songha.concert.reservation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReservationRepositoryService reservationRepositoryService;

    private List<ReservationDto> reservationList;

    @BeforeEach
    void setUp() {
        reservationList = List.of(
                ReservationDto.builder()
                        .id(1L)
                        .userId(1L)
                        .concertId(1L)
                        .concertTitle("Title 1")
                        .build(),
                ReservationDto.builder()
                        .id(2L)
                        .userId(2L)
                        .concertId(1L)
                        .concertTitle("Title 1")
                        .build()
        );
    }

    @DisplayName("userId로 reservations을 조회한다.")
    @Test
    void getReservationsByUserId() throws Exception {
        Long userId = 1L;
        int page = 0, size = 10;
        Page<ReservationDto> reservations = new PageImpl<>(reservationList.subList(0, 1));

        Mockito.when(reservationRepositoryService.getReservationsByUserId(eq(userId), any(Pageable.class)))
                .thenReturn(reservations);

        mockMvc.perform(get("/reservation/user-id/" + userId + "?page=" + page + "&size=" + size))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].userId").value(1L))
                .andExpect(jsonPath("$.content[0].concertId").value(1L))
                .andExpect(jsonPath("$.content[0].concertTitle").value("Title 1"));

        Mockito.verify(reservationRepositoryService, times(1)).getReservationsByUserId(eq(userId), any(Pageable.class));
    }

    @DisplayName("concertId로 reservations을 조회한다.")
    @Test
    void getReservationsByConcertId() throws Exception {
        Long concertId = 1L;
        int page = 0, size = 10;
        Page<ReservationDto> reservations = new PageImpl<>(reservationList);

        Mockito.when(reservationRepositoryService.getReservationsByConcertId(eq(concertId), any(Pageable.class)))
                .thenReturn(reservations);

        mockMvc.perform(get("/reservation/concert-id/" + concertId + "?page=" + page + "&size=" + size))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].userId").value(1L))
                .andExpect(jsonPath("$.content[0].concertId").value(1L))
                .andExpect(jsonPath("$.content[0].concertTitle").value("Title 1"))
                .andExpect(jsonPath("$.content[1].id").value(2L))
                .andExpect(jsonPath("$.content[1].userId").value(2L))
                .andExpect(jsonPath("$.content[1].concertId").value(1L))
                .andExpect(jsonPath("$.content[1].concertTitle").value("Title 1"));
        ;

        Mockito.verify(reservationRepositoryService, times(1)).getReservationsByConcertId(eq(concertId), any(Pageable.class));
    }

    @DisplayName("userId와 concertId로 reservation을 조회한다.")
    @Test
    void getReservationByUserIdAndConcertId() throws Exception {
        Long userId = 1L;
        Long concertId = 1L;
        ReservationDto reservationDto = reservationList.getFirst();

        Mockito.when(reservationRepositoryService.getReservationByUserIdAndConcertId(userId, concertId))
                .thenReturn(reservationDto);

        mockMvc.perform(get("/reservation/user-id/" + userId + "/concert-id/" + concertId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.concertId").value(1L))
                .andExpect(jsonPath("$.concertTitle").value("Title 1"));

        Mockito.verify(reservationRepositoryService, times(1)).getReservationByUserIdAndConcertId(userId, concertId);
    }

    @DisplayName("reservation을 생성한다.")
    @Test
    void createReservation() throws Exception {
        ReservationDto reservationDto = mock(ReservationDto.class);

        mockMvc.perform(post("/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservationDto)))
                .andExpect(status().isCreated());

        Mockito.verify(reservationRepositoryService, times(1)).createReservation(any(ReservationDto.class));
    }

    @DisplayName("reservation을 수정한다.")
    @Test
    void updateReservation() throws Exception {
        Long reservationId = 1L;
        ReservationDto reservationDto = mock(ReservationDto.class);

        mockMvc.perform(put("/reservation/" + reservationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservationDto)))
                .andExpect(status().isOk());

        Mockito.verify(reservationRepositoryService, times(1)).updateReservation(eq(reservationId), any(ReservationDto.class));
    }

    @DisplayName("reservation을 삭제한다.")
    @Test
    void deleteReservation() throws Exception {
        Long reservationId = 1L;

        mockMvc.perform(delete("/reservation/" + reservationId))
                .andExpect(status().isNoContent());

        Mockito.verify(reservationRepositoryService, times(1)).deleteReservation(reservationId);
    }
}