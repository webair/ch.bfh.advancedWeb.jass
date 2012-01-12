package ch.frickler.jass;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

import ch.frickler.jass.db.entity.Game;
import ch.frickler.jass.helper.Translator;
import ch.frickler.jass.service.GameManagerService;



/**
 * 
 * Class to manage the list games page and actions to join or create a new game
 * @author seed
 *
 */
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
		list.addAll(GameManagerService.getInstance().getAvailableGames());
		return list;
	}
	
	public int getGameCount(){
		return getGames().size();
	}

	/**
	 * action for joining the game
	 * 
	 * @return redirect string to waitingRoom page
	 */
	public String joinGame() {
		// only join a game if not already playing
		if (userBean.isPlaying()) {
			System.out.println("user is already playing cannot join.");
			FacesContext ctx = FacesContext.getCurrentInstance();
			ctx.addMessage(null,
					Translator.getMessage(ctx, "already_playing"));
			return null;
		}
		FacesContext ctx = FacesContext.getCurrentInstance();
		Map<String, String> params = ctx.getExternalContext()
				.getRequestParameterMap();
		long gameId = Integer.parseInt(params.get("gameId"));
		System.out.println("User wants to join game: "+gameId);
		GameManagerService.getInstance().addUserToGame(userBean.getUser(), gameId);
		ctx.getExternalContext().getSessionMap()
				.put(GameManagerService.GAME_ID_KEY, gameId);
		return "waitingRoom?faces-redirect=true";
	}
}