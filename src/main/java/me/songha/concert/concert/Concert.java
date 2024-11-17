package me.songha.concert.concert;

import jakarta.persistence.*;
import lombok.*;
import me.songha.concert.common.BaseTimeEntity;
import me.songha.concert.concertseat.ConcertSeat;
import me.songha.concert.venue.Venue;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Table(name = "CONCERT")
@Entity
public class Concert extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String title;

    private String description;

    @OneToOne
    @JoinColumn(name = "venue_id")
    private Venue venue;

    @OneToMany(mappedBy = "concert")
    private List<ConcertSeat> concertSeats;

    private LocalDateTime concertDate;

    private Integer runningTime;

    private LocalDateTime salesStartAt;

    private LocalDateTime salesEndAt;

    @Builder
    public Concert(Long id, String title, String description, Venue venue, List<ConcertSeat> concertSeats,
                   LocalDateTime concertDate, Integer runningTime, LocalDateTime salesStartAt, LocalDateTime salesEndAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.venue = venue;
        this.concertSeats = concertSeats;
        this.concertDate = concertDate;
        this.runningTime = runningTime;
        this.salesStartAt = salesStartAt;
        this.salesEndAt = salesEndAt;
    }

    public void update(String title, String description, Venue venue, List<ConcertSeat> concertSeats,
                       LocalDateTime concertDate, int runningTime) {
        this.title = title;
        this.description = description;
        this.venue = venue;
        this.concertSeats = concertSeats;
        this.concertDate = concertDate;
        this.runningTime = runningTime;
    }
}
