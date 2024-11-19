package me.songha.concert.reservation.preoccupy;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PreoccupySeatsResponse {
    private Long concertId;
    private Long userId;
    private List<String> seatNumbers;
    private String message;
}