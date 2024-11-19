package me.songha.concert.reservation.preoccupy;

import lombok.Data;

import java.util.List;

@Data
public class PreoccupySeatRequest {
    private Long concertId;
    private Long userId;
    private List<String> seatNumbers;
}
