package me.songha.concert.venueseat;

import me.songha.concert.common.NotFoundException;

public class VenueSeatNotFoundException extends NotFoundException {
    public VenueSeatNotFoundException(String message) {
        super(message);
    }
}
