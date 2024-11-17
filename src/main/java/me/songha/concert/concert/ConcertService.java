package me.songha.concert.concert;

import lombok.RequiredArgsConstructor;
import me.songha.concert.concertseat.ConcertSeat;
import me.songha.concert.concertseat.ConcertSeatRepository;
import me.songha.concert.venue.Venue;
import me.songha.concert.venue.VenueNotFoundException;
import me.songha.concert.venue.VenueRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class ConcertService {
    private final ConcertRepository concertRepository;
    private final VenueRepository venueRepository;
    private final ConcertSeatRepository concertSeatRepository;
    private final ConcertConverter concertConverter;

    public Page<ConcertDto> getAllConcerts(Pageable pageable) {
        return concertRepository.findAll(pageable).map(concertConverter::toDto);
    }

    public ConcertDto getConcert(Long id) {
        return concertRepository.findById(id)
                .map(concertConverter::toDto)
                .orElseThrow(() -> new ConcertNotFoundException("[Error] Concert not found."));
    }

    public void createConcert(ConcertDto concertDto) {
        Venue venue = venueRepository.findByName(concertDto.getVenueName())
                .orElseThrow(() -> new VenueNotFoundException("[Error] Venue not found."));

        concertRepository.save(concertConverter.toEntity(concertDto, venue));
    }

    public void updateConcert(Long id, ConcertDto concertDto) {
        Concert concert = concertRepository.findById(id)
                .orElseThrow(() -> new ConcertNotFoundException("[Error] Concert not found."));

        Venue venue = venueRepository.findByName(concertDto.getVenueName())
                .orElseThrow(() -> new VenueNotFoundException("[Error] Venue not found."));

        List<ConcertSeat> concertSeats = concertSeatRepository.findByConcertId(concert.getId());

        concert.update(
                concertDto.getTitle(),
                concertDto.getDescription(),
                venue,
                concertSeats,
                concertDto.getConcertDate(),
                concertDto.getRunningTime()
        );

        concertRepository.save(concertConverter.toEntity(concertDto, venue));
    }

    public void deleteConcert(Long id) {
        Concert concert = concertRepository.findById(id)
                .orElseThrow(() -> new ConcertNotFoundException("[Error] Concert not found."));

        concertRepository.delete(concert);
    }

}
