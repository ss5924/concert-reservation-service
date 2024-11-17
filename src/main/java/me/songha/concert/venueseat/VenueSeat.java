package me.songha.concert.venueseat;

import jakarta.persistence.*;
import lombok.*;
import me.songha.concert.common.BaseTimeEntity;
import me.songha.concert.venue.Venue;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Table(name = "VENUE_SEAT")
@Entity
public class VenueSeat extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String seatNumber;

    @ManyToOne
    @JoinColumn(name = "venue_id")
    private Venue venue;

    @Builder
    public VenueSeat(Long id, String seatNumber, Venue venue) {
        this.id = id;
        this.seatNumber = seatNumber;
        this.venue = venue;
    }
}
