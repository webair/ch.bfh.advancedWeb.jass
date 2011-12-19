package ch.frickler.jass;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import ch.frickler.jass.db.entity.Game;
import ch.frickler.jass.service.GameHistoryService;

@ManagedBean
public class GameHistoryBean {
	
	@ManagedProperty(value = "#{userBean}")
	private UserBean userBean;

	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}
	
	public List<Game> getCompletedGames(){
		GameHistoryService b = GameHistoryService.getInstance();		
		return b.getCompletedGames(userBean.getUser());
	}

}
