package me.songha.concert.reservation;

import me.songha.concert.concert.Concert;
import me.songha.concert.concert.ConcertNotFoundException;
import me.songha.concert.concert.ConcertRepository;
import me.songha.concert.reservation.general.*;
import me.songha.concert.reservation.seat.ReservationSeat;
import me.songha.concert.reservation.seat.ReservationSeatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ReservationRepositoryServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationConverter reservationConverter;

    @Mock
    private ConcertRepository concertRepository;

    @Mock
    private ReservationSeatRepository reservationSeatRepository;

    @InjectMocks
    private ReservationRepositoryService reservationRepositoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void 예매_조회_성공() {
        Long reservationId = 1L;
        Reservation reservation = mock(Reservation.class);
        ReservationDto reservationDto = mock(ReservationDto.class);

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(reservationConverter.toDto(reservation)).thenReturn(reservationDto);

        ReservationDto result = reservationRepositoryService.getReservation(reservationId);

        assertNotNull(result);
        verify(reservationRepository, times(1)).findById(reservationId);
        verify(reservationConverter, times(1)).toDto(reservation);
    }

    @DisplayName("없는 예매 조회시 예외가 발생한다.")
    @Test
    void getReservation_ThrowReservationNotFoundException() {
        Long reservationId = 1L;

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        assertThrows(ReservationNotFoundException.class, () -> reservationRepositoryService.getReservation(reservationId));
        verify(reservationRepository, times(1)).findById(reservationId);
    }

    @DisplayName("예매를 저장한다.")
    @Test
    void createReservation() {
        ReservationDto reservationDto = mock(ReservationDto.class);
        Concert concert = mock(Concert.class);
        List<ReservationSeat> reservationSeats = List.of(mock(ReservationSeat.class));
        Reservation reservation = mock(Reservation.class);

        when(concertRepository.findById(reservationDto.getConcertId())).thenReturn(Optional.of(concert));
        when(reservationSeatRepository.findByReservation(reservationDto.getId())).thenReturn(reservationSeats);
        when(reservationConverter.toEntity(reservationDto, concert, reservationSeats)).thenReturn(reservation);

        reservationRepositoryService.createReservation(reservationDto);

        verify(concertRepository, times(1)).findById(reservationDto.getConcertId());
        verify(reservationSeatRepository, times(1)).findByReservation(reservationDto.getId());
        verify(reservationConverter, times(1)).toEntity(reservationDto, concert, reservationSeats);
        verify(reservationRepository, times(1)).save(reservation);
    }

    @DisplayName("예매를 저장할 때 concert가 없으면 예외가 발생한다.")
    @Test
    void createReservation_ThrowConcertNotFoundException() {
        ReservationDto reservationDto = mock(ReservationDto.class);

        when(concertRepository.findById(reservationDto.getConcertId())).thenReturn(Optional.empty());

        assertThrows(ConcertNotFoundException.class, () -> reservationRepositoryService.createReservation(reservationDto));
        verify(concertRepository, times(1)).findById(reservationDto.getConcertId());
        verifyNoInteractions(reservationSeatRepository);
        verifyNoInteractions(reservationRepository);
    }

    @DisplayName("예매를 삭제한다.")
    @Test
    void deleteReservation() {
        Long reservationId = 1L;
        Reservation reservation = mock(Reservation.class);

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        reservationRepositoryService.deleteReservation(reservationId);

        verify(reservationRepository, times(1)).findById(reservationId);
        verify(reservationRepository, times(1)).delete(reservation);
    }

    @DisplayName("예매를 삭제할 때 예매가 없다면 예외가 발생한다.")
    @Test
    void deleteReservation_ThrowReservationNotFoundException() {
        Long reservationId = 1L;

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        assertThrows(ReservationNotFoundException.class, () -> reservationRepositoryService.deleteReservation(reservationId));
        verify(reservationRepository, times(1)).findById(reservationId);
        verify(reservationRepository, never()).delete(any());
    }
}