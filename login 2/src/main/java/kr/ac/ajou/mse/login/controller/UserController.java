package kr.ac.ajou.mse.login.controller;
import kr.ac.ajou.mse.login.model.User;
import kr.ac.ajou.mse.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;



@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/rankings")
    public List<User> getRankings() {
        return userRepository.findAllByOrderByScoreDesc();
    }
}
