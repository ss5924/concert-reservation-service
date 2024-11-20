package me.songha.concert.reservation.history;

import me.songha.concert.reservation.general.Reservation;
import me.songha.concert.reservation.general.ReservationStatus;
import org.springframework.stereotype.Component;

@Component
public class ReservationHistoryConverter {
    public ReservationHistory toEntity(ReservationHistoryDto reservationHistoryDto, Reservation reservation) {
        return ReservationHistory.builder()
                .id(reservationHistoryDto.getId())
                .userId(reservationHistoryDto.getUserId())
                .reservation(reservation)
                .status(ReservationStatus.fromString(reservationHistoryDto.getReservationStatus()))
                .amount(reservationHistoryDto.getAmount())
                .build();
    }

    public ReservationHistoryDto toDto(ReservationHistory reservationHistory) {
        return ReservationHistoryDto.builder()
                .id(reservationHistory.getId())
                .userId(reservationHistory.getUserId())
                .reservationId(reservationHistory.getReservation().getId())
                .reservationStatus(reservationHistory.getStatus().toString())
                .amount(reservationHistory.getAmount())
                .build();
    }
}
