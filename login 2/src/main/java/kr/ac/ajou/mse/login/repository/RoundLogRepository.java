package kr.ac.ajou.mse.login.repository;

import kr.ac.ajou.mse.login.model.RoundLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// Look up rounds in order of numbers.

public interface RoundLogRepository extends JpaRepository<RoundLog, Long> {
    List<RoundLog> findByGameIdOrderByRoundNoAsc(Long gameId);
}
