package ch.frickler.jass.service;

import java.util.List;

import ch.frickler.jass.db.entity.Game;
import ch.frickler.jass.db.entity.User;

/**
 * Singleton class for getting the history of completed games 
 * @author seed
 * 
 */
public class GameHistoryService extends PersistanceService {

	private static GameHistoryService instance;

	private GameHistoryService() {

	}

	/**
	 * Singleton creator method
	 * 
	 * @return instance of Gamehistory
	 */
	public static GameHistoryService getInstance() {
		if (instance == null) {
			instance = new GameHistoryService();
		}
		return instance;
	}

	/**
	 * returns a list of completed games where the given user played
	 * 
	 * @param user
	 * @return List of games
	 */
	public List<Game> getCompletedGames(User user) {
		List<Game> g = getEm()
				.createQuery(
						"select G from Game G inner join G.team1 t1 inner join G.team2 t2 where t1.user1.id = :id or t1.user2.id = :id or t2.user1.id = :id or t2.user2.id = :id",
						Game.class).setParameter("id", user.getId())
				.getResultList();
		return g;
	}

}
