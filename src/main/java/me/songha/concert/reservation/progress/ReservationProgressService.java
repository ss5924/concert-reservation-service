package me.songha.concert.reservation.progress;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.songha.concert.common.ReservationIllegalArgumentException;
import me.songha.concert.seatprice.SeatGrade;
import me.songha.concert.seatprice.SeatPriceRepositoryService;
import me.songha.concert.payment.MockPaymentService;
import me.songha.concert.payment.PaymentStatus;
import me.songha.concert.reservation.general.ReservationRepositoryService;
import me.songha.concert.reservation.general.ReservationStatus;
import me.songha.concert.reservation.history.ReservationHistoryDto;
import me.songha.concert.reservation.history.ReservationHistoryRepositoryService;
import me.songha.concert.reservation.seat.ReservationSeatPreoccupyService;
import me.songha.concert.reservation.seat.ReservationSeatRepositoryService;
import me.songha.concert.seat.SeatRepositoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class ReservationProgressService {
    private final MockPaymentService mockPaymentService;
    private final ReservationRepositoryService reservationRepositoryService;
    private final ReservationHistoryRepositoryService reservationHistoryRepositoryService;
    private final ReservationSeatRepositoryService reservationSeatRepositoryService;
    private final SeatPriceRepositoryService seatPriceRepositoryService;
    private final SeatRepositoryService seatRepositoryService;
    private final ReservationSeatPreoccupyService reservationSeatPreoccupyService;

    public void progressReservation(Long reservationId, Long userId, List<String> seatNumbers, Long concertId) {
        reservationSeatPreoccupyService.isValidatedSeatForCurrentUser(concertId, userId, seatNumbers);

        int amount = getTotalAmount(concertId, seatNumbers);
        PaymentStatus paymentStatus = mockPaymentService.getPaymentStatus(reservationId, amount);
        String reservationNumber = generateReservationNumber(reservationId);

        switch (paymentStatus) {
            case CONFIRMED -> {
                log.info("Reservation processed successfully. reservationId={}", reservationId);
                reserveSeats(reservationId, seatNumbers);
                updateReservationInfo(reservationId, reservationNumber, amount, ReservationStatus.CONFIRMED);
                saveHistory(reservationId, userId, amount, ReservationStatus.CONFIRMED);
            }
            case REJECTED -> {
                log.info("Reservation processing rejected. reservationId={}", reservationId);
                updateReservationInfo(reservationId, reservationNumber, amount, ReservationStatus.REJECTED);
                saveHistory(reservationId, userId, amount, ReservationStatus.REJECTED);
                throw new ReservationIllegalArgumentException("[Error] Payment is rejected.");
            }
            case CANCELLED -> {
                log.info("Reservation processing canceled. reservationId={}", reservationId);
                updateReservationInfo(reservationId, reservationNumber, amount, ReservationStatus.CANCELED);
                saveHistory(reservationId, userId, amount, ReservationStatus.CANCELED);
                throw new ReservationIllegalArgumentException("[Error] Payment is cancelled.");
            }
            default -> throw new ReservationIllegalArgumentException("[Error] Unexpected value: " + paymentStatus);
        }
    }

    private int getTotalAmount(Long concertId, List<String> seatNumbers) {
        Map<SeatGrade, Integer> tickets = getGroupingTickets(seatNumbers);
        return seatPriceRepositoryService.getTotalAmount(concertId, tickets);
    }

    private Map<SeatGrade, Integer> getGroupingTickets(List<String> seatNumbers) {
        Map<SeatGrade, Integer> result = new HashMap<>();
        seatNumbers.forEach(seatNumber -> {
            SeatGrade grade = seatRepositoryService.getGradeBySeatNumber(seatNumber);
            result.put(grade, result.getOrDefault(grade, 0) + 1);
        });
        return result;
    }

    private void updateReservationInfo(Long reservationId, String reservationNumber, int amount, ReservationStatus status) {
        reservationRepositoryService.updateReservationInfo(reservationId, reservationNumber, amount, status);
    }

    private void saveHistory(Long reservationId, Long userId, int amount, ReservationStatus status) {
        try {
            ReservationHistoryDto reservationHistoryDto = ReservationHistoryDto.builder()
                    .reservationId(reservationId)
                    .userId(userId)
                    .amount(amount)
                    .reservationStatus(status.toString())
                    .build();
            reservationHistoryRepositoryService.createReservationHistory(reservationHistoryDto);
        } catch (Exception e) {
            log.error("[Error] Failed to save History. e.getMessage:{}", e.getMessage(), e);
        }
    }

    private void reserveSeats(Long reservationId, List<String> seatNumbers) {
        for (String seatNumber : seatNumbers) {
            reservationSeatRepositoryService.createReservationSeat(reservationId, seatNumber);
        }
    }

    private String generateReservationNumber(Long reservationId) {
        return ReservationNumberGenerator.generateReservationNumber(reservationId);
    }

}
