package ch.frickler.jass;

import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import ch.frickler.jass.db.entity.Card;
import ch.frickler.jass.db.entity.Game;
import ch.frickler.jass.db.entity.User;
import ch.frickler.jass.db.enums.GameKind;
import ch.frickler.jass.service.GameService;
import ch.frickler.jass.service.JUAAnsagen;
import ch.frickler.jass.service.JUALayCard;

@ManagedBean
@SessionScoped
public class GameBean {

	private Long gameId = null;

	@ManagedProperty(value = "#{userBean}")
	private UserBean user;

	private String trump = null;

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

	public List<User> getPlayers() {
		return GameManager.getInstance().getGameService(getGameId())
				.getAllSpieler();
	}

	public List<Card> getCards() {
		return user.getUser().getCards();
	}

	public void playCard() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		Map<String, String> params = ctx.getExternalContext()
				.getRequestParameterMap();

		String family = params.get("cardFamily");
		String value = params.get("cardValue");
		Card found = null;
		for (Card c : user.getUser().getCards()) {
			if (c.getFamily().toString().equals(family)
					&& c.getValue().toString().equals(value)) {
				found = c;
				break;
			}
		}
		if (found != null) {
			new JUALayCard(user.getUser(), found).doAction(GameManager
					.getInstance().getGameService(getGameId()));
		}
	}

	public List<Card> getDeck() {
		return GameManager.getInstance().getGameService(getGameId())
				.getCurrentRound().getCards();
	}

	public GameKind[] getGameKinds() {
		return GameKind.values();
	}

	public boolean isAnsager() {
		GameService gs = GameManager.getInstance().getGameService(getGameId());
		if (gs.getState().equals(Game.GameState.Ansage)) {
			return gs.getCaller().equals(user.getUser());
		}
		return false;
	}

	public String getTrump() {
		return null;
	}

	public void setTrump(String trump) {
		this.trump = trump;
	}

	public void ansagen() {
		GameKind gk = GameKind.valueOf(trump);
		GameService gs = GameManager.getInstance().getGameService(getGameId());
		new JUAAnsagen(user.getUser(), gk).doAction(gs);
	}

}
