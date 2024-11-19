package me.songha.concert.concertseat;

import java.util.Arrays;
import java.util.List;

public enum SeatGrade {
    VIP, R, S, A, B;

    public static SeatGrade fromString(String status) {
        return switch (status) {
            case "VIP" -> VIP;
            case "R" -> R;
            case "S" -> S;
            case "A" -> A;
            case "B" -> B;
            default -> throw new IllegalArgumentException("Invalid reservation status: " + status);
        };
    }

    public static List<SeatGrade> toList() {
        return Arrays.asList(SeatGrade.values());
    }
}