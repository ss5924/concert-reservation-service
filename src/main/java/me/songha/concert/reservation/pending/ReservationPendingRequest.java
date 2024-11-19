package me.songha.concert.reservation.pending;

import lombok.Data;

@Data
public class ReservationPendingRequest {
    private Long userId;
    private Long concertId;
    private String requestId;
}