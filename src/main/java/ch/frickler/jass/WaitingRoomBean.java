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
import ch.frickler.jass.service.GameManagerService;
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
					.get(GameManagerService.GAME_ID_KEY);
		}
		return gameId;
	}

	/**
	 * @return game instance
	 */
	public Game getGame() {
		return GameManagerService.getInstance().getGame(getGameId());
	}

	/**
	 * 
	 * @return list of all players for the current game
	 */
	public List<User> getPlayers() {
		return GameManagerService.getInstance().getGameService(getGameId())
				.getAllPlayers();
	}

	/**
	 * action for starting the game
	 * @return redirect to play page
	 */
	public String start() {
		System.out.println("lets start game "+getGameId());
		GameManagerService.getInstance().startGame(getGameId());
		
		String redirect = "playGame?faces-redirect=true&gameID="+getGameId();
		gameId = 0L;
		return redirect;
	}

	/**
	 * action for adding a bot to the given game
	 */
	public void addBot() {
		System.out.println("add computer for "+getGameId());
		GameManagerService.getInstance().addUserToGame(new UserService().createBot(),
				getGameId());
	}

	/**
	 * action for refreshing table with games
	 * 
	 * @return redirect for reloading page
	 */
	public String refresh() {
		if (GameManagerService.getInstance().gameIsReady(getGameId())) {
			gameId = 0L;
			FacesContext ctx = FacesContext.getCurrentInstance();
			System.out.println("Start game with id:"+getGameId()+" name "+ GameManagerService.getInstance()
					.getGameService(getGameId()).getName());
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
