package me.songha.concert.concertseat;

import me.songha.concert.venueseat.VenueSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ConcertSeatRepository extends JpaRepository<ConcertSeat, Long> {
    @Query("select cs from ConcertSeat cs where cs.concert.id = :concertId")
    List<ConcertSeat> findByConcertId(Long concertId);

    @Query("select coalesce(sum(cs.price), 0) from ConcertSeat cs where cs.concert.id = :concertId and cs.venueSeat.seatNumber in :seatNumbers")
    int findAmountByConcertIdAndSeatNumbers(Long concertId, List<String> seatNumbers);
}
