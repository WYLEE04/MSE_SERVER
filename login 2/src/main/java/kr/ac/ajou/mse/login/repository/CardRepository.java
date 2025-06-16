package kr.ac.ajou.mse.login.repository;

import kr.ac.ajou.mse.login.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {
    Optional<Card> findByName(String name);
}
