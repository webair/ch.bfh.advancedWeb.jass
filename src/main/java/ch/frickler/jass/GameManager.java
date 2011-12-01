package ch.frickler.jass;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ch.frickler.jass.db.entity.Game;
import ch.frickler.jass.db.entity.Game.GameState;
import ch.frickler.jass.db.entity.User;
import ch.frickler.jass.service.GameService;
import ch.frickler.jass.service.UserService;


public class GameManager {

	// for refacotring
	public static final String GAME_ID_KEY = "currentGameId";

	private static GameManager instance;

	private Long currentwaitingGameMapIndex = 1L;
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
		
		waitingGameMap.put(currentwaitingGameMapIndex, g);
		return currentwaitingGameMapIndex++;
	}

	public void addUserToGame(User u, Long gameTicket) {
		Game s = waitingGameMap.get(gameTicket);
		// TODO we need a class extends ISpieler
//		if (s != null)
//			s.addSpieler(u);
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
			UserService s = new UserService();
			gs.addSpieler(s.createSpieler("a bot"));
		}
		
		activeGames.put(gameTicket, g);
		return gameTicket; // TODO should be the db id...
	}

	public Collection<Game> getAvailableGames(){
		return waitingGameMap.values();
	}
	
}
