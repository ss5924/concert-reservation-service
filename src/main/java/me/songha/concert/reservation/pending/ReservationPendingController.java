package me.songha.concert.reservation.pending;

import lombok.RequiredArgsConstructor;
import me.songha.concert.reservation.ReservationStatus;
import me.songha.concert.user.CurrentUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/reservation/pending")
@RequiredArgsConstructor
@RestController
public class ReservationPendingController {
    private final ReservationPendingProducer reservationPendingProducer;
    private final ReservationRequestStatusService reservationRequestStatusService;

    @PostMapping
    public ResponseEntity<String> pendingReservation(@CurrentUser Long userId, @RequestBody ReservationPendingRequest request) {
        String requestId = userId + "-" + System.currentTimeMillis();
        request.setUserId(userId);
        request.setRequestId(requestId);

        reservationRequestStatusService.saveStatus(requestId, ReservationStatus.PENDING.toString());
        reservationPendingProducer.sendToQueue(requestId, request);

        return ResponseEntity.ok("Reservation request added to the queue. Request ID: " + requestId);
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
