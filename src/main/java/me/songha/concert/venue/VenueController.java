package me.songha.concert.venue;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/venue")
@RequiredArgsConstructor
@RestController
public class VenueController {
    private final VenueRepositoryService venueRepositoryService;

    @GetMapping("/all")
    public ResponseEntity<Page<VenueDto>> getAllVenues(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Page<VenueDto> venues = venueRepositoryService.getAllVenues(PageRequest.of(page, size));
        return ResponseEntity.ok(venues);
    }

    @GetMapping("/venue-name/{name}")
    public ResponseEntity<VenueDto> getVenueByName(@PathVariable String name) {
        VenueDto venueDto = venueRepositoryService.getVenueByName(name);
        return ResponseEntity.ok(venueDto);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<VenueDto> getVenue(@PathVariable Long id) {
        VenueDto venueDto = venueRepositoryService.getVenue(id);
        return ResponseEntity.ok(venueDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Void> createVenue(@RequestBody VenueDto venueDto) {
        venueRepositoryService.createVenue(venueDto);
        return ResponseEntity.status(201).build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/id/{id}")
    public ResponseEntity<Void> updateVenue(@PathVariable Long id, @RequestBody VenueDto venueDto) {
        venueRepositoryService.updateVenue(id, venueDto);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deleteVenue(@PathVariable Long id) {
        venueRepositoryService.deleteVenue(id);
        return ResponseEntity.noContent().build();
    }
}
