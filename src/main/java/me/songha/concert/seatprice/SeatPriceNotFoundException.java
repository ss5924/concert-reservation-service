package me.songha.concert.seatprice;

import me.songha.concert.common.NotFoundException;

public class SeatPriceNotFoundException extends NotFoundException {
    public SeatPriceNotFoundException(String message) {
        super(message);
    }
}
