package me.songha.concert.reservation.progress;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ReservationProgressResponse {
    private List<String> seatNumbers;
    private String message;
}