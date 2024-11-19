package me.songha.concert.concertseat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SeatPriceRepository extends JpaRepository<SeatPrice, Long> {
    @Query("select csp from SeatPrice csp where csp.concert.id = :concertId")
    List<SeatPrice> findByConcertId(Long concertId);

    @Query("select sp.grade, sp.price from SeatPrice sp " +
            "where sp.concert.id = :concertId and sp.grade in :grades")
    Map<String, Integer> findPricesByConcertIdAndGrades(Long concertId, List<SeatGrade> grades);

    @Query("select sum(sp.price) from SeatPrice sp where sp.concert.id = :concertId and sp.grade = :grade")
    Optional<Integer> findAmountByConcertIdAndGrade(Long concertId, SeatGrade grade);
}
