package me.songha.concert.concert;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConcertDto {
    private Long id;
    private String title;
    private String description;
    private String venueName;
    private LocalDateTime concertDate;
    private Integer runningTime;
    private LocalDateTime salesStartAt;
    private LocalDateTime salesEndAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public ConcertDto(Long id, String title, String description, String venueName,
                      LocalDateTime concertDate, Integer runningTime, LocalDateTime salesStartAt, LocalDateTime salesEndAt,
                      LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.venueName = venueName;
        this.concertDate = concertDate;
        this.runningTime = runningTime;
        this.salesStartAt = salesStartAt;
        this.salesEndAt = salesEndAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
