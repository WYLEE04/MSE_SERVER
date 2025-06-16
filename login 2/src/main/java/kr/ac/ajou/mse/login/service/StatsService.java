package kr.ac.ajou.mse.login.service;

import kr.ac.ajou.mse.login.model.*;
import kr.ac.ajou.mse.login.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class StatsService {

    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final RoundLogRepository roundLogRepository;

    public StatsService(UserRepository userRepository, 
                       GameRepository gameRepository,
                       RoundLogRepository roundLogRepository) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.roundLogRepository = roundLogRepository;
    }

    // ===== 개인 통계 =====

    public Map<String, Object> getPlayerFactionStats(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return Map.of("error", "User not found");
        }

        List<Game> games = gameRepository.findByPlayer1OrPlayer2(user, user);
        Map<String, Integer> factionWins = new HashMap<>();
        Map<String, Integer> factionTotal = new HashMap<>();

        for (Game game : games) {
            if (!game.isFinished()) continue;

            // 해당 게임의 모든 라운드 로그 가져오기
            List<RoundLog> rounds = roundLogRepository.findByGameIdOrderByRoundNoAsc(game.getId());
            
            for (RoundLog round : rounds) {
                String faction;
                boolean isWin = false;

                // 플레이어가 player1인지 player2인지 확인
                if (game.getPlayer1().equals(user)) {
                    faction = round.getP1Faction().name();
                    isWin = round.getRoundWinner().equals(user);
                } else {
                    faction = round.getP2Faction().name();
                    isWin = round.getRoundWinner().equals(user);
                }

                factionTotal.put(faction, factionTotal.getOrDefault(faction, 0) + 1);
                if (isWin) {
                    factionWins.put(faction, factionWins.getOrDefault(faction, 0) + 1);
                }
            }
        }

        // 승률 계산
        List<Map<String, Object>> factionStats = new ArrayList<>();
        for (String faction : factionTotal.keySet()) {
            int total = factionTotal.get(faction);
            int wins = factionWins.getOrDefault(faction, 0);
            double winRate = total > 0 ? (double) wins / total * 100 : 0;

            factionStats.add(Map.of(
                "faction", faction,
                "gamesPlayed", total,
                "wins", wins,
                "losses", total - wins,
                "winRate", Math.round(winRate * 100.0) / 100.0
            ));
        }

        // 승률 순으로 정렬
        factionStats.sort((a, b) -> Double.compare((Double) b.get("winRate"), (Double) a.get("winRate")));

        return Map.of(
            "username", username,
            "factionStats", factionStats
        );
    }

    public Map<String, Object> getPlayerCharacterStats(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return Map.of("error", "User not found");
        }

        List<Game> games = gameRepository.findByPlayer1OrPlayer2(user, user);
        Map<String, Integer> characterWins = new HashMap<>();
        Map<String, Integer> characterTotal = new HashMap<>();

        for (Game game : games) {
            if (!game.isFinished()) continue;

            List<RoundLog> rounds = roundLogRepository.findByGameIdOrderByRoundNoAsc(game.getId());
            
            for (RoundLog round : rounds) {
                String character;
                boolean isWin = false;

                if (game.getPlayer1().equals(user)) {
                    character = round.getP1Character().name();
                    isWin = round.getRoundWinner().equals(user);
                } else {
                    character = round.getP2Character().name();
                    isWin = round.getRoundWinner().equals(user);
                }

                characterTotal.put(character, characterTotal.getOrDefault(character, 0) + 1);
                if (isWin) {
                    characterWins.put(character, characterWins.getOrDefault(character, 0) + 1);
                }
            }
        }

        List<Map<String, Object>> characterStats = new ArrayList<>();
        for (String character : characterTotal.keySet()) {
            int total = characterTotal.get(character);
            int wins = characterWins.getOrDefault(character, 0);
            double winRate = total > 0 ? (double) wins / total * 100 : 0;

            characterStats.add(Map.of(
                "character", character,
                "gamesPlayed", total,
                "wins", wins,
                "losses", total - wins,
                "winRate", Math.round(winRate * 100.0) / 100.0
            ));
        }

        characterStats.sort((a, b) -> Double.compare((Double) b.get("winRate"), (Double) a.get("winRate")));

        return Map.of(
            "username", username,
            "characterStats", characterStats
        );
    }

    public Map<String, Object> getPlayerCardStats(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return Map.of("error", "User not found");
        }

        List<Game> games = gameRepository.findByPlayer1OrPlayer2(user, user);
        Map<String, Integer> cardWins = new HashMap<>();
        Map<String, Integer> cardTotal = new HashMap<>();

        for (Game game : games) {
            if (!game.isFinished()) continue;

            List<RoundLog> rounds = roundLogRepository.findByGameIdOrderByRoundNoAsc(game.getId());
            
            Set<String> gameCards = new HashSet<>();
            boolean isWin = game.getWinner() != null && game.getWinner().equals(user);


            for (RoundLog round : rounds) {
                List<Card> playerCards;
                
                if (game.getPlayer1().equals(user)) {
                    playerCards = round.getP1Cards();
                } else {
                    playerCards = round.getP2Cards();
                }
                for (Card card : playerCards) {
                    gameCards.add(card.getName());
                }
            }
            //게임별로 1번씩 카운트
            for (String cardName : gameCards) {
                cardTotal.put(cardName, cardTotal.getOrDefault(cardName, 0) + 1);
                if (isWin) {
                    cardWins.put(cardName, cardWins.getOrDefault(cardName, 0) + 1);
                }
            }
        }
    

        List<Map<String, Object>> cardStats = new ArrayList<>();
        for (String cardName : cardTotal.keySet()) {
            int total = cardTotal.get(cardName);
            int wins = cardWins.getOrDefault(cardName, 0);
            double winRate = total > 0 ? (double) wins / total * 100 : 0;

            cardStats.add(Map.of(
                "cardName", cardName,
                "timesUsed", total,
                "wins", wins,
                "losses", total - wins,
                "winRate", Math.round(winRate * 100.0) / 100.0
            ));
        }

        // 사용 횟수 순으로 정렬
        cardStats.sort((a, b) -> Integer.compare((Integer) b.get("timesUsed"), (Integer) a.get("timesUsed")));

        return Map.of(
            "username", username,
            "cardStats", cardStats
        );
    }

    public Map<String, Object> getPlayerOverallStats(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return Map.of("error", "User not found");
        }

        List<Game> games = gameRepository.findByPlayer1OrPlayer2(user, user);
        int totalGames = 0;
        int wins = 0;
        int totalRounds = 0;

        for (Game game : games) {
            if (!game.isFinished()) continue;
            
            totalGames++;
            if (game.getWinner() != null && game.getWinner().equals(user)) {
                wins++;
            }

            List<RoundLog> rounds = roundLogRepository.findByGameIdOrderByRoundNoAsc(game.getId());
            totalRounds += rounds.size();
        }

        double winRate = totalGames > 0 ? (double) wins / totalGames * 100 : 0;
        double avgRoundsPerGame = totalGames > 0 ? (double) totalRounds / totalGames : 0;

        return Map.of(
            "username", username,
            "totalGames", totalGames,
            "wins", wins,
            "losses", totalGames - wins,
            "winRate", Math.round(winRate * 100.0) / 100.0,
            "totalRounds", totalRounds,
            "avgRoundsPerGame", Math.round(avgRoundsPerGame * 100.0) / 100.0,
            "currentScore", user.getScore()
        );
    }

    // ===== 전체 메타 분석 =====

    public Map<String, Object> getGlobalFactionMeta() {
        List<RoundLog> allRounds = roundLogRepository.findAll();
        Map<String, Integer> factionWins = new HashMap<>();
        Map<String, Integer> factionTotal = new HashMap<>();

        for (RoundLog round : allRounds) {
            // Player1 통계
            String p1Faction = round.getP1Faction().name();
            factionTotal.put(p1Faction, factionTotal.getOrDefault(p1Faction, 0) + 1);
            if (round.getRoundWinner().equals(round.getGame().getPlayer1())) {
                factionWins.put(p1Faction, factionWins.getOrDefault(p1Faction, 0) + 1);
            }

            // Player2 통계
            String p2Faction = round.getP2Faction().name();
            factionTotal.put(p2Faction, factionTotal.getOrDefault(p2Faction, 0) + 1);
            if (round.getRoundWinner().equals(round.getGame().getPlayer2())) {
                factionWins.put(p2Faction, factionWins.getOrDefault(p2Faction, 0) + 1);
            }
        }

        List<Map<String, Object>> factionMeta = new ArrayList<>();
        for (String faction : factionTotal.keySet()) {
            int total = factionTotal.get(faction);
            int wins = factionWins.getOrDefault(faction, 0);
            double winRate = total > 0 ? (double) wins / total * 100 : 0;
            double pickRate = allRounds.size() > 0 ? (double) total / (allRounds.size() * 2) * 100 : 0;

            factionMeta.add(Map.of(
                "faction", faction,
                "pickRate", Math.round(pickRate * 100.0) / 100.0,
                "winRate", Math.round(winRate * 100.0) / 100.0,
                "totalPicks", total,
                "wins", wins
            ));
        }

        factionMeta.sort((a, b) -> Double.compare((Double) b.get("winRate"), (Double) a.get("winRate")));

        return Map.of("factionMeta", factionMeta);
    }

    public Map<String, Object> getGlobalCharacterMeta() {
        // 진영과 비슷한 로직으로 캐릭터 메타 계산
        List<RoundLog> allRounds = roundLogRepository.findAll();
        Map<String, Integer> characterWins = new HashMap<>();
        Map<String, Integer> characterTotal = new HashMap<>();

        for (RoundLog round : allRounds) {
            String p1Character = round.getP1Character().name();
            characterTotal.put(p1Character, characterTotal.getOrDefault(p1Character, 0) + 1);
            if (round.getRoundWinner().equals(round.getGame().getPlayer1())) {
                characterWins.put(p1Character, characterWins.getOrDefault(p1Character, 0) + 1);
            }

            String p2Character = round.getP2Character().name();
            characterTotal.put(p2Character, characterTotal.getOrDefault(p2Character, 0) + 1);
            if (round.getRoundWinner().equals(round.getGame().getPlayer2())) {
                characterWins.put(p2Character, characterWins.getOrDefault(p2Character, 0) + 1);
            }
        }

        List<Map<String, Object>> characterMeta = new ArrayList<>();
        for (String character : characterTotal.keySet()) {
            int total = characterTotal.get(character);
            int wins = characterWins.getOrDefault(character, 0);
            double winRate = total > 0 ? (double) wins / total * 100 : 0;
            double pickRate = allRounds.size() > 0 ? (double) total / (allRounds.size() * 2) * 100 : 0;

            characterMeta.add(Map.of(
                "character", character,
                "pickRate", Math.round(pickRate * 100.0) / 100.0,
                "winRate", Math.round(winRate * 100.0) / 100.0,
                "totalPicks", total,
                "wins", wins
            ));
        }

        characterMeta.sort((a, b) -> Double.compare((Double) b.get("winRate"), (Double) a.get("winRate")));

        return Map.of("characterMeta", characterMeta);
    }

    public Map<String, Object> getGlobalCardMeta() {
        // 카드 메타 분석 로직
        List<RoundLog> allRounds = roundLogRepository.findAll();
        Map<String, Integer> cardWins = new HashMap<>();
        Map<String, Integer> cardTotal = new HashMap<>();

        for (RoundLog round : allRounds) {
            // Player1 카드들
            for (Card card : round.getP1Cards()) {
                String cardName = card.getName();
                cardTotal.put(cardName, cardTotal.getOrDefault(cardName, 0) + 1);
                if (round.getRoundWinner().equals(round.getGame().getPlayer1())) {
                    cardWins.put(cardName, cardWins.getOrDefault(cardName, 0) + 1);
                }
            }

            // Player2 카드들
            for (Card card : round.getP2Cards()) {
                String cardName = card.getName();
                cardTotal.put(cardName, cardTotal.getOrDefault(cardName, 0) + 1);
                if (round.getRoundWinner().equals(round.getGame().getPlayer2())) {
                    cardWins.put(cardName, cardWins.getOrDefault(cardName, 0) + 1);
                }
            }
        }

        List<Map<String, Object>> cardMeta = new ArrayList<>();
        int totalCardUsages = cardTotal.values().stream().mapToInt(Integer::intValue).sum();

        for (String cardName : cardTotal.keySet()) {
            int total = cardTotal.get(cardName);
            int wins = cardWins.getOrDefault(cardName, 0);
            double winRate = total > 0 ? (double) wins / total * 100 : 0;
            double pickRate = totalCardUsages > 0 ? (double) total / totalCardUsages * 100 : 0;

            cardMeta.add(Map.of(
                "cardName", cardName,
                "pickRate", Math.round(pickRate * 100.0) / 100.0,
                "winRate", Math.round(winRate * 100.0) / 100.0,
                "totalUsages", total,
                "wins", wins
            ));
        }

        cardMeta.sort((a, b) -> Integer.compare((Integer) b.get("totalUsages"), (Integer) a.get("totalUsages")));

        return Map.of("cardMeta", cardMeta);
    }

    public Map<String, Object> getFactionMatchups() {
        // 진영 상성 분석 (A vs B 승률)
        List<RoundLog> allRounds = roundLogRepository.findAll();
        Map<String, Map<String, Integer>> matchupWins = new HashMap<>();
        Map<String, Map<String, Integer>> matchupTotal = new HashMap<>();

        for (RoundLog round : allRounds) {
            String p1Faction = round.getP1Faction().name();
            String p2Faction = round.getP2Faction().name();

            // P1 진영 vs P2 진영
            matchupTotal.computeIfAbsent(p1Faction, k -> new HashMap<>())
                      .put(p2Faction, matchupTotal.get(p1Faction).getOrDefault(p2Faction, 0) + 1);
            
            if (round.getRoundWinner().equals(round.getGame().getPlayer1())) {
                matchupWins.computeIfAbsent(p1Faction, k -> new HashMap<>())
                          .put(p2Faction, matchupWins.get(p1Faction).getOrDefault(p2Faction, 0) + 1);
            }

            // P2 진영 vs P1 진영 (반대 케이스)
            matchupTotal.computeIfAbsent(p2Faction, k -> new HashMap<>())
                      .put(p1Faction, matchupTotal.get(p2Faction).getOrDefault(p1Faction, 0) + 1);
            
            if (round.getRoundWinner().equals(round.getGame().getPlayer2())) {
                matchupWins.computeIfAbsent(p2Faction, k -> new HashMap<>())
                          .put(p1Faction, matchupWins.get(p2Faction).getOrDefault(p1Faction, 0) + 1);
            }
        }

        Map<String, Map<String, Double>> matchupMatrix = new HashMap<>();
        for (String faction1 : matchupTotal.keySet()) {
            Map<String, Double> winRates = new HashMap<>();
            for (String faction2 : matchupTotal.get(faction1).keySet()) {
                int total = matchupTotal.get(faction1).get(faction2);
                int wins = matchupWins.getOrDefault(faction1, new HashMap<>()).getOrDefault(faction2, 0);
                double winRate = total > 0 ? (double) wins / total * 100 : 0;
                winRates.put(faction2, Math.round(winRate * 100.0) / 100.0);
            }
            matchupMatrix.put(faction1, winRates);
        }

        return Map.of("matchupMatrix", matchupMatrix);
    }

    public Map<String, Object> getMetaTrends(String period) {
        // 나중에 구현 (시간별 메타 변화)
        return Map.of("message", "Meta trends analysis coming soon");
    }
}