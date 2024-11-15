package me.songha.concert.venue;

import lombok.RequiredArgsConstructor;
import me.songha.concert.venueseat.VenueSeat;
import me.songha.concert.venueseat.VenueSeatRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class VenueService {
    private final VenueRepository venueRepository;
    private final VenueConverter venueConverter;
    private final VenueSeatRepository venueSeatRepository;

    public Page<VenueDto> getAllVenues(Pageable pageable) {
        return venueRepository.findAll(pageable).map(venueConverter::toDto);
    }

    public VenueDto getVenueByName(String name) {
        return venueRepository.findByName(name)
                .map(venueConverter::toDto)
                .orElseThrow(() -> new VenueNotFoundException("[Error] Venue not found."));
    }

    public VenueDto getVenue(Long id) {
        return venueRepository.findById(id)
                .map(venueConverter::toDto)
                .orElseThrow(() -> new VenueNotFoundException("[Error] Venue not found."));
    }

    public void createVenue(VenueDto venueDto) {
        List<VenueSeat> venueSeats = venueSeatRepository.findByVenueId(venueDto.getId());

        venueRepository.save(venueConverter.toEntity(venueDto, venueSeats));
    }

    public void updateVenue(Long id, VenueDto venueDto) {
        Venue venue = venueRepository.findById(id)
                .orElseThrow(() -> new VenueNotFoundException("[Error] Venue not found."));

        List<VenueSeat> venueSeats = venueSeatRepository.findByVenueId(venueDto.getId());

        venue.update(
                venueDto.getName(),
                venueDto.getCapacity(),
                venueSeats
        );

        venueRepository.save(venueConverter.toEntity(venueDto, venueSeats));
    }

    public void deleteVenue(Long id) {
        Venue venue = venueRepository.findById(id)
                .orElseThrow(() -> new VenueNotFoundException("[Error] Venue not found."));

        venueRepository.delete(venue);
    }
}
