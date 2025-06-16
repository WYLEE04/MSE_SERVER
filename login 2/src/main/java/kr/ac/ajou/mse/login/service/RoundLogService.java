package kr.ac.ajou.mse.login.service;

import kr.ac.ajou.mse.login.model.*;
import kr.ac.ajou.mse.login.model.Character;
import kr.ac.ajou.mse.login.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
public class RoundLogService {

    private final RoundLogRepository roundLogRepository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final CardRepository cardRepository;  

    public RoundLogService(RoundLogRepository roundLogRepository,
                           GameRepository gameRepository,
                           UserRepository userRepository,
                           CardRepository cardRepository) {
        this.roundLogRepository = roundLogRepository;
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.cardRepository = cardRepository;
    }

    public void addRound(Long gameId, int roundNo,
                         Faction p1Faction, Character p1Character,
                         Faction p2Faction, Character p2Character,
                         String winnerUsername,
                         List<Long> p1CardIds, List<Long> p2CardIds) {

        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));
        User winner = userRepository.findByUsername(winnerUsername);

        RoundLog roundLog = new RoundLog();
        roundLog.setGame(game);
        roundLog.setRoundNo(roundNo);
        roundLog.setP1Faction(p1Faction);
        roundLog.setP1Character(p1Character);
        roundLog.setP2Faction(p2Faction);
        roundLog.setP2Character(p2Character);
        roundLog.setRoundWinner(winner);

        
        List<Card> p1Cards = new ArrayList<>();
        for (Long cardId : p1CardIds) {
            Card card = cardRepository.findById(cardId)
                    .orElseThrow(() -> new RuntimeException("Card not found: id=" + cardId));
            p1Cards.add(card);
        }

        List<Card> p2Cards = new ArrayList<>();
        for (Long cardId : p2CardIds) {
            Card card = cardRepository.findById(cardId)
                    .orElseThrow(() -> new RuntimeException("Card not found: id=" + cardId));
            p2Cards.add(card);
        }

        roundLog.setP1Cards(p1Cards);
        roundLog.setP2Cards(p2Cards);

        roundLogRepository.save(roundLog);

        
        if (game.getPlayer1().getUsername().equals(winnerUsername)) {
            game.setPlayer1Wins(game.getPlayer1Wins() + 1);
        } else if (game.getPlayer2().getUsername().equals(winnerUsername)) {
            game.setPlayer2Wins(game.getPlayer2Wins() + 1);
        }

        gameRepository.save(game);
    }

     
    @Transactional(readOnly = true)
    public List<RoundLog> getReplay(Long gameId) {
        return roundLogRepository.findByGameIdOrderByRoundNoAsc(gameId);
    }
}
