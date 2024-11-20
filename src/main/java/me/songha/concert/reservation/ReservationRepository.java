package me.songha.concert.reservation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("select r from Reservation r where r.userId = :userId")
    Page<Reservation> findByUserId(Long userId, Pageable pageable);

    @Query("select r from Reservation r where r.concert.id = :concertId")
    Page<Reservation> findByConcertId(Long concertId, Pageable pageable);

    @Query("select r from Reservation r where r.userId = :userId and r.concert.id = :concertId")
    Optional<Reservation> findByUserIdAndConcertId(Long userId, Long concertId);
}
