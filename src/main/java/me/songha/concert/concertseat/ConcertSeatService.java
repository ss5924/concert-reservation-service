package me.songha.concert.concertseat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class ConcertSeatService {
    private final ConcertSeatRepository concertSeatRepository;

    public int getTotalAmount(Long concertId, List<String> seatNumbers) {
        return concertSeatRepository.findAmountByConcertIdAndSeatNumbers(concertId, seatNumbers);
    }
}
