package me.songha.concert.reservation;

import me.songha.concert.payment.MockPaymentService;
import me.songha.concert.payment.PaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ReservationFlowServiceTest {

    @InjectMocks
    private ReservationFlowService reservationFlowService;

    @Mock
    private ReservationHandler reservationHandler;

    @Mock
    private MockPaymentService mockPaymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("예매를 시작한다 - pending")
    @Test
    void pendingReservation() {
        ReservationDto reservationDto = mock(ReservationDto.class);
        reservationDto.setId(1L);
        reservationDto.setUserId(100L);
        reservationDto.setConcertId(200L);
        reservationDto.setTotalAmount(500);
        reservationDto.setSeatNumbers(List.of("A1", "A2"));

        when(reservationHandler.createReservation(reservationDto)).thenReturn(1L);

        Long reservationId = reservationFlowService.pendingReservation(reservationDto);

        assertEquals(1L, reservationId);
        verify(reservationHandler, times(1)).createReservation(reservationDto);
        verify(reservationHandler, times(1)).updateReservationStatus(
                reservationDto.getId(),
                reservationDto.getUserId(),
                reservationDto.getTotalAmount(),
                ReservationStatus.PENDING
        );
        verify(reservationHandler, times(1)).preoccupySeats(
                reservationDto.getConcertId(),
                reservationDto.getUserId(),
                reservationDto.getSeatNumbers()
        );
    }

    @DisplayName("예매를 완료한다 - confirmed")
    @Test
    void progressReservation_Confirmed() {
        Long reservationId = 1L;
        Long userId = 100L;
        Long concertId = 1L;
        int amount = 0;
        List<String> seatNumbers = List.of("A1", "A2");

        when(mockPaymentService.getPaymentStatus(reservationId, amount)).thenReturn(PaymentStatus.CONFIRMED);

        reservationFlowService.progressReservation(reservationId,  userId, seatNumbers, concertId);

        verify(reservationHandler, times(1)).reserveSeats(reservationId, seatNumbers);
        verify(reservationHandler, times(1)).updateReservationStatus(
                reservationId,
                userId,
                amount,
                ReservationStatus.CONFIRMED
        );
    }

    @DisplayName("예매가 실패한다 - rejected")
    @Test
    void progressReservation_Rejected() {
        Long reservationId = 1L;
        int amount = 0;
        Long concertId = 1L;
        Long userId = 100L;
        List<String> seatNumbers = List.of("A1", "A2");

        when(mockPaymentService.getPaymentStatus(reservationId, amount)).thenReturn(PaymentStatus.REJECTED);

        reservationFlowService.progressReservation(reservationId,  userId, seatNumbers, concertId);

        verify(reservationHandler, never()).reserveSeats(anyLong(), anyList());
        verify(reservationHandler, times(1)).updateReservationStatus(
                reservationId,
                userId,
                amount,
                ReservationStatus.REJECTED
        );
    }

    @DisplayName("예매가 취소된다 - cancelled")
    @Test
    void progressReservation_Cancelled() {
        Long reservationId = 1L;
        int amount = 0;
        Long concertId = 1L;
        Long userId = 100L;
        List<String> seatNumbers = List.of("A1", "A2");

        when(mockPaymentService.getPaymentStatus(reservationId, amount)).thenReturn(PaymentStatus.CANCELLED);

        reservationFlowService.progressReservation(reservationId,  userId, seatNumbers, concertId);

        verify(reservationHandler, never()).reserveSeats(anyLong(), anyList());
        verify(reservationHandler, times(1)).updateReservationStatus(
                reservationId,
                userId,
                amount,
                ReservationStatus.CANCELED
        );
    }
}
