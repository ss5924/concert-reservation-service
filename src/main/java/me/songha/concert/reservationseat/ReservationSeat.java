package me.songha.concert.reservationseat;

import jakarta.persistence.*;
import lombok.*;
import me.songha.concert.common.BaseTimeEntity;
import me.songha.concert.reservation.Reservation;
import me.songha.concert.seat.Seat;

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
    private Seat seat;

    private Integer price;

    @Builder
    public ReservationSeat(Long id, Reservation reservation, Seat seat, Integer price) {
        this.id = id;
        this.reservation = reservation;
        this.seat = seat;
        this.price = price;
    }
}
