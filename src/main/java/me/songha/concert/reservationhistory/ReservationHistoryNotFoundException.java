package me.songha.concert.reservationhistory;

import me.songha.concert.common.NotFoundException;

public class ReservationHistoryNotFoundException extends NotFoundException {
    public ReservationHistoryNotFoundException(String message) {
        super(message);
    }
}
