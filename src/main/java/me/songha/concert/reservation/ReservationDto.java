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
    private List<String> seatNumbers;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public ReservationDto(Long id, Long userId, Long concertId, String concertTitle, Integer totalAmount,
                          List<String> seatNumbers, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.concertId = concertId;
        this.concertTitle = concertTitle;
        this.totalAmount = totalAmount;
        this.seatNumbers = seatNumbers;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
