package me.songha.concert.concert;

import me.songha.concert.venue.Venue;
import me.songha.concert.venue.VenueNotFoundException;
import me.songha.concert.venue.VenueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConcertServiceTest {
    @Mock
    private ConcertRepository concertRepository;

    @Mock
    private VenueRepository venueRepository;

    @Mock
    private ConcertConverter concertConverter;

    @InjectMocks
    private ConcertService concertService;

    private List<ConcertDto> concertList;

    private List<Concert> concerts;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        concertList = Arrays.asList(
                ConcertDto.builder()
                        .id(1L)
                        .title("Title 1")
                        .description("Description 1")
                        .createdAt(LocalDateTime.now()).build(),
                ConcertDto.builder()
                        .id(2L)
                        .title("Title 2")
                        .description("Description 2")
                        .createdAt(LocalDateTime.now()).build()
        );

        concerts = Arrays.asList(
                Concert.builder()
                        .id(1L)
                        .title("Title 1")
                        .description("Description 1").build(),
                Concert.builder()
                        .id(2L)
                        .title("Title 2")
                        .description("Description 2").build()
        );
    }

    @Test
    public void testGetAllConcerts() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<Concert> concertPage = new PageImpl<>(concerts);
        when(concertRepository.findAll(pageable)).thenReturn(concertPage);

        when(concertConverter.toDto(concerts.get(0))).thenReturn(concertList.get(0));
        when(concertConverter.toDto(concerts.get(1))).thenReturn(concertList.get(1));

        Page<ConcertDto> result = concertService.getAllConcerts(pageable);

        assertEquals(2, result.getContent().size());
        assertEquals("Title 1", result.getContent().get(0).getTitle());
        assertEquals("Title 2", result.getContent().get(1).getTitle());

        Mockito.verify(concertRepository, Mockito.times(1)).findAll(pageable);
        Mockito.verify(concertConverter, Mockito.times(2)).toDto(Mockito.any(Concert.class));
    }

    @DisplayName("id로 콘서트를 반환한다.")
    @Test
    void getConcert() {
        Long concertId = 1L;
        Concert concert = mock(Concert.class);
        ConcertDto concertDto = concertList.getFirst();
        when(concertRepository.findById(concertId)).thenReturn(Optional.of(concert));
        when(concertConverter.toDto(concert)).thenReturn(concertDto);

        ConcertDto result = concertService.getConcert(concertId);

        assertNotNull(result);
        assertEquals("Title 1", result.getTitle());
        verify(concertRepository, times(1)).findById(concertId);
    }

    @DisplayName("id로 콘서트를 반환할 때 존재하지 않으면 예외가 발생한다.")
    @Test
    void getConcert_ThrowExceptionWhenNotFound() {
        Long concertId = 1L;
        when(concertRepository.findById(concertId)).thenReturn(Optional.empty());

        assertThrows(ConcertNotFoundException.class, () -> concertService.getConcert(concertId));
        verify(concertRepository, times(1)).findById(concertId);
    }

    @DisplayName("콘서트를 저장한다.")
    @Test
    void saveConcert() {
        Venue venue = mock(Venue.class);
        ConcertDto concertDto = ConcertDto.builder()
                .title("Title 3")
                .description("Description 3")
                .build();
        Concert concert = mock(Concert.class);

        when(venueRepository.findByName(concertDto.getVenueName())).thenReturn(Optional.of(venue));
        when(concertConverter.toEntity(concertDto, venue)).thenReturn(concert);

        concertService.createConcert(concertDto);

        verify(venueRepository, times(1)).findByName(concertDto.getVenueName());
        verify(concertConverter, times(1)).toEntity(concertDto, venue);
        verify(concertRepository, times(1)).save(concert);
    }

    @DisplayName("콘서트를 저장할 때 venue가 없으면 예외가 발생한다.")
    @Test
    void saveConcert_ThrowExceptionWhenVenueNotFound() {
        ConcertDto concertDto = ConcertDto.builder()
                .title("Title 4")
                .description("Description 4")
                .venueName("invalid venue")
                .build();
        when(venueRepository.findByName(concertDto.getVenueName())).thenReturn(Optional.empty());

        assertThrows(VenueNotFoundException.class, () -> concertService.createConcert(concertDto));
        verify(venueRepository, times(1)).findByName(concertDto.getVenueName());
        verifyNoInteractions(concertRepository);
    }

    @DisplayName("id로 콘서트를 삭제한다.")
    @Test
    void deleteConcert() {
        Long concertId = 1L;
        Concert concert = mock(Concert.class);
        when(concertRepository.findById(concertId)).thenReturn(Optional.of(concert));

        concertService.deleteConcert(concertId);

        verify(concertRepository, times(1)).findById(concertId);
        verify(concertRepository, times(1)).delete(concert);
    }

    @DisplayName("id로 콘서트를 삭제할 때 존재하지 않으면 예외가 발생한다.")
    @Test
    void deleteConcert_ThrowExceptionWhenNotFound() {
        Long concertId = 1L;
        when(concertRepository.findById(concertId)).thenReturn(Optional.empty());

        assertThrows(ConcertNotFoundException.class, () -> concertService.deleteConcert(concertId));

        verify(concertRepository, times(1)).findById(concertId);
        verify(concertRepository, never()).delete(any());
    }
}