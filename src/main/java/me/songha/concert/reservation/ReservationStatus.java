package me.songha.concert.reservation;

public enum ReservationStatus {
    PENDING, CONFIRMED, CANCELED, REJECTED;

    public static ReservationStatus fromString(String status) {
        return switch (status) {
            case "PENDING" -> PENDING;
            case "CONFIRMED" -> CONFIRMED;
            case "CANCELED" -> CANCELED;
            case "REJECTED" -> REJECTED;
            default -> throw new IllegalArgumentException("Invalid reservation status: " + status);
        };
    }
}
