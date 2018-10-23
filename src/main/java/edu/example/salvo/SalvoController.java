package edu.example.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RequestMapping("/api")
@RestController
public class SalvoController {

    @Autowired
    private GameRepository gameRepo;

    @Autowired
    private PlayerRepository playerRepo;

    @Autowired
    private GamePlayerRepository gamePlayerRepo;

    @RequestMapping("/games")
    public List<Object> createGameDO (){
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

    private List<Object> createGamePlayerDO(Game game){
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

    private Map<String, Object> createPlayerDO(GamePlayer gp){
        Map<String, Object> singlePlayer = new HashMap<>();
            singlePlayer.put("player_id", gp.getPlayer().getId());
            singlePlayer.put("email", gp.getPlayer().getUserName());
            singlePlayer.put("name", gp.getPlayer().getName());
        return singlePlayer;
    }

    @RequestMapping("/game_view/{gpId}")
    public Map<String, Object> createGameView(@PathVariable long gpId) {
        GamePlayer currentGP = gamePlayerRepo.findById(gpId);
        GamePlayer opponentGP = getOpponentGP(currentGP);

        Map<String, Object> singleGameView = new HashMap<>();
            singleGameView.put("gp_id", currentGP.getId());
            singleGameView.put("game_id", currentGP.getGame().getId());
            singleGameView.put("created", currentGP.getCreationDate());
            singleGameView.put("player", createGPDOforGameView(currentGP));
            singleGameView.put("opponent", createGPDOforGameView(opponentGP));
            singleGameView.put("ships", createShipList(currentGP));
            singleGameView.put("salvoes", createSalvoList(currentGP, opponentGP));
        return singleGameView;
    }

    private GamePlayer getOpponentGP(GamePlayer currentGP){
        GamePlayer[] result = { null }; //to avoid this shit with final in lambda function
        currentGP.getGame().gamePlayerSet.stream().forEach(gp -> {
            if(gp.getId() != currentGP.getId()){
                result[0] = gp;
            }
        });
        return result[0];
    }

    private Map<String, Object> createGPDOforGameView(GamePlayer gp){
        Map<String, Object> singleGP = new HashMap<>();
            singleGP.put("gp_id", gp.getId());
            singleGP.put("player_id", gp.getPlayer().getId());
            singleGP.put("name", gp.getPlayer().getName());
            singleGP.put("user_name", gp.getPlayer().getUserName());
        return singleGP;
    }

    private List<Object> createShipList(GamePlayer gp){
        List<Object> shipList = new ArrayList<>();
        gp.shipSet.stream().forEach(ship -> {
            Map<String, Object> singleShip = new HashMap<>();
                singleShip.put("type", ship.getType());
                singleShip.put("locations", ship.getLocations());
            shipList.add(singleShip);
        });
        return shipList;
    }

    private List<Object> createSalvoList(GamePlayer currentGp, GamePlayer opponentGP){
        List<Object> salvoList = new ArrayList<>();
        currentGp.salvoSet.stream()
                          .forEach(salvo -> salvoList.add(createSingleSalvoMap(salvo)));
        opponentGP.salvoSet.stream()
                           .forEach(salvo -> salvoList.add(createSingleSalvoMap(salvo)));
        return salvoList;
    }

    private Map<String, Object> createSingleSalvoMap(Salvo salvo){
        Map<String, Object> singleSalvo = new HashMap<>();
            singleSalvo.put("player_id", salvo.getPlayer().getId());
            singleSalvo.put("turn", salvo.getTurnNo());
            singleSalvo.put("locations", salvo.getLocations());
        return singleSalvo;
    }

    @RequestMapping("/leaderboard")
    public List<Object> createLeaderboard () {
        List<Object> leaderboardDTO = new ArrayList<>();
        return playerRepo.findAll().stream().map(player -> {
            Map<String, Object> singlePlayer = new HashMap<>();
                singlePlayer.put("player_id", player.getId());
                singlePlayer.put("scores", player.scoreSet
                                                 .stream()
                                                 .map(score -> score.getScoreValue())
                                                 .collect(Collectors.toList()));
            return singlePlayer;
        }).collect(Collectors.toList());
    }
}
