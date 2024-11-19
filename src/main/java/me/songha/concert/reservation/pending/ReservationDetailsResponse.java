package me.songha.concert.reservation.pending;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ReservationDetailsResponse {
    private String requestId;
    private String status;
    private String reservationId;
}
