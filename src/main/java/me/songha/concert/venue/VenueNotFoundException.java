package me.songha.concert.venue;

import me.songha.concert.common.NotFoundException;

public class VenueNotFoundException extends NotFoundException {
    public VenueNotFoundException(String message) {
        super(message);
    }
}
