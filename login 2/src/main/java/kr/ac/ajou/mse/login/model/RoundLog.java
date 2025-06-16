package kr.ac.ajou.mse.login.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "round_logs")
public class RoundLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Game game;

    private int roundNo;

    @Enumerated(EnumType.STRING)
    private Faction p1Faction;

    @Enumerated(EnumType.STRING)
    private Character p1Character;

    @Enumerated(EnumType.STRING)
    private Faction p2Faction;

    @Enumerated(EnumType.STRING)
    private Character p2Character;

    @ManyToOne
    private User roundWinner;

    @ManyToMany
    @JoinTable(
        name = "roundlog_p1_cards",
        joinColumns = @JoinColumn(name = "roundlog_id"),
        inverseJoinColumns = @JoinColumn(name = "card_id")
    )
    private List<Card> p1Cards = new ArrayList<>();
    @ManyToMany
    @JoinTable(
        name = "roundlog_p2_cards",
        joinColumns = @JoinColumn(name = "roundlog_id"),
        inverseJoinColumns = @JoinColumn(name = "card_id")
    )
    
    private List<Card> p2Cards = new ArrayList<>();

    public RoundLog() {}

    public Long getId() {
        return id;
    }

    public Game getGame() {
        return game;
    }

    public int getRoundNo() {
        return roundNo;
    }

    public Faction getP1Faction() {
        return p1Faction;
    }

    public Character getP1Character() {
        return p1Character;
    }

    public Faction getP2Faction() {
        return p2Faction;
    }

    public Character getP2Character() {
        return p2Character;
    }

    public User getRoundWinner() {
        return roundWinner;
    }

    public List<Card> getP1Cards() {
        return p1Cards;
    }

    public List<Card> getP2Cards() {
        return p2Cards;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setRoundNo(int roundNo) {
        this.roundNo = roundNo;
    }

    public void setP1Faction(Faction p1Faction) {
        this.p1Faction = p1Faction;
    }

    public void setP1Character(Character p1Character) {
        this.p1Character = p1Character;
    }

    public void setP2Faction(Faction p2Faction) {
        this.p2Faction = p2Faction;
    }

    public void setP2Character(Character p2Character) {
        this.p2Character = p2Character;
    }

    public void setRoundWinner(User roundWinner) {
        this.roundWinner = roundWinner;
    }

    public void setP1Cards(List<Card> p1Cards) {
        this.p1Cards = p1Cards;
    }

    public void setP2Cards(List<Card> p2Cards) {
        this.p2Cards = p2Cards;
    }
}
