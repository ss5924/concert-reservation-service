package me.songha.concert.reservation.seat;

import me.songha.concert.common.NotFoundException;

public class ReservationSeatNotAvailableException extends NotFoundException {
    public ReservationSeatNotAvailableException(String message) {
        super(message);
    }
}
