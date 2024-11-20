package me.songha.concert.reservation.history;

import lombok.Builder;
import lombok.Data;

@Data
public class ReservationHistoryDto {
    private Long id;
    private Long reservationId;
    private Long userId;
    private String reservationStatus;
    private Integer amount;

    @Builder
    public ReservationHistoryDto(Long id, Long reservationId, Long userId, String reservationStatus, Integer amount) {
        this.id = id;
        this.reservationId = reservationId;
        this.userId = userId;
        this.reservationStatus = reservationStatus;
        this.amount = amount;
    }
}
