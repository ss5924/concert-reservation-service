package me.songha.concert.concertseat;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.songha.concert.common.BaseTimeEntity;
import me.songha.concert.concert.Concert;
import me.songha.concert.venueseat.VenueSeat;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Table(name = "CONCERT_SEAT")
@Entity
public class ConcertSeat extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @OneToOne
    @JoinColumn(name = "venue_seat_id")
    private VenueSeat venueSeat;

    @ManyToOne
    @JoinColumn(name = "concert_id")
    private Concert concert;

    @Enumerated(EnumType.STRING)
    private ConcertSeatGrade grade;

    private Integer price;
}