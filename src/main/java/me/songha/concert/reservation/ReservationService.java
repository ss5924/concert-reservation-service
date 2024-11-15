package me.songha.concert.reservation;

import lombok.RequiredArgsConstructor;
import me.songha.concert.concert.Concert;
import me.songha.concert.concert.ConcertNotFoundException;
import me.songha.concert.concert.ConcertRepository;
import me.songha.concert.reservationseat.ReservationSeat;
import me.songha.concert.reservationseat.ReservationSeatRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationConverter reservationConverter;
    private final ConcertRepository concertRepository;
    private final ReservationSeatRepository reservationSeatRepository;

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

    public void createReservation(ReservationDto reservationDto) {
        Concert concert = concertRepository.findById(reservationDto.getConcertId())
                .orElseThrow(() -> new ConcertNotFoundException("[Error] Concert not found."));

        List<ReservationSeat> reservationSeats = reservationSeatRepository.findByReservation(reservationDto.getId());

        Reservation reservation = reservationConverter.toEntity(reservationDto, concert, reservationSeats);

        reservationRepository.save(reservation);
    }

    public void updateReservation(Long id, ReservationDto reservationDto) {
        List<ReservationSeat> reservationSeats = reservationSeatRepository.findByReservation(reservationDto.getId());

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("[Error] Reservation not found."));

        reservation.update(
                reservationDto.getTotalAmount(),
                ReservationStatus.fromString(reservationDto.getReservationStatus()),
                reservationSeats
        );

        reservationRepository.save(reservation);
    }

    public void deleteReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("[Error] Reservation not found."));

        reservationRepository.delete(reservation);
    }
}
