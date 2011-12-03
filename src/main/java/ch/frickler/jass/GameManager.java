package ch.frickler.jass;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.frickler.jass.logic.Bot;
import ch.frickler.jass.logic.Game;
import ch.frickler.jass.logic.Player;

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
	public long createGame(String name, Player owner, int winPoints) {
		Game g = new Game(gameId, name, owner, winPoints);

		games.put(gameId, g);
		addUserToGame(owner, gameId);
		return gameId++;
	}

	public void addUserToGame(Player p, long gameId) {
		Game g = games.get(gameId);
		if (g != null) {
			g.addPlayer(p);
		}
	}

	public List<Player> getPlayers(long gameId) {
		Game g = games.get(gameId);
		if (g != null)
			return g.getPlayers();
		return null;
	}

	/**
	 * a game is ready, when there are 4 players
	 * 
	 * @param gameId
	 * @return
	 */
	public boolean gameIsReady(long gameId) {
		Game g = games.get(gameId);
		return g.getPlayers().size() == 4;
	}

	/**
	 * 
	 * @param gameTicket
	 * @return
	 */
	public void startGame(Long gameTicket) {
		Game g = games.get(gameTicket);
		while (g.getPlayers().size() < 4) {
			g.addPlayer(new Bot());
		}
	}

	public Collection<Game> getAvailableGames() {
		return games.values();
	}

}
