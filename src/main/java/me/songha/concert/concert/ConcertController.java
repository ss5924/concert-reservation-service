package me.songha.concert.concert;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/concert")
@RestController
public class ConcertController {
    private final ConcertService concertService;

    @GetMapping("/all")
    public ResponseEntity<Page<ConcertDto>> getAllConcerts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Page<ConcertDto> concerts = concertService.getAllConcerts(PageRequest.of(page, size));
        return ResponseEntity.ok(concerts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConcertDto> getConcert(@PathVariable Long id) {
        ConcertDto concert = concertService.getConcert(id);
        return ResponseEntity.ok(concert);
    }

    @PostMapping
    public ResponseEntity<Void> createConcert(@RequestBody ConcertDto concertDto) {
        concertService.createConcert(concertDto);
        return ResponseEntity.status(201).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateConcert(@PathVariable Long id, @RequestBody ConcertDto concertDto) {
        concertService.updateConcert(id, concertDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConcert(@PathVariable Long id) {
        concertService.deleteConcert(id);
        return ResponseEntity.noContent().build();
    }

}
