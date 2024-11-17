package me.songha.concert.reservation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReservationResponse {
    private Object data;
    private String message;
}