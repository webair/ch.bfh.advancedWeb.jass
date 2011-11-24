package ch.frickler.jass;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

@ManagedBean
public class GameOverviewBean {
	
	public List<GameBean> getGames() {
		List<GameBean> list = new ArrayList<GameBean>();
		for (int i=0; i< 10; i++) {
			GameBean game = new GameBean();
			game.setName("Game" + i);
			game.setId(i + 1);
			list.add(game);
			
		}
	    return list;
	}
}
