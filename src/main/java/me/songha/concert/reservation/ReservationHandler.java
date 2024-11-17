package me.songha.concert.reservation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.songha.concert.concertseat.ConcertSeatService;
import me.songha.concert.reservationhistory.ReservationHistoryDto;
import me.songha.concert.reservationhistory.ReservationHistoryService;
import me.songha.concert.reservationseat.ReservationSeatNotAvailableException;
import me.songha.concert.reservationseat.ReservationSeatPreoccupyService;
import me.songha.concert.reservationseat.ReservationSeatService;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class ReservationHandler {
    private final ReservationService reservationService;
    private final ReservationHistoryService reservationHistoryService;
    private final ReservationSeatPreoccupyService reservationSeatPreoccupyService;
    private final ReservationSeatService reservationSeatService;
    private final ConcertSeatService concertSeatService;

    public Long createReservation(ReservationDto reservationDto) {
        return reservationService.createReservation(reservationDto);
    }

    public void preoccupySeats(Long concertId, Long userId, List<String> seatNumbers) {
        validateMaxSeatLimit(seatNumbers);

        for (String seatNumber : seatNumbers) {
            boolean isSeatAvailable = reservationSeatPreoccupyService.isSeatAvailable(concertId, seatNumber);
            if (!isSeatAvailable) {
                logAndThrowSeatNotAvailableException(concertId, seatNumber);
            }
            boolean isPreoccupy = reservationSeatPreoccupyService.preoccupySeats(concertId, seatNumber, userId);
            if (!isPreoccupy) {
                logAndThrowSeatNotAvailableException(concertId, seatNumber);
            }
        }
    }

    private void validateMaxSeatLimit(List<String> seatNumbers) {
        if (seatNumbers.size() > 4) {
            throw new IllegalArgumentException("Max number of seats is 4.");
        }
    }

    private void logAndThrowSeatNotAvailableException(Long concertId, String seatNumber) {
        log.info("Seat is not available. SeatNumber: {}, ConcertId: {}", seatNumber, concertId);
        throw new ReservationSeatNotAvailableException("[Error] Seat is not available.");
    }

    public void updateReservationStatus(Long reservationId, Long userId, int amount, ReservationStatus status) {
        reservationService.updateReservationStatus(reservationId, status);

        ReservationHistoryDto reservationHistoryDto = ReservationHistoryDto.builder()
                .userId(userId)
                .reservationId(reservationId)
                .amount(amount)
                .reservationStatus(status.toString())
                .build();

        reservationHistoryService.createReservationHistory(reservationHistoryDto);
    }

    public void reserveSeats(Long reservationId, List<String> seatNumbers) {
        for (String seatNumber : seatNumbers) {
            reservationSeatService.createReservationSeat(reservationId, seatNumber);
        }
    }

    public String generateReservationNumber(Long reservationId) {
        return ReservationNumberGenerator.generateReservationNumber(reservationId);
    }

    public int getTotalAmount(Long concertId, List<String> seatNumbers) {
        return concertSeatService.getTotalAmount(concertId, seatNumbers);
    }
}
