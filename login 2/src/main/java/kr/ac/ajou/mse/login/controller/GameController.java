package kr.ac.ajou.mse.login.controller;
import kr.ac.ajou.mse.login.model.Game;
import kr.ac.ajou.mse.login.model.RoundLog;
import kr.ac.ajou.mse.login.model.Faction;
import kr.ac.ajou.mse.login.model.Character;
import kr.ac.ajou.mse.login.service.GameService;
import kr.ac.ajou.mse.login.service.RoundLogService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import kr.ac.ajou.mse.login.util.StringUtils;


@RestController
@RequestMapping("/api")
public class GameController {

    private final GameService gameService;
    private final RoundLogService roundLogService;

    public GameController(GameService gameService, RoundLogService roundLogService) {
        this.gameService = gameService;
        this.roundLogService = roundLogService;
    }

    // Game setting
    @PostMapping("/game")
    public String createGame(@RequestBody CreateGameRequest request) {
        String player1 = StringUtils.cleanUsername(request.getPlayer1());
        String player2 = StringUtils.cleanUsername(request.getPlayer2());
        Long gameId = gameService.createGame(player1, player2);

        return "Game created with ID: " + gameId;
    }

    // Round log
    @PostMapping("/round")
    public String addRound(@RequestBody AddRoundRequest request) {
        String winnerUsername = StringUtils.cleanUsername(request.getWinnerUsername());
        roundLogService.addRound(
                request.getGameId(),
                request.getRoundNo(),
                request.getP1Faction(),
                request.getP1Character(),
                request.getP2Faction(),
                request.getP2Character(),
                winnerUsername,
                request.getP1CardIds(),
                request.getP2CardIds()
        );

        return "Round added successfully";
    }

    // finish
    @PostMapping("/finish")
    public String finishGame(@RequestBody FinishGameRequest request) {
            try {
                // 먼저 원본 username으로 시도
                gameService.finishGame(request.getGameId(), request.getWinnerUsername());
            } catch (Exception e) {
                // 실패하면 cleaned username으로 재시도
                String cleanWinner = StringUtils.cleanUsername(request.getWinnerUsername());
                gameService.finishGame(request.getGameId(), cleanWinner);
            }
            return "Game finished";
        
}
    

    // Look up history
    @GetMapping("/history/{username}")
    public List<Game> getHistory(@PathVariable String username) {
        String cleanUsername = StringUtils.cleanUsername(username);
        return gameService.getHistory(cleanUsername);
    }

    // Replay game
    @GetMapping("/replay/{gameId}")
    public List<RoundLog> getReplay(@PathVariable Long gameId) {
        return roundLogService.getReplay(gameId);
    }

    public static class CreateGameRequest {
        private String player1;
        private String player2;

        public String getPlayer1() { 
            return player1; 
        }
        public void setPlayer1(String player1) { 
            this.player1 = player1; 
        }
        public String getPlayer2() { 
            return player2; 
        }
        public void setPlayer2(String player2) { 
            this.player2 = player2; 
        }
    }

    public static class AddRoundRequest {
        private Long gameId;
        private int roundNo;
        private Faction p1Faction;
        private Character p1Character;
        private Faction p2Faction;
        private Character p2Character;
        private String winnerUsername;
        private List<Long> p1CardIds;
        private List<Long> p2CardIds;


        public List<Long> getP1CardIds() {
            return p1CardIds;
        }
    
        public void setP1CardIds(List<Long> p1CardIds) {
            this.p1CardIds = p1CardIds;
        }
    
        public List<Long> getP2CardIds() {
            return p2CardIds;
        }
    
        public void setP2CardIds(List<Long> p2CardIds) {
            this.p2CardIds = p2CardIds;
        }

        public Long getGameId() { 
            return gameId; 
        }
        public void setGameId(Long gameId) { this.gameId = gameId; }
        public int getRoundNo() { 
            return roundNo; 
        }
        public void setRoundNo(int roundNo) { 
            this.roundNo = roundNo; 
        }
        public Faction getP1Faction() { 
            return p1Faction; 
        }

        public void setP1Faction(Faction p1Faction) { 
            this.p1Faction = p1Faction; 
        }
        public Character getP1Character() { 
            return p1Character; 
        }
        public void setP1Character(Character p1Character) { 
            this.p1Character = p1Character; 
        }
        public Faction getP2Faction() { 
            return p2Faction; 
        }
        public void setP2Faction(Faction p2Faction) { 
            this.p2Faction = p2Faction; 
        }
        public Character getP2Character() { 
            return p2Character; 
        }
        public void setP2Character(Character p2Character) { 
            this.p2Character = p2Character; 
        }
        public String getWinnerUsername() { 
            return winnerUsername; 
        }
        public void setWinnerUsername(String winnerUsername) { 
            this.winnerUsername = winnerUsername; 
        }
    }

    public static class FinishGameRequest {
        private Long gameId;
        private String winnerUsername;
        public Long getGameId() { 
            return gameId; 
        }
        public void setGameId(Long gameId) { 
            this.gameId = gameId; 
        }
        public String getWinnerUsername() { 
            return winnerUsername; 
        }
        public void setWinnerUsername(String winnerUsername) { 
            this.winnerUsername = winnerUsername; 
        }
    }
}
