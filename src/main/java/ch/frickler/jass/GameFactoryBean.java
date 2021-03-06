package ch.frickler.jass;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

import ch.frickler.jass.helper.Translator;
import ch.frickler.jass.service.GameManagerService;

/**
 * 
 * Factory Bean to create new games
 * @author seed
 * 
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
	private int winningPoints = 500;

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
		GameManagerService gm = GameManagerService.getInstance();
		Long gameId = gm.createGame(getName(), user.getUser(),
				getWinningPoints());
		FacesContext ctx = FacesContext.getCurrentInstance();
		ctx.getExternalContext().getSessionMap()
				.put(GameManagerService.GAME_ID_KEY, gameId);
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
