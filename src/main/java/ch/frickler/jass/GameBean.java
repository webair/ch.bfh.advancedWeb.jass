package ch.frickler.jass;

import java.util.ArrayList;
import java.util.List;

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
		cards.add("Bube");
		cards.add("Dame");
		cards.add("König");
		cards.add("Gras");
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
		//TODO ask the GameManager whether this user can play this card...
		// use <params> to get the cards id
		String c = cards.remove(0);
		deck.add(c);
	}
	
	public List<String> getDeck(){
		return deck;
	}

}
