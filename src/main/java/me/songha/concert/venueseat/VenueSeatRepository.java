package me.songha.concert.venueseat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VenueSeatRepository extends JpaRepository<VenueSeat, Long> {
    @Query("select vs from VenueSeat vs where vs.venue.id = :venueId")
    List<VenueSeat> findByVenueId(Long venueId);
}
