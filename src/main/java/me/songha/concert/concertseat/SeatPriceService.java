package me.songha.concert.concertseat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
@RequiredArgsConstructor
@Service
public class SeatPriceService {
    private final SeatPriceRepository seatPriceRepository;

    public int getTotalAmount(Long concertId, Map<SeatGrade, Integer> tickets) {
        List<SeatPrice> prices = seatPriceRepository.findByConcertId(concertId);

        int amount = 0;
        for(Map.Entry<SeatGrade, Integer> entry : tickets.entrySet()) {
            SeatGrade grade = entry.getKey();
            int price = prices.stream().filter(p->p.getGrade().equals(grade))
                    .findFirst().orElseThrow(() -> new IllegalArgumentException("Cannot found price.")).getPrice();
            amount += tickets.get(grade) * price;
        }

        return amount;
    }
}
