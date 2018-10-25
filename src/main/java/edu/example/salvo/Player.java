package edu.example.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String name;
    private String userName;

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    Set<GamePlayer> gamePlayerSet;

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    Set<Score> scoreSet;

    Player(){}

    Player(String name, String userName){
        this.name = name;
        this.userName = userName;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUserName() {
        return userName;
    }

    public void addGamePlayer(GamePlayer gamePlayer){
        gamePlayer.setPlayer(this);
        gamePlayerSet.add(gamePlayer);
    }

    public void addScore(Score score){
        score.setPlayer(this);
        scoreSet.add(score);
    }

    @JsonIgnore
    public List<Game> getGames(){
        return gamePlayerSet.stream().map(gp -> gp.getGame()).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
