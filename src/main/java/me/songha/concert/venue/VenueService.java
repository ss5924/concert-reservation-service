package me.songha.concert.venue;

import lombok.RequiredArgsConstructor;
import me.songha.concert.seat.Seat;
import me.songha.concert.seat.SeatRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class VenueService {
    private final VenueRepository venueRepository;
    private final VenueConverter venueConverter;
    private final SeatRepository seatRepository;

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
        List<Seat> seats = seatRepository.findByVenueId(venueDto.getId());

        venueRepository.save(venueConverter.toEntity(venueDto, seats));
    }

    public void updateVenue(Long id, VenueDto venueDto) {
        Venue venue = venueRepository.findById(id)
                .orElseThrow(() -> new VenueNotFoundException("[Error] Venue not found."));

        List<Seat> seats = seatRepository.findByVenueId(venueDto.getId());

        venue.update(
                venueDto.getName(),
                venueDto.getCapacity(),
                seats
        );

        venueRepository.save(venueConverter.toEntity(venueDto, seats));
    }

    public void deleteVenue(Long id) {
        Venue venue = venueRepository.findById(id)
                .orElseThrow(() -> new VenueNotFoundException("[Error] Venue not found."));

        venueRepository.delete(venue);
    }
}
