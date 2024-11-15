package me.songha.concert.reservation;

import me.songha.concert.concert.Concert;
import me.songha.concert.reservationseat.ReservationSeat;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReservationConverter {
    public Reservation toEntity(ReservationDto reservationDto, Concert concert, List<ReservationSeat> reservationSeats) {
        return Reservation.builder()
                .id(reservationDto.getId())
                .userId(reservationDto.getUserId())
                .concert(concert)
                .totalAmount(reservationDto.getTotalAmount())
                .reservationSeats(reservationSeats)
                .build();
    }

    public ReservationDto toDto(Reservation reservation) {
        return ReservationDto.builder()
                .id(reservation.getId())
                .userId(reservation.getUserId())
                .concertId(reservation.getConcert().getId())
                .concertTitle(reservation.getConcert().getTitle())
                .seatNumbers(reservation.getReservationSeats()
                        .stream().map(reservationSeat -> reservationSeat.getVenueSeat().getSeatNumber()).toList())
                .build();
    }

}