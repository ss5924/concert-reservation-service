package me.songha.concert.reservation.preoccupy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.songha.concert.reservationseat.ReservationSeatNotAvailableException;
import me.songha.concert.reservationseat.ReservationSeatPreoccupyService;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class PreoccupySeatService {
    private final ReservationSeatPreoccupyService reservationSeatPreoccupyService;

    public PreoccupySeatsResponse preoccupySeats(Long concertId, Long userId, List<String> seatNumbers) {
        validateMaxSeatLimit(seatNumbers);

        for (String seatNumber : seatNumbers) {
            boolean isSeatAvailable = reservationSeatPreoccupyService.isSeatAvailable(concertId, seatNumber);
            if (!isSeatAvailable) {
                throw new ReservationSeatNotAvailableException("[Error] Seat is not available.");
            }
            boolean isPreoccupy = reservationSeatPreoccupyService.preoccupySeats(concertId, seatNumber, userId);
            if (!isPreoccupy) {
                throw new ReservationSeatNotAvailableException("[Error] Seat is not available.");
            }
        }

        return new PreoccupySeatsResponse(concertId, userId, seatNumbers, "Seats have been reserved.");
    }

    private void validateMaxSeatLimit(List<String> seatNumbers) {
        if (seatNumbers.size() > 4) {
            throw new IllegalArgumentException("Max number of seats is 4.");
        }
    }

}
