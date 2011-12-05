package ch.frickler.jass;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ch.frickler.jass.db.entity.Game;
import ch.frickler.jass.db.entity.User;
import ch.frickler.jass.service.GameService;
import ch.frickler.jass.service.UserService;

public class GameManager {

	public static final String GAME_ID_KEY = "currentGameId";

	private static GameManager instance;

	private Map<Long, Game> games = new HashMap<Long, Game>();

	private long gameId = 0L;

	private GameManager() {

	}

	public static GameManager getInstance() {
		if (instance == null) {
			instance = new GameManager();
		}
		return instance;

	}

	public Game getGame(long gameId) {
		return games.get(gameId);
	}

	/**
	 * creates a new game and adds it to the waitinglist
	 * 
	 * @param name
	 * @param winPoints
	 * @return this games "gameTicket" (use it to start the game)
	 */
	public long createGame(String name, User owner, int winPoints) {
		Game g = new Game(name, owner, null, null, winPoints);

		games.put(gameId, g);
		addUserToGame(owner, gameId);
		return gameId++;
	}

	public void addUserToGame(User u, long gameId) {
		GameService gs = getGameService(gameId);
		if (gs != null)
			gs.addSpieler(u);
	}

	public GameService getGameService(long gameId) {
		Game g = games.get(gameId);
		if (g != null) {
			GameService gs = new GameService(g);
			return gs;
		}
		return null;
	}

	/**
	 * a game is ready, when there are 4 players
	 * 
	 * @param gameId
	 * @return
	 */
	public boolean gameIsReady(long gameId) {
		GameService gs = getGameService(gameId);
		if (gs != null)
			return gs.getState().equals(Game.GameState.WaitForCards);
		return false;
	}

	/**
	 * 
	 * @param gameTicket
	 * @return
	 */
	public void startGame(Long gameId) {
		GameService gs = getGameService(gameId);
		while (gs.getAllSpieler().size() < 4) {
			gs.addSpieler(new UserService().createBot());
		}
	}

	public Collection<Game> getAvailableGames() {
		return games.values();
	}

}
