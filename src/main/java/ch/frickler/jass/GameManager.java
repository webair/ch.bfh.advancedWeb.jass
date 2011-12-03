package ch.frickler.jass;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.frickler.jass.db.entity.Game;
import ch.frickler.jass.db.entity.Game.GameState;
import ch.frickler.jass.db.entity.User;
import ch.frickler.jass.service.GameService;
import ch.frickler.jass.service.UserService;


public class GameManager {

	public static final String GAME_ID_KEY = "currentGameId";

	private static GameManager instance;

	private Map<Long, Game> waitingGameMap = new HashMap<Long, Game>();
	private Map<Long, Game> activeGames = new HashMap<Long, Game>();
	
	private GameManager() {

	}

	public static GameManager getInstance() {
		if (instance == null) {
			instance = new GameManager();
		}
		return instance;

	}

	public Game getGame(long gameId){
		return waitingGameMap.get(gameId);
	}
	
	/**
	 * creates a new game and adds it to the waitinglist
	 * 
	 * @param name
	 * @param winPoints
	 * @return this games "gameTicket" (use it to start the game)
	 */
	public long createGame(String name, int winPoints) {
		GameService s = new GameService();
		Game g = s.createGame(name, winPoints);
		
		waitingGameMap.put(g.getId(), g);
		return g.getId();
	}

	public void addUserToGame(User u, Long gameTicket) {
		Game g = waitingGameMap.get(gameTicket);
		if (g != null){
			getGameService(g).addSpieler(u);
		}
	}

	public List<User> getPlayers(Long gameTicket) {
		// TODO game should come from the db
		Game g = waitingGameMap.get(gameTicket);
		return getGameService(g).getAllSpieler();
	}
	
	/**
	 * a game is ready, when there are 4 players
	 * @param gameId
	 * @return
	 */
	public boolean gameIsReady(Long gameId) {
		GameService s = getGameService(waitingGameMap.get(gameId));
		return (s.getState() == GameState.RediForPlay);
	}

	private GameService getGameService(Game game) {
		GameService gs = new GameService();
		gs.loadGame(game.getId());
		return gs;
	}

	/**
	 * 
	 * @param gameTicket
	 * @return
	 */
	public Long startGame(Long gameTicket){
		Game g = waitingGameMap.remove(gameTicket);
		
		if(g==null)
			return -1L;
		
		GameService gs = getGameService(g);
		
		// fill the game with computer players
		while(gs.getState() == GameState.WaitForPlayers){
			//TODO why create a user for every computer player????
			UserService s = new UserService();
			gs.addSpieler(s.createSpieler("a bot"));
		}
		
		activeGames.put(gameTicket, g);
		return gameTicket; // TODO should be the db id...
	}

	// TODO return all games with state waitingFor Player
	public Collection<Game> getAvailableGames(){
		return waitingGameMap.values();
	}
	
	// TODO return all games in Progress
	public Collection<Game> getActiveGames() {
		return activeGames.values();
	}
	
}
