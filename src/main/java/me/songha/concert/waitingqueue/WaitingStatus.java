package me.songha.concert.waitingqueue;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum WaitingStatus {
    WAITING("대기중"),
    IN_PROGRESS("진행중"),
    COMPLETED("완료"),
    EXPIRED("만료");

    private final String description;
}