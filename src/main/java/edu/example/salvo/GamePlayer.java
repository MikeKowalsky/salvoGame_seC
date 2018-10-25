package edu.example.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class GamePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "playerId")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gameId")
    private Game game;

    private Date creationDate = new Date();

    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER)
    Set<Ship> shipSet  = new HashSet<>();

    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER)
    Set<Salvo> salvoSet  = new HashSet<>();

    GamePlayer(){}

    GamePlayer(Game game, Player player){
        this.game = game;
        this.player = player;
    }

    public long getId() {
        return id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void addShip(Ship ship) {
        ship.setGamePlayer(this);
        shipSet.add(ship);
    }

    public void addSalvo(Salvo salvo) {
        salvo.setGamePlayer(this);
        salvoSet.add(salvo);
    }

    public Set<Ship> getShipSet() {
        return shipSet;
    }

    public Set<Salvo> getSalvoSet() {
        return salvoSet;
    }

    public Date getCreationDate() {
        return creationDate;
    }
}
