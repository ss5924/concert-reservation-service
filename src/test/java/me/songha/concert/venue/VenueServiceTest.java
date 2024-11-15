package me.songha.concert.venue;

import me.songha.concert.venueseat.VenueSeat;
import me.songha.concert.venueseat.VenueSeatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class VenueServiceTest {
    @Mock
    private VenueRepository venueRepository;

    @Mock
    private VenueConverter venueConverter;

    @Mock
    private VenueSeatRepository venueSeatRepository;

    @InjectMocks
    private VenueService venueService;

    private List<Venue> venues;

    private List<VenueDto> venueList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        venues = List.of(
                Venue.builder().id(1L).name("Venue 1").build(),
                Venue.builder().id(2L).name("Venue 2").build());
        venueList = List.of(
                VenueDto.builder().id(1L).name("Venue 1").build(),
                VenueDto.builder().id(2L).name("Venue 2").build());
    }

    @DisplayName("전체 공연장소를 반환한다.")
    @Test
    void getAllVenues() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Venue> venuePage = new PageImpl<>(venues);

        VenueDto venueDto = venueList.getFirst();

        when(venueRepository.findAll(pageable)).thenReturn(venuePage);
        when(venueConverter.toDto(any(Venue.class))).thenReturn(venueDto);

        Page<VenueDto> result = venueService.getAllVenues(pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        verify(venueRepository, times(1)).findAll(pageable);
    }

    @DisplayName("이름으로 공연장소를 반환한다.")
    @Test
    void getVenueByName() {
        String venueName = "Venue 1";
        Venue venue = venues.getFirst();
        VenueDto venueDto = venueList.getFirst();

        when(venueRepository.findByName(venueName)).thenReturn(Optional.of(venue));
        when(venueConverter.toDto(any(Venue.class))).thenReturn(venueDto);

        VenueDto result = venueService.getVenueByName(venueName);

        assertNotNull(result);
        assertEquals(venueName, result.getName());
        verify(venueRepository, times(1)).findByName(venueName);
    }

    @DisplayName("id로 공연장소를 반환한다.")
    @Test
    void getVenue() {
        Long venueId = 1L;
        Venue venue = venues.getFirst();
        VenueDto venueDto = venueList.getFirst();

        when(venueRepository.findById(venueId)).thenReturn(Optional.of(venue));
        when(venueConverter.toDto(venue)).thenReturn(venueDto);

        VenueDto result = venueService.getVenue(venueId);

        assertNotNull(result);
        assertEquals(venueId, result.getId());
        verify(venueRepository, times(1)).findById(venueId);
    }

    @DisplayName("공연장소를 생성한다.")
    @Test
    void createVenue() {
        VenueDto venueDto = venueList.getFirst();
        List<VenueSeat> venueSeats = Collections.emptyList();

        when(venueSeatRepository.findByVenueId(venueDto.getId())).thenReturn(venueSeats);
        when(venueConverter.toEntity(venueDto, venueSeats)).thenReturn(venues.getFirst());

        venueService.createVenue(venueDto);

        verify(venueSeatRepository, times(1)).findByVenueId(venueDto.getId());
        verify(venueRepository, times(1)).save(any(Venue.class));
    }

    @DisplayName("공연장소를 수정한다.")
    @Test
    void updateVenue() {
        Long venueId = 3L;
        VenueDto venueDto = VenueDto.builder().id(3L).name("Update Venue").build();
        Venue venue = Venue.builder().id(3L).name("Old Venue").build();
        List<VenueSeat> venueSeats = Collections.emptyList();

        when(venueRepository.findById(venueId)).thenReturn(Optional.of(venue));
        when(venueSeatRepository.findByVenueId(venueId)).thenReturn(venueSeats);
        when(venueConverter.toEntity(venueDto, venueSeats))
                .thenReturn(Venue.builder().id(3L).name("Update Venue").venueSeats(Collections.emptyList()).build());

        venueService.updateVenue(venueId, venueDto);

        verify(venueRepository, times(1)).findById(venueId);
        verify(venueSeatRepository, times(1)).findByVenueId(venueId);
        verify(venueRepository, times(1)).save(any(Venue.class));
    }

    @DisplayName("공연장소를 삭제한다.")
    @Test
    void deleteVenue() {
        Long venueId = 1L;
        Venue venue = mock(Venue.class);

        when(venueRepository.findById(venueId)).thenReturn(Optional.of(venue));

        venueService.deleteVenue(venueId);

        verify(venueRepository, times(1)).findById(venueId);
        verify(venueRepository, times(1)).delete(venue);
    }
}