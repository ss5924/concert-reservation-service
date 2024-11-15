package me.songha.concert.venue;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class VenueDto {
    private Long id;
    private String name;
    private Integer capacity;
    private List<String> seatNumbers;

    @Builder
    public VenueDto(Long id, String name, Integer capacity, List<String> seatNumbers) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.seatNumbers = seatNumbers;
    }
}
