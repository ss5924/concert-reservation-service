package me.songha.concert.reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/reservation-process")
@RequiredArgsConstructor
@RestController
public class ReservationProgressController {
    private final ReservationFlowService reservationFlowService;

    @PostMapping("/pending")
    public ResponseEntity<ReservationResponse> pendingReservation(@RequestBody ReservationDto reservationDto) {
        Long reservationId = reservationFlowService.pendingReservation(reservationDto);
        ReservationResponse response = new ReservationResponse(reservationId, "Reservation pending successfully.");
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/progress")
    public ResponseEntity<ReservationResponse> updateReservation(@RequestBody ReservationProgressRequest request) {
        String reservationNumber = reservationFlowService.progressReservation(
                request.getReservationId(),
                request.getUserId(),
                request.getSeatNumbers(),
                request.getConcertId()
        );
        ReservationResponse response = new ReservationResponse(reservationNumber, "Reservation processed successfully.");
        return ResponseEntity.ok(response);
    }

}
