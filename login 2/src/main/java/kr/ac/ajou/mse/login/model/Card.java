package kr.ac.ajou.mse.login.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
 
    private Long CardId;
    private String name;

    public Card() {}

    public Card(String name) {
        this.name = name;
    }

    
    public Long getId() { 
        return CardId; 
    }

    public String getName() { 
        return name; 
    }
    public void setName(String name) { 
        this.name = name; 
    }
}
