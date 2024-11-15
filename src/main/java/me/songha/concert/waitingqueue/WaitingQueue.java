package me.songha.concert.waitingqueue;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.songha.concert.common.BaseTimeEntity;
import me.songha.concert.concert.Concert;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Table(name = "WAITING_QUEUE")
@Entity
public class WaitingQueue extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private Integer waitingNumber;

    private Long userId;

    @ManyToOne
    @JoinColumn(name = "concert_id")
    private Concert concert;

    @Enumerated(EnumType.STRING)
    private WaitingStatus waitingStatus;

    private LocalDateTime enteredAt;

    private LocalDateTime completedAt;
}
