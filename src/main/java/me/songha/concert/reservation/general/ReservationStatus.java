package me.songha.concert.reservation.general;

public enum ReservationStatus {
    PENDING, PROCESSING, CONFIRMED, CANCELED, REJECTED;

    public static ReservationStatus fromString(String status) {
        return switch (status) {
            case "PENDING" -> PENDING;
            case "PROCESSING" -> PROCESSING;
            case "CONFIRMED" -> CONFIRMED;
            case "CANCELED" -> CANCELED;
            case "REJECTED" -> REJECTED;
            default -> throw new IllegalArgumentException("Invalid reservation status: " + status);
        };
    }
}
