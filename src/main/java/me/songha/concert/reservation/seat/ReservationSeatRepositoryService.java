package me.songha.concert.reservation.seat;

import lombok.RequiredArgsConstructor;
import me.songha.concert.seatprice.SeatPriceNotFoundException;
import me.songha.concert.seatprice.SeatPriceRepository;
import me.songha.concert.reservation.general.Reservation;
import me.songha.concert.reservation.general.ReservationNotFoundException;
import me.songha.concert.reservation.general.ReservationRepository;
import me.songha.concert.seat.Seat;
import me.songha.concert.seat.SeatNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class ReservationSeatRepositoryService {
    private final ReservationSeatRepository reservationSeatRepository;
    private final ReservationRepository reservationRepository;
    private final SeatPriceRepository seatPriceRepository;

    public void createReservationSeat(Long reservationId, String seatNumber) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("[Error] Reservation not found."));

        Seat seat = reservation.getConcert().getVenue().getSeats().stream()
                .filter(s -> s.getSeatNumber().equals(seatNumber))
                .findFirst().orElseThrow(() -> new SeatNotFoundException("[Error] VenueSeat not found."));

        int price = seatPriceRepository.findAmountByConcertIdAndGrade(reservation.getConcert().getId(), seat.getGrade())
                .orElseThrow(() -> new SeatPriceNotFoundException("[Error] ConcertSeat not found."));

        ReservationSeat reservationSeat = ReservationSeat.builder()
                .price(price)
                .seat(seat)
                .reservation(reservation).build();

        reservationSeatRepository.save(reservationSeat);
    }
}
