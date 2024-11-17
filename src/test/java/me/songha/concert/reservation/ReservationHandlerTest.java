package me.songha.concert.reservation;

import me.songha.concert.reservationhistory.ReservationHistoryDto;
import me.songha.concert.reservationhistory.ReservationHistoryService;
import me.songha.concert.reservationseat.ReservationSeatNotAvailableException;
import me.songha.concert.reservationseat.ReservationSeatPreoccupyService;
import me.songha.concert.reservationseat.ReservationSeatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationHandlerTest {

    @InjectMocks
    private ReservationHandler reservationHandler;

    @Mock
    private ReservationService reservationService;

    @Mock
    private ReservationHistoryService reservationHistoryService;

    @Mock
    private ReservationSeatPreoccupyService reservationSeatPreoccupyService;

    @Mock
    private ReservationSeatService reservationSeatService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("예약 프로세스를 생성한다.")
    @Test
    void createReservation() {
        ReservationDto reservationDto = mock(ReservationDto.class);
        when(reservationService.createReservation(reservationDto)).thenReturn(1L);

        Long reservationId = reservationHandler.createReservation(reservationDto);

        assertEquals(1L, reservationId);
        verify(reservationService, times(1)).createReservation(reservationDto);
    }

    @DisplayName("좌석을 선점한다 - success")
    @Test
    void preoccupySeats_Success() {
        Long concertId = 1L;
        Long userId = 1L;
        List<String> seatNumbers = Arrays.asList("A1", "A2");

        when(reservationSeatPreoccupyService.isSeatAvailable(concertId, "A1")).thenReturn(true);
        when(reservationSeatPreoccupyService.isSeatAvailable(concertId, "A2")).thenReturn(true);
        when(reservationSeatPreoccupyService.preoccupySeats(concertId, "A1", userId)).thenReturn(true);
        when(reservationSeatPreoccupyService.preoccupySeats(concertId, "A2", userId)).thenReturn(true);

        assertDoesNotThrow(() -> reservationHandler.preoccupySeats(concertId, userId, seatNumbers));

        verify(reservationSeatPreoccupyService, times(1)).isSeatAvailable(concertId, "A1");
        verify(reservationSeatPreoccupyService, times(1)).isSeatAvailable(concertId, "A2");
        verify(reservationSeatPreoccupyService, times(1)).preoccupySeats(concertId, "A1", userId);
        verify(reservationSeatPreoccupyService, times(1)).preoccupySeats(concertId, "A2", userId);
    }

    @DisplayName("좌석을 선점한다 - fail")
    @Test
    void preoccupySeats_Fail() {
        Long concertId = 1L;
        Long userId = 1L;
        List<String> seatNumbers = Arrays.asList("A1", "A2");

        when(reservationSeatPreoccupyService.isSeatAvailable(concertId, "A1")).thenReturn(false);

        ReservationSeatNotAvailableException exception = assertThrows(ReservationSeatNotAvailableException.class,
                () -> reservationHandler.preoccupySeats(concertId, userId, seatNumbers));

        assertEquals("[Error] Seat is not available.", exception.getMessage());
        verify(reservationSeatPreoccupyService, times(1)).isSeatAvailable(concertId, "A1");
        verify(reservationSeatPreoccupyService, never()).preoccupySeats(anyLong(), anyString(), anyLong());
    }

    @DisplayName("예매 프로세스 상태를 수정한다.")
    @Test
    void updateReservationStatus() {
        Long reservationId = 1L;
        Long userId = 1L;
        int amount = 100;
        ReservationStatus status = ReservationStatus.CONFIRMED;

        ReservationHistoryDto reservationHistoryDto = ReservationHistoryDto.builder()
                .userId(userId)
                .reservationId(reservationId)
                .amount(amount)
                .reservationStatus(status.toString())
                .build();

        reservationHandler.updateReservationStatus(reservationId, userId, amount, status);

        verify(reservationService, times(1)).updateReservationStatus(reservationId, status);
        verify(reservationHistoryService, times(1)).createReservationHistory(reservationHistoryDto);
    }

    @DisplayName("좌석을 선점한다.")
    @Test
    void reserveSeats() {
        Long reservationId = 1L;
        List<String> seatNumbers = Arrays.asList("A1", "A2");

        reservationHandler.reserveSeats(reservationId, seatNumbers);

        verify(reservationSeatService, times(1)).createReservationSeat(reservationId, "A1");
        verify(reservationSeatService, times(1)).createReservationSeat(reservationId, "A2");
    }
}