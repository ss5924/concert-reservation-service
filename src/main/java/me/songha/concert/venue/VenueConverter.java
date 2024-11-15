package me.songha.concert.venue;

import me.songha.concert.venueseat.VenueSeat;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VenueConverter {
    public Venue toEntity(VenueDto venueDto, List<VenueSeat> venueSeats) {
        return Venue.builder()
                .id(venueDto.getId())
                .capacity(venueDto.getCapacity())
                .venueSeats(venueSeats)
                .name(venueDto.getName())
                .build();
    }

    public VenueDto toDto(Venue venue) {
        return VenueDto.builder()
                .id(venue.getId())
                .capacity(venue.getCapacity())
                .seatNumbers(venue.getVenueSeats().stream().map(VenueSeat::getSeatNumber).toList())
                .name(venue.getName())
                .build();
    }
}
