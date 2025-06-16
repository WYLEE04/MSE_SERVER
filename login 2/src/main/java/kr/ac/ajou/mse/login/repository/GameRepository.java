package kr.ac.ajou.mse.login.repository;

import kr.ac.ajou.mse.login.model.Game;
import kr.ac.ajou.mse.login.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// View the game which the player participates in. 

public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findByPlayer1OrPlayer2(User player1, User player2);
}
