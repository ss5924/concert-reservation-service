package me.songha.concert.reservationhistory;

import jakarta.persistence.*;
import lombok.*;
import me.songha.concert.common.BaseTimeEntity;
import me.songha.concert.reservation.Reservation;
import me.songha.concert.reservation.ReservationStatus;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Table(name = "RESERVATION_HISTORY")
@Entity
public class ReservationHistory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    private Integer amount;

    @Builder
    public ReservationHistory(Long id, Long userId, Reservation reservation, ReservationStatus status, Integer amount) {
        this.id = id;
        this.userId = userId;
        this.reservation = reservation;
        this.status = status;
        this.amount = amount;
    }
}