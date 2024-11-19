package me.songha.concert.reservation.progress;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.songha.concert.user.CurrentUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/reservation/progress")
@RequiredArgsConstructor
@RestController
public class ReservationProgressController {
    private final ReservationProgressService reservationProgressService;

    @PatchMapping
    public ResponseEntity<ReservationProgressResponse> progressReservation(
            @CurrentUser Long userId, @RequestBody ReservationProgressRequest request) {
        reservationProgressService.progressReservation(request.getReservationId(), userId, request.getSeatNumbers(), request.getConcertId());
        ReservationProgressResponse response = new ReservationProgressResponse(
                request.getSeatNumbers(),
                "Reservation processed successfully.");
        return ResponseEntity.ok(response);
    }

}
