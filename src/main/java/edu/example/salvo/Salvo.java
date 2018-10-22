package edu.example.salvo;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Salvo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long turnNo;

    @ElementCollection
    @Column(name="location")
    private List<String> locations = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gamePlayerId")
    private GamePlayer gamePlayer;

    public Salvo(){}

    public Salvo(long turnNo){
        this.turnNo = turnNo;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public long getId() {
        return id;
    }

    public long getTurnNo() {
        return turnNo;
    }

    public Player getPlayer(){
        return this.gamePlayer.getPlayer();
    }

    public List<String> getLocations() {
        return locations;
    }
}
