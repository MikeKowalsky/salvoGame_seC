package edu.example.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private Date creationDate = new Date();

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    Set<GamePlayer> gamePlayerSet;

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    Set<Score> scoreSet;

    Game(){}

    public long getId() {
        return id;
    }

    public Date getCreationDate() {
        return creationDate;
    }


    public void addGamePlayer(GamePlayer gamePlayer){
        gamePlayer.setGame(this);
        gamePlayerSet.add(gamePlayer);
    }

    public void addScore(Score score){
        score.setGame(this);
        scoreSet.add(score);
    }

//    @JsonIgnore
    public List<Player> getPlayers(){
        return gamePlayerSet.stream().map(gp -> gp.getPlayer()).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", creationDate=" + creationDate +
                '}';
    }
}
