package edu.example.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private ShipRepository shipRepo;

    // creating data for game list
    @RequestMapping("/games")
    public Map<String, Object> createGameDO (Authentication authentication){
        Map<String, Object> gameDO = new HashMap<>();
        if (isGuest(authentication)){
            gameDO.put("loggedIn", null);
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

    // adding info to gameView JSON
    @RequestMapping("/game_view/{gpId}")
    public ResponseEntity<Map<String, Object>> createGameView(@PathVariable long gpId, Authentication auth){
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

        if(isGuest(auth)){//this one is redundant, because not login user has no authority to see /game_View at all
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(createMap("error", "Please login first"));
        } else {
            Player loggedInPlayer = getLoggedInPlayer(auth);
            if (loggedInPlayer.getId() == currentGP.getPlayer().getId()){
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(singleGameView);
            } else {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(createMap("error", "This is not your game!"));
            }
        }
    }

    // adding info to gameView JSON
    private Map<String, Object> createGPDOforGameView(GamePlayer gp){
        return new HashMap<String, Object>(){{
            put("gp_id", gp.getId());
            put("player_id", gp.getPlayer().getId());
            put("name", gp.getPlayer().getName());
            put("user_name", gp.getPlayer().getUserName());
        }};
    }

    // adding info to gameView JSON
    private List<Object> createShipList(GamePlayer gp){
        List<Object> shipList = new ArrayList<>();
        gp.shipSet.stream().forEach(ship -> shipList.add(new HashMap<String, Object>(){{
            put("type", ship.getType());
            put("locations", ship.getLocations());
        }}));
        return shipList;
    }

    // adding info to gameView JSON
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

    // adding info to gameView JSON
    private Map<String, Object> createSingleSalvoMap(Salvo salvo){
        return new HashMap<String, Object>(){{
            put("player_id", salvo.getPlayer().getId());
            put("turn", salvo.getTurnNo());
            put("locations", salvo.getLocations());
        }};
    }


    // create leaderboard data
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

    // creating Map objects for responses
    private Map<String, Object> createMap(String key, Object value) {
        return new HashMap<String, Object>(){{
            put(key, value);
        }};
    }

    // create new player
    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createPlayer(String name, String userName, String password) {

        if(name.isEmpty() || userName.isEmpty() || password.isEmpty()){
            return new ResponseEntity<>(createMap("error", "Invalid name, username or password"), HttpStatus.FORBIDDEN);
        }

        Player player = playerRepo.findByUserName(userName);
        if(player != null) {
            return new ResponseEntity<>(createMap("error", "Username already exists"), HttpStatus.FORBIDDEN);
        }

        Player newPlayer = playerRepo.save(new Player(name, userName, password));
        return new ResponseEntity<>(createMap("Username", newPlayer.getUserName()), HttpStatus.CREATED);
    }

    //create game
    @RequestMapping(path = "/games", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createGame(Authentication auth){
        if(isGuest(auth)){
            return new ResponseEntity<>(createMap("error", "You need to login"), HttpStatus.UNAUTHORIZED);
        }

        Game newGame = gameRepo.save(new Game());
        GamePlayer newGamePlayer = new GamePlayer(newGame, getLoggedInPlayer(auth));
        gamePlayerRepo.save(newGamePlayer);

        return new ResponseEntity<>(createMap("new_GamePlayerID", newGamePlayer.getId()), HttpStatus.CREATED);
    }

    //join game
    @RequestMapping(path = "/game/{gameId}/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> joinGame(Authentication auth, @PathVariable long gameId){
        if(isGuest(auth)){
            return new ResponseEntity<>(createMap("error", "You need to login."), HttpStatus.UNAUTHORIZED);
        }
        if(!gameRepo.existsById(gameId)){
            return new ResponseEntity<>(createMap("error", "Game with this id doesn't exist."), HttpStatus.FORBIDDEN);
        }

        Game currentGame = gameRepo.findById(gameId);

        if(currentGame.isFull()){
            return new ResponseEntity<>(createMap("error", "Game has already two players."), HttpStatus.FORBIDDEN);
        }

        Player loggedInPlayer = playerRepo.findByUserName(auth.getName());
        GamePlayer newGamePlayer = new GamePlayer(currentGame,loggedInPlayer);
        gamePlayerRepo.save(newGamePlayer);

        return new ResponseEntity<>(createMap("new_GamePlayerID", newGamePlayer.getId()), HttpStatus.CREATED);
    }

    //add ships
    @RequestMapping(path = "/games/players/{gpId}/ships", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> addShips(Authentication auth,
                                                        @PathVariable long gpId,
                                                        @RequestBody ArrayList<Ship> shipArray){
        if(isGuest(auth)){
            return new ResponseEntity<>(createMap("error", "You need to login."), HttpStatus.UNAUTHORIZED);
        }
        if(!gamePlayerRepo.existsById(gpId)){
            return new ResponseEntity<>(createMap("error", "GamePlayer doesn't exist."), HttpStatus.UNAUTHORIZED);
        }

        GamePlayer currentGP = gamePlayerRepo.findById(gpId);
        Player loggedInPlayer = playerRepo.findByUserName(auth.getName());

        if(currentGP.getPlayer().getId() != loggedInPlayer.getId()){
            return new ResponseEntity<>(createMap("error", "You are not allowed."), HttpStatus.UNAUTHORIZED);
        }

        if(currentGP.shipSet.size() > 0){
            return new ResponseEntity<>(createMap("error", "Ships are already placed."), HttpStatus.FORBIDDEN);
        }

        Map<Object,Object> result = new HashMap<>();

        shipArray.forEach(ship -> {
            currentGP.addShip(ship);
            shipRepo.save(ship);
            result.put(ship.getId(), ship.getLocations());
        });

        gamePlayerRepo.save(currentGP);
        return new ResponseEntity<>(createMap("Added Ships", result), HttpStatus.CREATED);
    }
}
