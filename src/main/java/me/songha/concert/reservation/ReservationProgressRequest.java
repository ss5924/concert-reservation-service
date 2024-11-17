package me.songha.concert.reservation;

import lombok.Data;

import java.util.List;

@Data
public class ReservationProgressRequest {
    private Long reservationId;
    private Long userId;
    private List<String> seatNumbers;
    private Long concertId;
}