package edu.example.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/api")
@RestController
public class SalvoController {

    @Autowired
    private GameRepository gameRepo;

    @RequestMapping("/games")
    public List<Object> gameDO (){
        List<Object> gameDataObject = new ArrayList<>();
        gameRepo.findAll()
                .stream()
                .forEach(game -> {
                    Map<String, Object> singleGame = new HashMap<>();
                    singleGame.put("game_id", game.getId());
                    singleGame.put("created", game.getCreationDate().getTime());
                    singleGame.put("gamePlayers", createGamePlayerDO(game));
                    gameDataObject.add(singleGame);
        });
        return gameDataObject;
    }

    public List<Object> createGamePlayerDO(Game game){
        List<Object> gpDataObject = new ArrayList<>();
        game.gamePlayerSet.stream()
                          .forEach(gp -> {
                                Map<String, Object> singleGP = new HashMap<>();
                                singleGP.put("gp_id", gp.getId());
                                singleGP.put("player", createPlayerDO(gp));
                                gpDataObject.add(singleGP);
        });
        return gpDataObject;
    }

    public Map<String, Object> createPlayerDO(GamePlayer gp){
        Map<String, Object> singlePlayer = new HashMap<>();
        singlePlayer.put("player_id", gp.getPlayer().getId());
        singlePlayer.put("email", gp.getPlayer().getUserName());
        return singlePlayer;
    }

}
