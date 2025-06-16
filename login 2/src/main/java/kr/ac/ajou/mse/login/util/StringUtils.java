package kr.ac.ajou.mse.login.util;

import org.springframework.stereotype.Component;

@Component  
public class StringUtils {
    
    /**
     * Removes invisible Unicode characters from usernames
     * Prevents issues with URL encoding and database queries
     * 
     * @param username The raw username to clean
     * @return Cleaned username safe for database storage
     */

    public static String cleanUsername(String username) {
        if (username == null) return "";
        return username
            .replaceAll("[\\u200B-\\u200D\\uFEFF]", "")  
            .replaceAll("[\\u0000-\\u001F\\u007F-\\u009F]", "")  
            .replaceAll("\\s+", " ")  
            .trim();
    }
}