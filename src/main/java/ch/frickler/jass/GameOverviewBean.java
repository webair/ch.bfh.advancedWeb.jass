package ch.frickler.jass;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

import ch.frickler.jass.db.entity.Game;
import ch.frickler.jass.helper.MessageHelper;

@ManagedBean
public class GameOverviewBean {

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

	/**
	 * 
	 * @return a list of the current (and not yet started) games
	 */
	public List<Game> getGames() {
		List<Game> list = new ArrayList<Game>();
		list.addAll(GameManager.getInstance().getAvailableGames());
		return list;
	}

	public String joinGame() {
		// only join a game if not already playing
		if (userBean.isPlaying()) {
			FacesContext ctx = FacesContext.getCurrentInstance();
			ctx.addMessage(null,
					MessageHelper.getMessage(ctx, "already_playing"));
			return null;
		}
		FacesContext ctx = FacesContext.getCurrentInstance();
		Map<String, String> params = ctx.getExternalContext()
				.getRequestParameterMap();
		long gameId = Integer.parseInt(params.get("gameId"));
		GameManager.getInstance().addUserToGame(userBean.getUser(), gameId);
		ctx.getExternalContext().getSessionMap()
				.put(GameManager.GAME_ID_KEY, gameId);
		return "waitForPlayers?faces-redirect=true";
	}
}