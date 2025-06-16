package kr.ac.ajou.mse.login.service;

import kr.ac.ajou.mse.login.model.User;
import kr.ac.ajou.mse.login.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public User registerUser(String username, String password) {
        
        if (userRepository.findByUsername(username) != null) {
            return null; 
        }
        
        User user = new User(username, password);
        return userRepository.save(user);
    }
    
    public boolean validateUser(String username, String password) {
        User user = userRepository.findByUsername(username);
        return user != null && user.getPassword().equals(password);
    }
}