package me.songha.concert.venue;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface VenueRepository extends JpaRepository<Venue, Long> {
    @Query("select v from Venue v where v.name = :name")
    Optional<Venue> findByName(String name);
}
