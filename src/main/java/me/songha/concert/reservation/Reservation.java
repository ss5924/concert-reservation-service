package me.songha.concert.reservation;

import jakarta.persistence.*;
import lombok.*;
import me.songha.concert.common.BaseTimeEntity;
import me.songha.concert.concert.Concert;
import me.songha.concert.reservationseat.ReservationSeat;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Table(name = "RESERVATION")
@Entity
public class Reservation extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private Long userId;

    @OneToOne
    @JoinColumn(name = "concert_id")
    private Concert concert;

    private Integer totalAmount;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL)
    private List<ReservationSeat> reservationSeats;

    @Builder
    public Reservation(Long id, Long userId, Concert concert, Integer totalAmount, List<ReservationSeat> reservationSeats) {
        this.id = id;
        this.userId = userId;
        this.concert = concert;
        this.totalAmount = totalAmount;
        this.reservationSeats = reservationSeats;
    }

    public void update(Long userId, Concert concert, Integer totalAmount, List<ReservationSeat> reservationSeats) {
        this.userId = userId;
        this.concert = concert;
        this.totalAmount = totalAmount;
        this.reservationSeats = reservationSeats;
    }
}
