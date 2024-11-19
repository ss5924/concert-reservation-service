package me.songha.concert.concertseat;

import me.songha.concert.common.NotFoundException;

public class SeatPriceNotFoundException extends NotFoundException {
    public SeatPriceNotFoundException(String message) {
        super(message);
    }
}
