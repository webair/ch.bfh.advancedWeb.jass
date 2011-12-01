package ch.frickler.jass;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ch.frickler.jass.db.entity.User;
import ch.frickler.jass.logic.Spiel;
import ch.frickler.jass.logic.Spieler;

public class GameManager {

	// for refacotring
	public static final String GAME_ID_KEY = "currentGameId";

	private static GameManager instance;

	private Long currentwaitingGameMapIndex = 1L;
	private Map<Long, Spiel> waitingGameMap = new HashMap<Long, Spiel>();
	private Map<Long, Spiel> activeGames = new HashMap<Long, Spiel>();
	
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
		Spiel s = new Spiel(name);
		s.setWinPoints(winPoints);

		waitingGameMap.put(currentwaitingGameMapIndex, s);
		return currentwaitingGameMapIndex++;
	}

	public void addUserToGame(User u, Long gameTicket) {
		Spiel s = waitingGameMap.get(gameTicket);
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
		Spiel s = waitingGameMap.get(gameId);
		return (s != null && s.getAllSpieler().size() == 4);
	}

	/**
	 * 
	 * @param gameTicket
	 * @return
	 */
	public Long startGame(Long gameTicket){
		Spiel s = waitingGameMap.remove(gameTicket);
		if(s==null)
			return -1L;
		
		// fill the game with computer players
		while(s.getAllSpieler().size() < 4)
			s.addSpieler(new Spieler("a bot"));
		
		activeGames.put(gameTicket, s);
		return gameTicket; // TODO should be the db id...
	}

	public Collection<Spiel> getAvailableGames(){
		return waitingGameMap.values();
	}
	
}
