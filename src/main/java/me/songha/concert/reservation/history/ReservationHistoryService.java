package me.songha.concert.reservation.history;

import lombok.RequiredArgsConstructor;
import me.songha.concert.reservation.general.Reservation;
import me.songha.concert.reservation.general.ReservationNotFoundException;
import me.songha.concert.reservation.general.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class ReservationHistoryService {
    private final ReservationHistoryRepository reservationHistoryRepository;
    private final ReservationHistoryConverter reservationHistoryConverter;
    private final ReservationRepository reservationRepository;

    public ReservationHistoryDto getReservationHistoryById(Long id) {
        return reservationHistoryRepository.findById(id)
                .map(reservationHistoryConverter::toDto)
                .orElseThrow(() -> new ReservationHistoryNotFoundException("[Error] ReservationHistory not found."));
    }

    public ReservationHistoryDto getReservationHistoryById(Long userId, Long reservationId) {
        return reservationHistoryRepository.findByUserIdAndReservationId(userId, reservationId)
                .map(reservationHistoryConverter::toDto)
                .orElseThrow(() -> new ReservationHistoryNotFoundException("[Error] ReservationHistory not found."));
    }

    public void createReservationHistory(ReservationHistoryDto reservationHistoryDto) {
        Reservation reservation = reservationRepository.findById(reservationHistoryDto.getReservationId())
                .orElseThrow(() -> new ReservationNotFoundException("[Error] Reservation not found."));

        ReservationHistory reservationHistory = reservationHistoryConverter.toEntity(reservationHistoryDto, reservation);

        reservationHistoryRepository.save(reservationHistory);
    }

}
