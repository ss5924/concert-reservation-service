package me.songha.concert.concertseat;

import me.songha.concert.common.NotFoundException;

public class ConcertSeatNotFoundException extends NotFoundException {
    public ConcertSeatNotFoundException(String message) {
        super(message);
    }
}
