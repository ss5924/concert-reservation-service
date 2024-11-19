package me.songha.concert.venue;

import jakarta.persistence.*;
import lombok.*;
import me.songha.concert.common.BaseTimeEntity;
import me.songha.concert.seat.Seat;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Table(name = "VENUE")
@Entity
public class Venue extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(unique = true)
    private String name;

    private Integer capacity;

    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL)
    private List<Seat> seats;

    @Builder
    public Venue(Long id, String name, Integer capacity, List<Seat> seats) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.seats = seats;
    }

    public void update(String name, Integer capacity, List<Seat> seats) {
        this.name = name;
        this.capacity = capacity;
        this.seats = seats;
    }
}
