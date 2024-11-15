package me.songha.concert.reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/reservation")
@RequiredArgsConstructor
@RestController
public class ReservationController {
    private final ReservationService reservationService;

    @GetMapping("/user-id/{userId}")
    public ResponseEntity<Page<ReservationDto>> getReservationsByUserId(@PathVariable Long userId,
                                                                        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Page<ReservationDto> reservations = reservationService.getReservationsByUserId(userId, PageRequest.of(page, size));
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/concert-id/{concertId}")
    public ResponseEntity<Page<ReservationDto>> getReservationsByConcertId(@PathVariable Long concertId,
                                                                           @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Page<ReservationDto> reservations = reservationService.getReservationsByConcertId(concertId, PageRequest.of(page, size));
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/user-id/{userId}/concert-id/{concertId}")
    public ResponseEntity<ReservationDto> getReservationByUserIdAndConcertId(@PathVariable Long userId, @PathVariable Long concertId) {
        ReservationDto reservationDto = reservationService.getReservationByUserIdAndConcertId(userId, concertId);
        return ResponseEntity.ok(reservationDto);
    }

    @GetMapping("{id}")
    public ResponseEntity<ReservationDto> getReservation(@PathVariable Long id) {
        ReservationDto reservationDto = reservationService.getReservation(id);
        return ResponseEntity.ok(reservationDto);
    }

    @PostMapping
    public ResponseEntity<Void> createReservation(@RequestBody ReservationDto reservationDto) {
        reservationService.createReservation(reservationDto);
        return ResponseEntity.status(201).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateReservation(@PathVariable Long id, @RequestBody ReservationDto reservationDto) {
        reservationService.updateReservation(id, reservationDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}
