package ch.frickler.jass;

import java.io.IOException;
import java.util.List;

import javax.faces.FacesException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import ch.frickler.jass.db.entity.Game;
import ch.frickler.jass.db.entity.User;
import ch.frickler.jass.service.UserService;

/**
 * @author seed
 *
 * Bean for handling the waiting room page
 */
@ManagedBean
@SessionScoped
public class WaitingRoomBean {
	/**
	 * holds the current game id
	 */
	long gameId = 0L;

	/**
	 * @return returns the id of the current game, looked up in the session
	 */
	public long getGameId() {
		if (gameId == 0L) {
			gameId = (Long) FacesContext.getCurrentInstance()
					.getExternalContext().getSessionMap()
					.get(GameManager.GAME_ID_KEY);
		}
		return gameId;
	}

	/**
	 * @return game instance
	 */
	public Game getGame() {
		return GameManager.getInstance().getGame(getGameId());
	}

	/**
	 * 
	 * @return list of all players for the current game
	 */
	public List<User> getPlayers() {
		return GameManager.getInstance().getGameService(getGameId())
				.getAllSpieler();
	}

	/**
	 * action for starting the game
	 * @return redirect to play page
	 */
	public String start() {
		GameManager.getInstance().startGame(getGameId());
		return "playGame?faces-redirect=true";
	}

	/**
	 * action for adding a bot to the given game
	 */
	public void addBot() {
		GameManager.getInstance().addUserToGame(new UserService().createBot(),
				getGameId());
	}

	/**
	 * action for refreshing table with games
	 * 
	 * @return redirect for reloading page
	 */
	public String refresh() {
		if (GameManager.getInstance().gameIsReady(getGameId())) {
			FacesContext ctx = FacesContext.getCurrentInstance();

			ExternalContext extContext = ctx.getExternalContext();
			String url = extContext.encodeActionURL(ctx.getApplication()
					.getViewHandler()
					.getActionURL(ctx, "/restricted/playGame.xhtml"));
			try {
				extContext.redirect(url);
			} catch (IOException ioe) {
				throw new FacesException(ioe);

			}
		}
		return null; // stay on the page
	}
}
