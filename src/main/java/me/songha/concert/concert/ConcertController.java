package me.songha.concert.concert;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/concert")
@RestController
public class ConcertController {
    private final ConcertRepositoryService concertRepositoryService;

    @GetMapping("/all")
    public ResponseEntity<Page<ConcertDto>> getAllConcerts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Page<ConcertDto> concerts = concertRepositoryService.getAllConcerts(PageRequest.of(page, size));
        return ResponseEntity.ok(concerts);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ConcertDto> getConcert(@PathVariable Long id) {
        ConcertDto concert = concertRepositoryService.getConcert(id);
        return ResponseEntity.ok(concert);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Void> createConcert(@RequestBody ConcertDto concertDto) {
        concertRepositoryService.createConcert(concertDto);
        return ResponseEntity.status(201).build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/id/{id}")
    public ResponseEntity<Void> updateConcert(@PathVariable Long id, @RequestBody ConcertDto concertDto) {
        concertRepositoryService.updateConcert(id, concertDto);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deleteConcert(@PathVariable Long id) {
        concertRepositoryService.deleteConcert(id);
        return ResponseEntity.noContent().build();
    }

}
