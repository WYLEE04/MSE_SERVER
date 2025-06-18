package kr.ac.ajou.mse.login.service;

import kr.ac.ajou.mse.login.model.Game;
import kr.ac.ajou.mse.login.model.User;
import kr.ac.ajou.mse.login.repository.GameRepository;
import kr.ac.ajou.mse.login.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GameService {

    private final GameRepository gameRepository;
    private final UserRepository userRepository;

    public GameService(GameRepository gameRepository, UserRepository userRepository) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
    }
    public Long createGame(String player1Name, String player2Name) {

        User player1 = userRepository.findByUsername(player1Name);
        User player2 = userRepository.findByUsername(player2Name);
        Game game = new Game();
        game.setPlayer1(player1);
        game.setPlayer2(player2);
        
        return gameRepository.save(game).getId();
    }

    public void finishGame(Long gameId, String winnerName) {
        Game game = gameRepository.findById(gameId).orElseThrow();
        if (game.isFinished()) {
            System.out.println("Game already finished."); 
            return;
        }
        
        User winner = userRepository.findByUsername(winnerName);
        game.setWinner(winner);
        game.setFinished(true);
        User player1 = game.getPlayer1();
        User player2 = game.getPlayer2();
        
        
        if (player1.getUsername().equals(winnerName)) {
            game.setPlayer1Wins(1);
            game.setPlayer2Wins(0);
        } else {
            game.setPlayer1Wins(0);
            game.setPlayer2Wins(1);
        }
        
        //Score Update
        User loser;
        if (player1.getUsername().equals(winnerName)) {
            loser = player2;
        } else {
            loser = player1;
        }

        winner.setScore(winner.getScore() + 5);
        loser.setScore(Math.max(0, loser.getScore() - 3));

        userRepository.save(winner);
        userRepository.save(loser);
        
        gameRepository.save(game); 
    }
    @Transactional(readOnly = true)
    public List<Game> getHistory(String username) {
        User user = userRepository.findByUsername(username);
        return gameRepository.findByPlayer1OrPlayer2(user, user);
    }
}
