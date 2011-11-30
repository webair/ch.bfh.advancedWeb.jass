package ch.frickler.jass;

import java.util.HashMap;
import java.util.Map;

import ch.frickler.jass.db.entity.Game;
import ch.frickler.jass.db.entity.Team;
import ch.frickler.jass.db.entity.User;



public class GameManager {

	//for refacotring
	public static final String GAME_ID_KEY = "currentGameId";
	
	private static GameManager instance;
	
	private Long currentwaitingGameMapIndex = 1L;
	private Map<Long,Game> waitingGameMap = new HashMap<Long, Game>();
	
	private GameManager() {
		
	}
	
	public static GameManager getInstance() {
		if (instance == null) {
			instance = new GameManager();
		}
		return instance;
		
	}
	
	public long createGame(String name, int winPoints) {
		Game g = new Game();
		g.setName(name);
		g.setWinPoints(winPoints);
		waitingGameMap.put(currentwaitingGameMapIndex, g);
		
		return currentwaitingGameMapIndex++;
	}
	
	public Game getGame(Long id) {
		return waitingGameMap.get(id);
	}
	
	public void addUserToGame(User u,Long id) {
		Game g = getGame(id);
		Team t1 = g.getTeam1();
		Team t2 = g.getTeam2();
		if (t1 == null) {
			Team t = new Team();
			t.setUser1(u);
		} else {
			if (t1.getUser2() == null) {
				t1.setUser2(u);
			} else if (t2 == null){
				Team t = new Team();
				t.setUser1(u);
			} else if (t2.getUser2() == null) {
				t2.setUser2(u);
			}
		}

	}
	
}
