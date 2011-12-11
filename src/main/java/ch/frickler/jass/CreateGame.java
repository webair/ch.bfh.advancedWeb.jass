package ch.frickler.jass;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

@ManagedBean
public class CreateGame {

	//TODO remove this value
	private String name="defölt";

	@ManagedProperty(value = "#{userBean}")
	private UserBean userBean;
 
	/**
	 * the setter for the injection
	 * 
	 * @param u
	 */
	public void setUserBean(UserBean u) {
		userBean = u;
	}

	//TODO remove default points
	private int winPoints=1000;

	public String create() {
		GameManager gm = GameManager.getInstance();
		Long gameId = gm.createGame(getName(), userBean.getUser(), getWinPoints());
		FacesContext ctx = FacesContext.getCurrentInstance();
		ctx.getExternalContext().getSessionMap()
				.put(GameManager.GAME_ID_KEY, gameId);
		return "waitForPlayers?faces-redirect=true";
	}

	// Getters & Setters

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getWinPoints() {
		return winPoints;
	}

	public void setWinPoints(int winPoints) {
		this.winPoints = winPoints;
	}

}
