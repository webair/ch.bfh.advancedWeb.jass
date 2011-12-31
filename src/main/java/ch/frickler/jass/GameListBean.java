package ch.frickler.jass;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

import ch.frickler.jass.db.entity.Game;
import ch.frickler.jass.helper.Translator;

@ManagedBean
public class GameListBean {

	/**
	 * holds the user bean (injection)
	 */
	@ManagedProperty(value = "#{userBean}")
	private UserBean userBean;

	/**
	 * injection setter
	 * 
	 * @param u
	 */
	public void setUserBean(UserBean u) {
		userBean = u;
	}

	/**
	 * 
	 * @return a list of games in waiting queue
	 */
	public List<Game> getGames() {
		List<Game> list = new ArrayList<Game>();
		list.addAll(GameManager.getInstance().getAvailableGames());
		return list;
	}

	/**
	 * action for joining the game
	 * 
	 * @return redirect string to waitingRoom page
	 */
	public String joinGame() {
		// only join a game if not already playing
		if (userBean.isPlaying()) {
			FacesContext ctx = FacesContext.getCurrentInstance();
			ctx.addMessage(null,
					Translator.getMessage(ctx, "already_playing"));
			return null;
		}
		FacesContext ctx = FacesContext.getCurrentInstance();
		Map<String, String> params = ctx.getExternalContext()
				.getRequestParameterMap();
		long gameId = Integer.parseInt(params.get("gameId"));
		GameManager.getInstance().addUserToGame(userBean.getUser(), gameId);
		ctx.getExternalContext().getSessionMap()
				.put(GameManager.GAME_ID_KEY, gameId);
		return "waitingRoom?faces-redirect=true";
	}
}