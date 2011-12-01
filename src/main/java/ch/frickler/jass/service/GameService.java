package ch.frickler.jass.service;

import ch.frickler.jass.db.entity.Game;

public class GameService extends PersistanceService {
	
	public Game createGame(String name, Integer winPoints) {
		Game g = new Game();
		g.setWinPoints(winPoints);
		g = mergeObject(g);
		return g;
	}

	public Game loadGame(Long userId) {
		return loadObject(Game.class, userId);
	}

}
