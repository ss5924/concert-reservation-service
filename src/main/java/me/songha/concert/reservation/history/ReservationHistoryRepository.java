package me.songha.concert.reservation.history;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ReservationHistoryRepository extends JpaRepository<ReservationHistory, Long> {
    @Query("select rh from ReservationHistory rh where rh.userId = :userId and rh.reservation.id = :reservationId")
    Optional<ReservationHistory> findByUserIdAndReservationId(Long userId, Long reservationId);
}
