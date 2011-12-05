package ch.frickler.jass;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import ch.frickler.jass.db.entity.Card;
import ch.frickler.jass.db.entity.User;
import ch.frickler.jass.service.JUALayCard;

@ManagedBean
@SessionScoped
public class GameBean {

	private Long gameId = null;

	// just for demo purposes
	private List<Card> deck = new ArrayList<Card>();

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
			deck.add(found);
		}
		// cards.remove(cardId);
		// deck.add(cardId);
	}

	public List<Card> getDeck() {
		return GameManager.getInstance().getGameService(getGameId())
				.getCurrentRound().getCards();
	}

}
