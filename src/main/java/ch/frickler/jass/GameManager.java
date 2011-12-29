package ch.frickler.jass;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.frickler.jass.db.entity.Game;
import ch.frickler.jass.db.entity.User;
import ch.frickler.jass.service.GameService;
import ch.frickler.jass.service.UserService;

/**
 * @author seed
 * 
 * class for managing the games
 */
public class GameManager {

	/**
	 *  constant for game id key
	 */
	public static final String GAME_ID_KEY = "currentGameId";

	/**
	 * holds game manager instance (singleton)
	 */
	private static GameManager instance;

	/**
	 *  Map of games <id , game>
	 */
	private Map<Long, Game> games = new HashMap<Long, Game>();

	/**
	 * services holds the game service instances <game id, service>
	 * pretty but ugly hack for preventing multiples entitymanagers
	 */
	private Map<Long, GameService> services = new HashMap<Long, GameService>();

	/**
	 * holds the current amount of games
	 */
	private long gameCount = 0L;

	private GameManager() {

	}

	/**
	 * singleton method, for instantiating a game manager
	 * 
	 * @return instance of GameManager
	 */
	public static GameManager getInstance() {
		if (instance == null) {
			instance = new GameManager();
		}
		return instance;

	}

	/**
	 * @param gameId
	 * @return returns the game for the given game id
	 */
	public Game getGame(long gameId) {
		return games.get(gameId);
	}

	/**
	 * creates and append a game to the waiting list
	 * 
	 * @param name
	 * @param winningPoints
	 * @return this games "gameTicket" (use it to start the game)
	 */
	public long createGame(String name, User creator, int winningPoints) {
		Game g = new Game(name, creator, null, null, winningPoints);

		games.put(gameCount, g);
		addUserToGame(creator, gameCount);
		return gameCount++;
	}

	/**
	 * @param u user
	 * @param gameId
	 * 
	 * add a player to the game with the given id
	 */
	public void addUserToGame(User u, long gameId) {
		GameService gs = getGameService(gameId);
		if (gs != null)
			gs.addSpieler(u);
	}

	/**
	 * @param gameId
	 * @return the game service for the given game id
	 */
	public GameService getGameService(long gameId) {
		GameService gs = services.get(gameId);
		if (gs != null)
			return gs;
		Game g = games.get(gameId);
		if (g != null) {
			gs = new GameService(g);
			services.put(gameId, gs);
			return gs;
		}
		return null;
	}

	/**
	 * determine if game is ready (all 4 players are added to the game)
	 * 
	 * @param gameId
	 * @return
	 */
	public boolean gameIsReady(long gameId) {
		GameService gs = getGameService(gameId);
		if (gs != null)
			return gs.getState().equals(Game.GameState.Ansage);
		return false;
	}

	/**
	 * action for starting the game with the given game id
	 * 
	 * @param gameId
	 * 
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
	
	public  List<Game> getCompletedGames(){
		return null;
	}
}
