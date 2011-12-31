package ch.frickler.jass;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

import ch.frickler.jass.helper.Translator;

/**
 * @author seed
 * 
 * Bean to create a new game
 */
@ManagedBean
public class GameFactoryBean {

	/**
	 * holds the game name
	 */
	private String name = "";

	/**
	 * holds the user bean (injection)
	 */
	@ManagedProperty(value = "#{userBean}")
	private UserBean user;

	public void setUser(UserBean u) {
		user = u;
	}

	/**
	 * winning points
	 */
	private int winningPoints = 1000;

	/**
	 * initializes a new game, with the winningPoints & name setted by the user
	 * 
	 * @return redirect to waitingRoom
	 */
	public String create() {
		// when user is playing he can't create a game
		if (user.getUser().isPlaying()) {
			FacesContext ctx = FacesContext.getCurrentInstance();
			ctx.addMessage(null,
					Translator.getMessage(ctx, "already_playing"));
			return null;
		}
		GameManager gm = GameManager.getInstance();
		Long gameId = gm.createGame(getName(), user.getUser(),
				getWinningPoints());
		FacesContext ctx = FacesContext.getCurrentInstance();
		ctx.getExternalContext().getSessionMap()
				.put(GameManager.GAME_ID_KEY, gameId);
		return "waitingRoom?faces-redirect=true";
	}

	// Getters & Setters

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getWinningPoints() {
		return winningPoints;
	}

	public void setWinningPoints(int winPoints) {
		this.winningPoints = winPoints;
	}

}
