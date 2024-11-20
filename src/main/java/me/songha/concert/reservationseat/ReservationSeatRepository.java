package me.songha.concert.reservationseat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReservationSeatRepository extends JpaRepository<ReservationSeat, Long> {
    @Query("select rs from ReservationSeat rs where rs.reservation.id = :reservationId")
    List<ReservationSeat> findByReservation(Long reservationId);

    @Query("select rs.id from ReservationSeat rs where rs.reservation.concert.id = :concertId")
    List<Long> findIdsByConcertId(Long concertId);

    @Query("select rs.seat.seatNumber from ReservationSeat rs where rs.reservation.concert.id = :concertId")
    List<String> findSeatNumbersByConcertId(Long concertId);
}
