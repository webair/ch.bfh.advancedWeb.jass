package ch.frickler.jass;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;

import ch.frickler.jass.logic.Spiel;

@ManagedBean
public class GameOverviewBean {
	
	/**
	 * 
	 * @return a list of the current (and not yet started) games
	 */
	public List<GameBean> getGames() {
		// TODO first, add a game id, second this is pretty stupid
		List<GameBean> list = new ArrayList<GameBean>();
		int i=1;
		for (Spiel s : GameManager.getInstance().getAvailableGames()) {
			GameBean game = new GameBean();
			game.setName(s.getName());
			game.setId(i++);
			list.add(game);
		}
	    return list;
	}
}
