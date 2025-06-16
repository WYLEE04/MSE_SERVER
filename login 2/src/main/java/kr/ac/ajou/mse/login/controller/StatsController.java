package kr.ac.ajou.mse.login.controller;

import kr.ac.ajou.mse.login.service.StatsService;
import org.springframework.web.bind.annotation.*;
import kr.ac.ajou.mse.login.util.StringUtils;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class StatsController {

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/stats/{username}/factions")
    public Map<String, Object> getPlayerFactionStats(@PathVariable String username) {
        return statsService.getPlayerFactionStats(StringUtils.cleanUsername(username));
    }

    @GetMapping("/stats/{username}/characters")
    public Map<String, Object> getPlayerCharacterStats(@PathVariable String username) {
        return statsService.getPlayerCharacterStats(StringUtils.cleanUsername(username));
    }

    @GetMapping("/stats/{username}/cards")
    public Map<String, Object> getPlayerCardStats(@PathVariable String username) {
        return statsService.getPlayerCardStats(StringUtils.cleanUsername(username));
    }

    @GetMapping("/stats/{username}/overall")
    public Map<String, Object> getPlayerOverallStats(@PathVariable String username) {
        return statsService.getPlayerOverallStats(StringUtils.cleanUsername(username));
    }
    
}