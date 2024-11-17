package me.songha.concert.reservation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.songha.concert.payment.MockPaymentService;
import me.songha.concert.payment.PaymentStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class ReservationFlowService {
    private final ReservationHandler reservationHandler;
    private final MockPaymentService mockPaymentService;

    public Long pendingReservation(ReservationDto reservationDto) {
        Long reservationId = reservationHandler.createReservation(reservationDto);
        reservationHandler.updateReservationStatus(
                reservationDto.getId(),
                reservationDto.getUserId(),
                reservationDto.getTotalAmount(),
                ReservationStatus.PENDING);
        reservationHandler.preoccupySeats(reservationDto.getConcertId(), reservationDto.getUserId(), reservationDto.getSeatNumbers());
        return reservationId;
    }

    public String progressReservation(Long reservationId, Long userId, List<String> seatNumbers, Long concertId) {
        int amount = reservationHandler.getTotalAmount(concertId, seatNumbers);
        PaymentStatus paymentStatus = mockPaymentService.getPaymentStatus(reservationId, amount);

        switch (paymentStatus) {
            case CONFIRMED -> {
                log.info("Reservation processed successfully. reservationId={}", reservationId);
                reservationHandler.reserveSeats(reservationId, seatNumbers);
                reservationHandler.updateReservationStatus(
                        reservationId,
                        userId,
                        amount,
                        ReservationStatus.CONFIRMED);
            }
            case REJECTED -> {
                log.info("Reservation processing rejected. reservationId={}", reservationId);
                reservationHandler.updateReservationStatus(
                        reservationId,
                        userId,
                        amount,
                        ReservationStatus.REJECTED);
            }
            case CANCELLED -> {
                log.info("Reservation processing canceled. reservationId={}", reservationId);
                reservationHandler.updateReservationStatus(
                        reservationId,
                        userId,
                        amount,
                        ReservationStatus.CANCELED);
            }
            default -> throw new IllegalStateException("Unexpected value: " + paymentStatus);
        }

        return reservationHandler.generateReservationNumber(reservationId);
    }

}
