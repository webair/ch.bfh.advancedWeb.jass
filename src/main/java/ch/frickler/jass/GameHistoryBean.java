package ch.frickler.jass;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import ch.frickler.jass.db.entity.Game;
import ch.frickler.jass.service.GameHistoryService;

/**
 * Class to manage the history page
 * @author seed
 *
 * 
 */
@ManagedBean
public class GameHistoryBean {
	
	/**
	 * holds the user bean (injection)
	 */
	@ManagedProperty(value = "#{userBean}")
	private UserBean userBean;

	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}
	
	/**
	 * reads all completed games and returns them
	 * 
	 * @return list of all completed games
	 */
	public List<Game> getCompletedGames(){
		GameHistoryService b = GameHistoryService.getInstance();		
		return b.getCompletedGames(userBean.getUser());
	}

}
