package me.songha.concert.venue;

import me.songha.concert.seat.Seat;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VenueConverter {
    public Venue toEntity(VenueDto venueDto, List<Seat> seats) {
        return Venue.builder()
                .id(venueDto.getId())
                .capacity(venueDto.getCapacity())
                .seats(seats)
                .name(venueDto.getName())
                .build();
    }

    public VenueDto toDto(Venue venue) {
        return VenueDto.builder()
                .id(venue.getId())
                .capacity(venue.getCapacity())
                .seatNumbers(venue.getSeats().stream().map(Seat::getSeatNumber).toList())
                .name(venue.getName())
                .build();
    }
}
