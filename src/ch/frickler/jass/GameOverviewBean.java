package ch.frickler.jass;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class GameOverviewBean {
	private int currentGame = 1;
	
	public List<GameBean> getGames() {
		List<GameBean> list = new ArrayList<GameBean>();
		int i;
		for (i=currentGame; i< currentGame + 10; i++) {
			GameBean game = new GameBean();
			game.setName("Game" + i);
			list.add(game);
		}
		currentGame = i;
	    return list;
	}
}
