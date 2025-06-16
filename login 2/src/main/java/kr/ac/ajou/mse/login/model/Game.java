package kr.ac.ajou.mse.login.model;

import jakarta.persistence.*;

@Entity
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    @ManyToOne
    private User player1;

    @ManyToOne
    private User player2;

    @ManyToOne
    private User winner;

    private boolean finished = false;
    private int player1Wins = 0;
    private int player2Wins = 0;


    public Game() {}

    public Long getId() { 
        return id; 
    }
    public User getPlayer1() { 
        return player1; 
    }
    public User getPlayer2() { 
        return player2; 
    }
    public User getWinner() { 
        return winner; 
    }


    public boolean isFinished() { 
        return finished; 
    }
    public void setFinished(boolean finished) { 
        this.finished = finished; 
    }
    public int getPlayer1Wins() { 
        return player1Wins; 
    }
    public int getPlayer2Wins() { 
        return player2Wins; 
    }

    public void setPlayer1(User player1) { 
        this.player1 = player1; 
    }
    public void setPlayer2(User player2) { 
        this.player2 = player2; 
    }
    public void setWinner(User winner) { 
        this.winner = winner; 
    }
    
    public void setPlayer1Wins(int player1Wins) { 
        this.player1Wins = player1Wins; 
    }
    public void setPlayer2Wins(int player2Wins) { 
        this.player2Wins = player2Wins; 
    }
}
