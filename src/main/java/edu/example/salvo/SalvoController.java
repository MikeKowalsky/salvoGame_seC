package edu.example.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    public Map<String, Object> createGameDO (Authentication authentication){
        Map<String, Object> gameDO = new HashMap<>();
        if (isGuest(authentication)){
            gameDO.put("loggedin", null);
        } else {
            Player loggedInPlayer = getLoggedInPlayer(authentication);
            gameDO.put("loggedIn", createPlayerDO(loggedInPlayer));
        }

        List<Object> gameList = new ArrayList<>();
        gameRepo.findAll()
            .stream()
            .forEach(game -> gameList.add(new HashMap<String, Object>(){{
                put("game_id", game.getId());
                put("created", game.getCreationDate().getTime());
                put("gamePlayers", createGamePlayerDO(game));
            }}));
        gameDO.put("gameList", gameList);
        return gameDO;
    }

    private List<Object> createGamePlayerDO(Game game){
        List<Object> gpDataObject = new ArrayList<>();
        game.gamePlayerSet.stream()
                          .forEach(gp -> gpDataObject.add(new HashMap<String, Object>(){{
                              put("gp_id", gp.getId());
                              put("player", createPlayerDO(gp.getPlayer()));
                          }}));
        return gpDataObject;
    }

    private Map<String, Object> createPlayerDO(Player player){
        return new HashMap<String, Object>(){{
            put("player_id", player.getId());
            put("email", player.getUserName());
            put("name", player.getName());
        }};
    }

    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    private Player getLoggedInPlayer(Authentication authentication){
        return !isGuest(authentication)
            ? playerRepo.findByUserName(authentication.getName())
            : null;
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
            if(opponentGP != null){
                singleGameView.put("opponent", createGPDOforGameView(opponentGP));
            } else {
                singleGameView.put("opponent", null);
            }
            singleGameView.put("ships", createShipList(currentGP));
            singleGameView.put("salvoes", createSalvoList(currentGP, opponentGP));
        return singleGameView;
    }

    private GamePlayer getOpponentGP(GamePlayer currentGP){
        if(currentGP.getGame().gamePlayerSet.size() == 1){
            return null;
        }
        GamePlayer[] result = { null }; //to avoid this shit with final in lambda function
        currentGP.getGame().gamePlayerSet.stream().forEach(gp -> {
            if(gp.getId() != currentGP.getId()){
                result[0] = gp;
            }
        });
        return result[0];
    }

    private Map<String, Object> createGPDOforGameView(GamePlayer gp){
        return new HashMap<String, Object>(){{
            put("gp_id", gp.getId());
            put("player_id", gp.getPlayer().getId());
            put("name", gp.getPlayer().getName());
            put("user_name", gp.getPlayer().getUserName());
        }};
    }

    private List<Object> createShipList(GamePlayer gp){
        List<Object> shipList = new ArrayList<>();
        gp.shipSet.stream().forEach(ship -> shipList.add(new HashMap<String, Object>(){{
            put("type", ship.getType());
            put("locations", ship.getLocations());
        }}));
        return shipList;
    }

    private List<Object> createSalvoList(GamePlayer currentGp, GamePlayer opponentGP){
        List<Object> salvoList = new ArrayList<>();
        currentGp.salvoSet.stream()
                          .forEach(salvo -> salvoList.add(createSingleSalvoMap(salvo)));
        if(opponentGP != null){
            opponentGP.salvoSet.stream()
                    .forEach(salvo -> salvoList.add(createSingleSalvoMap(salvo)));
        }
        return salvoList;
    }

    private Map<String, Object> createSingleSalvoMap(Salvo salvo){
        return new HashMap<String, Object>(){{
            put("player_id", salvo.getPlayer().getId());
            put("turn", salvo.getTurnNo());
            put("locations", salvo.getLocations());
        }};
    }

    @RequestMapping("/leaderboard")
    public List<Object> createLeaderboard () {
        return playerRepo.findAll().stream()
            .map(player -> new HashMap<String, Object>(){{
                put("player_id", player.getId());
                put("player_name", player.getName());
                put("scores", player.scoreSet
                    .stream()
                    .map(score -> score.getScoreValue())
                    .collect(Collectors.toList()));
        }}).collect(Collectors.toList());
    }
}
