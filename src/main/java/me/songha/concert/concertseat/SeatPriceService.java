package me.songha.concert.concertseat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Transactional
@RequiredArgsConstructor
@Service
public class SeatPriceService {
    private final SeatPriceRepository seatPriceRepository;

    public int getTotalAmount(Long concertId, Map<SeatGrade, Integer> tickets) {
        Map<String, Integer> prices = seatPriceRepository.findPricesByConcertIdAndGrades(concertId, SeatGrade.toList());

        int amount = 0;
        for (Map.Entry<String, Integer> entry : prices.entrySet()) {
            String grade = entry.getKey();
            int price = entry.getValue();
            amount += tickets.get(SeatGrade.fromString(grade)) * price;
        }
        return amount;
    }
}
