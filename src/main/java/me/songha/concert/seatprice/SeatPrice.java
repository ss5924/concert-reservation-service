package me.songha.concert.seatprice;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.songha.concert.common.BaseTimeEntity;
import me.songha.concert.concert.Concert;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Table(name = "SEAT_PRICE")
@Entity
public class SeatPrice extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "concert_id")
    private Concert concert;

    @Enumerated(EnumType.STRING)
    private SeatGrade grade;

    private Integer price;
}