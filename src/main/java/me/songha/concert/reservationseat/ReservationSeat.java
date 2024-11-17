package me.songha.concert.reservationseat;

import jakarta.persistence.*;
import lombok.*;
import me.songha.concert.common.BaseTimeEntity;
import me.songha.concert.reservation.Reservation;
import me.songha.concert.venueseat.VenueSeat;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Table(name = "RESERVATION_SEAT")
@Entity
public class ReservationSeat extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @ManyToOne
    @JoinColumn(name = "venue_seat_id")
    private VenueSeat venueSeat;

    private Integer price;

    @Builder
    public ReservationSeat(Long id, Reservation reservation, VenueSeat venueSeat, Integer price) {
        this.id = id;
        this.reservation = reservation;
        this.venueSeat = venueSeat;
        this.price = price;
    }
}
