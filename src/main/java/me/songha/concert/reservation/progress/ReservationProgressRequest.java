package me.songha.concert.reservation.progress;

import lombok.Data;

import java.util.List;

@Data
public class ReservationProgressRequest {
    private Long reservationId;
    private Long concertId;
    private List<String> seatNumbers;
}
