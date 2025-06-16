package kr.ac.ajou.mse.login.controller;

import java.util.HashMap;
import java.util.Map;
import kr.ac.ajou.mse.login.util.StringUtils;
import kr.ac.ajou.mse.login.model.User;
import kr.ac.ajou.mse.login.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    private final UserService userService;
    
    public AuthController(UserService userService) {
        this.userService = userService;
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> credentials) {

        String username = StringUtils.cleanUsername(credentials.get("username"));
        String password = credentials.get("password");


        User registeredUser = userService.registerUser(username, password);

        Map<String, Object> response = new HashMap<>();
        if (registeredUser == null) {
            response.put("success", false);
            response.put("message", "Username already exists");
            return ResponseEntity.badRequest().body(response);
        }
        response.put("success", true);
        response.put("message", "User registered successfully");
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> credentials) {
        String username = StringUtils.cleanUsername(credentials.get("username"));
        String password = credentials.get("password");
        
        boolean isValid = userService.validateUser(username, password);
        
        Map<String, Object> response = new HashMap<>();
        if (isValid) {
            response.put("success", true);
            response.put("message", "Login successful");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Invalid username or password");
            return ResponseEntity.badRequest().body(response);
        }
    }
}