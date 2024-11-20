package me.songha.concert.reservation.pending;

import lombok.RequiredArgsConstructor;
import me.songha.concert.common.CurrentUser;
import me.songha.concert.reservation.general.ReservationStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/reservation/pending")
@RequiredArgsConstructor
@RestController
public class ReservationPendingController {
    private final ReservationPendingProducer reservationPendingProducer;
    private final ReservationRequestStatusService reservationRequestStatusService;

    @PostMapping
    public ResponseEntity<ReservationPendingResponse> pendingReservation(@CurrentUser Long userId, @RequestBody ReservationPendingRequest request) {
        String requestId = "RES" + userId + "-" + System.currentTimeMillis();

        reservationRequestStatusService.saveStatus(requestId, ReservationStatus.PENDING.toString());
        reservationPendingProducer.sendToQueue(requestId, userId, request.getConcertId());

        ReservationPendingResponse response = new ReservationPendingResponse(
                requestId,
                "Reservation request added to the queue.");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/request-id/{requestId}/details")
    public ResponseEntity<ReservationDetailsResponse> getReservationDetails(@PathVariable String requestId) {
        String status = reservationRequestStatusService.getStatus(requestId);
        if (status == null) {
            return ResponseEntity.status(404).body(new ReservationDetailsResponse(null, "[Error] Request ID not found.", null));
        }
        String reservationId = reservationRequestStatusService.getReservationIdByRequestId(requestId);

        return ResponseEntity.ok(new ReservationDetailsResponse(requestId, status, reservationId));
    }

}
