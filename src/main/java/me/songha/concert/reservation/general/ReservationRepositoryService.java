package me.songha.concert.reservation.general;

import lombok.RequiredArgsConstructor;
import me.songha.concert.concert.Concert;
import me.songha.concert.concert.ConcertNotFoundException;
import me.songha.concert.concert.ConcertRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class ReservationRepositoryService {
    private final ReservationRepository reservationRepository;
    private final ReservationConverter reservationConverter;
    private final ConcertRepository concertRepository;

    public Page<ReservationDto> getReservationsByUserId(Long userId, Pageable pageable) {
        return reservationRepository.findByUserId(userId, pageable)
                .map(reservationConverter::toDto);
    }

    public Page<ReservationDto> getReservationsByConcertId(Long concertId, Pageable pageable) {
        return reservationRepository.findByConcertId(concertId, pageable)
                .map(reservationConverter::toDto);
    }

    public ReservationDto getReservationByUserIdAndConcertId(Long userId, Long concertId) {
        return reservationRepository.findByUserIdAndConcertId(userId, concertId)
                .map(reservationConverter::toDto)
                .orElseThrow(() -> new ReservationNotFoundException("[Error] Reservation not found."));
    }

    public ReservationDto getReservation(Long id) {
        return reservationRepository.findById(id)
                .map(reservationConverter::toDto)
                .orElseThrow(() -> new ReservationNotFoundException("[Error] Reservation not found."));
    }

    public Long createReservation(ReservationDto reservationDto) {
        Concert concert = concertRepository.findById(reservationDto.getConcertId())
                .orElseThrow(() -> new ConcertNotFoundException("[Error] Concert not found."));
        Reservation reservation = reservationConverter.toEntity(reservationDto, concert, List.of());
        Reservation createdReservation = reservationRepository.save(reservation);

        return createdReservation.getId();
    }

    public void updateReservationInfo(Long id, String reservationNumber, int amount, ReservationStatus status) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("[Error] Reservation not found."));

        reservation.update(amount, status, reservationNumber);

        reservationRepository.save(reservation);
    }

    public void deleteReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("[Error] Reservation not found."));

        reservationRepository.delete(reservation);
    }
}
