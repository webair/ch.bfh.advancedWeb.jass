package ch.frickler.jass;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import ch.frickler.jass.logic.Player;

@ManagedBean
@SessionScoped
public class GameBean {

	Long gameId = null;

	@ManagedProperty(value = "#{userBean}")
	private UserBean user;

	/**
	 * the setter for the injection
	 * 
	 * @param u
	 */
	public void setUser(UserBean u) {
		user = u;
	}

	private long getGameId() {
		if (gameId == null) {
			gameId = (Long) FacesContext.getCurrentInstance()
					.getExternalContext().getSessionMap()
					.get(GameManager.GAME_ID_KEY);
		}
		return gameId;
	}

	public List<Player> getPlayers() {
		return GameManager.getInstance().getPlayers(getGameId());
	}

	public String[] getCards() {
		//TODO this should probably be returned by the user
		return new String[] { "Bube", "Dame", "König", "Gras" };
	}

}
