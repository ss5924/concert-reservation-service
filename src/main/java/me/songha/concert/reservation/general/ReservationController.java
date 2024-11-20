package me.songha.concert.reservation.general;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/reservation")
@RequiredArgsConstructor
@RestController
public class ReservationController {
    private final ReservationRepositoryService reservationRepositoryService;

    @GetMapping("/user-id/{userId}")
    public ResponseEntity<Page<ReservationDto>> getReservationsByUserId(@PathVariable Long userId,
                                                                        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Page<ReservationDto> reservations = reservationRepositoryService.getReservationsByUserId(userId, PageRequest.of(page, size));
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/concert-id/{concertId}")
    public ResponseEntity<Page<ReservationDto>> getReservationsByConcertId(@PathVariable Long concertId,
                                                                           @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Page<ReservationDto> reservations = reservationRepositoryService.getReservationsByConcertId(concertId, PageRequest.of(page, size));
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/user-id/{userId}/concert-id/{concertId}")
    public ResponseEntity<ReservationDto> getReservationByUserIdAndConcertId(@PathVariable Long userId, @PathVariable Long concertId) {
        ReservationDto reservationDto = reservationRepositoryService.getReservationByUserIdAndConcertId(userId, concertId);
        return ResponseEntity.ok(reservationDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDto> getReservation(@PathVariable Long id) {
        ReservationDto reservationDto = reservationRepositoryService.getReservation(id);
        return ResponseEntity.ok(reservationDto);
    }

    @PostMapping
    public ResponseEntity<Void> createReservation(@RequestBody ReservationDto reservationDto) {
        reservationRepositoryService.createReservation(reservationDto);
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationRepositoryService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}
