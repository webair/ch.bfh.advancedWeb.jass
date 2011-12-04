package ch.frickler.jass;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import ch.frickler.jass.logic.Player;

@ManagedBean
@SessionScoped
public class GameBean {

	private Long gameId = null;

	// just for demo purposes
	private List<String> cards = new ArrayList<String>();
	private List<String> deck = new ArrayList<String>();

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

	public GameBean() {
		cards.add("pik/6.jpeg");
		cards.add("herz/bube.jpeg");
		cards.add("herz/ass.jpeg");
		cards.add("kreuz/10.jpeg");
	}

	private long getGameId() {
		if (gameId == null) {
			gameId = (Long) FacesContext.getCurrentInstance()
					.getExternalContext().getSessionMap()
					.get(GameManager.GAME_ID_KEY);
		}
		return gameId;
	}

	public List<Player> getPlayers() {
		return GameManager.getInstance().getPlayers(getGameId());
	}

	public List<String> getCards() {
		return cards;
	}

	public void playCard() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		Map<String, String> params = ctx.getExternalContext()
				.getRequestParameterMap();
		String cardId = params.get("cardId");
		cards.remove(cardId);
		deck.add(cardId);
	}

	public List<String> getDeck() {
		return deck;
	}

}
