package me.songha.concert.reservation;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReservationDto {
    private Long id;
    private Long userId;
    private Long concertId;
    private String concertTitle;
    private Integer totalAmount;
    private String reservationStatus;
    private List<String> seatNumbers;
    private String reservationNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public ReservationDto(Long id, Long userId, Long concertId, String concertTitle, Integer totalAmount,
                          String reservationStatus, List<String> seatNumbers, String reservationNumber,
                          LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.concertId = concertId;
        this.concertTitle = concertTitle;
        this.totalAmount = totalAmount;
        this.reservationStatus = reservationStatus;
        this.seatNumbers = seatNumbers;
        this.reservationNumber = reservationNumber;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
