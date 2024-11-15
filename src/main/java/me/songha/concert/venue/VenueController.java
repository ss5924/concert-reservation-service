package me.songha.concert.venue;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/venue")
@RequiredArgsConstructor
@RestController
public class VenueController {
    private final VenueService venueService;

    @GetMapping("/all")
    public ResponseEntity<Page<VenueDto>> getAllVenues(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Page<VenueDto> venues = venueService.getAllVenues(PageRequest.of(page, size));
        return ResponseEntity.ok(venues);
    }

    @GetMapping("/venue-name/{name}")
    public ResponseEntity<VenueDto> getVenueByName(@PathVariable String name) {
        VenueDto venueDto = venueService.getVenueByName(name);
        return ResponseEntity.ok(venueDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VenueDto> getVenue(@PathVariable Long id) {
        VenueDto venueDto = venueService.getVenue(id);
        return ResponseEntity.ok(venueDto);
    }

    @PostMapping
    public ResponseEntity<Void> createVenue(@RequestBody VenueDto venueDto) {
        venueService.createVenue(venueDto);
        return ResponseEntity.status(201).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateVenue(@PathVariable Long id, @RequestBody VenueDto venueDto) {
        venueService.updateVenue(id, venueDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVenue(@PathVariable Long id) {
        venueService.deleteVenue(id);
        return ResponseEntity.noContent().build();
    }
}
