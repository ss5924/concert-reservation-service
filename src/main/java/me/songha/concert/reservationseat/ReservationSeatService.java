package me.songha.concert.reservationseat;

import lombok.RequiredArgsConstructor;
import me.songha.concert.concertseat.ConcertSeatNotFoundException;
import me.songha.concert.reservation.Reservation;
import me.songha.concert.reservation.ReservationNotFoundException;
import me.songha.concert.reservation.ReservationRepository;
import me.songha.concert.venueseat.VenueSeat;
import me.songha.concert.venueseat.VenueSeatNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class ReservationSeatService {
    private final ReservationSeatRepository reservationSeatRepository;
    private final ReservationRepository reservationRepository;

    public void createReservationSeat(Long reservationId, String seatNumber) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("[Error] Reservation not found."));

        VenueSeat venueSeat = reservation.getConcert().getVenue().getVenueSeats().stream().filter(seat -> seat.getSeatNumber().equals(seatNumber))
                .findFirst().orElseThrow(() -> new VenueSeatNotFoundException("[Error] VenueSeat not found."));

        int price = reservation.getConcert().getConcertSeats().stream()
                .filter(seat -> seat.getVenueSeat().getSeatNumber().equals(seatNumber))
                .findFirst().orElseThrow(() -> new ConcertSeatNotFoundException("[Error] ConcertSeat not found."))
                .getPrice();

        ReservationSeat reservationSeat = ReservationSeat.builder()
                .price(price)
                .venueSeat(venueSeat)
                .reservation(reservation).build();

        reservationSeatRepository.save(reservationSeat);
    }
}
