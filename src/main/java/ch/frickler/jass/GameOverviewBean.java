package ch.frickler.jass;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;

import ch.frickler.jass.db.entity.Game;


@ManagedBean
public class GameOverviewBean {
	
	/**
	 * 
	 * @return a list of the current (and not yet started) games
	 */
	public List<Game> getGames() {
		List<Game> list = new ArrayList<Game>();
		list.addAll(GameManager.getInstance().getAvailableGames());
	    return list;
	}
}
