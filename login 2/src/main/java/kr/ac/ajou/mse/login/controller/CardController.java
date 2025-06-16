package kr.ac.ajou.mse.login.controller;

import kr.ac.ajou.mse.login.model.Card;
import kr.ac.ajou.mse.login.repository.CardRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/card")
public class CardController {

    private final CardRepository cardRepository;

    public CardController(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @PostMapping
    public Card createCard(@RequestBody Map<String, String> body) {
        String name = body.get("name");
        return cardRepository.save(new Card(name));
    }

    @GetMapping
    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }
}
