package me.songha.concert.reservation.pending;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationPendingProducerRequest {
    private Long concertId;
    private Long userId;
    private String requestId;
}
