package me.songha.concert.reservation.general;

import jakarta.persistence.*;
import lombok.*;
import me.songha.concert.common.BaseTimeEntity;
import me.songha.concert.concert.Concert;
import me.songha.concert.reservation.seat.ReservationSeat;

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

    @ManyToOne
    @JoinColumn(name = "concert_id")
    private Concert concert;

    private Integer totalAmount;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @OneToMany(mappedBy = "reservation")
    private List<ReservationSeat> reservationSeats;

    private String reservationNumber;

    @Builder
    public Reservation(Long id, Long userId, Concert concert, Integer totalAmount,
                       ReservationStatus status, List<ReservationSeat> reservationSeats, String reservationNumber) {
        this.id = id;
        this.userId = userId;
        this.concert = concert;
        this.totalAmount = totalAmount;
        this.status = status;
        this.reservationSeats = reservationSeats;
        this.reservationNumber = reservationNumber;
    }

    public void update(Integer totalAmount, ReservationStatus status, String reservationNumber) {
        this.totalAmount = totalAmount;
        this.status = status;
        this.reservationNumber = reservationNumber;
    }

    public void updateStatus(ReservationStatus status) {
        this.status = status;
    }
}
