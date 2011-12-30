package ch.frickler.jass;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import ch.frickler.jass.action.ActionAnnounce;
import ch.frickler.jass.action.ActionLayCard;
import ch.frickler.jass.db.entity.Card;
import ch.frickler.jass.db.entity.Game;
import ch.frickler.jass.db.entity.Game.GameState;
import ch.frickler.jass.db.entity.Team;
import ch.frickler.jass.db.entity.User;
import ch.frickler.jass.db.enums.CardFamily;
import ch.frickler.jass.db.enums.CardValue;
import ch.frickler.jass.db.enums.GameKind;
import ch.frickler.jass.service.GameService;

/**
 * @author seed
 * Bean for managing the game interactions and the game logic
 */
/**
 * @author seed
 *
 */
@ManagedBean
@SessionScoped
public class GameBean {

	/**
	 * holds the current game id
	 */
	private Long gameId = null;


	/**
	 * holds the cards from the last round, so we can present them
	 * for some seconds to the user
	 */
	private List<Card> lastCards;

	/**
	 * holds the player index 0=first ... 3=last
	 */
	private int myIndex;

	/**
	 * holds the user bean (injection)
	 */
	@ManagedProperty(value = "#{userBean}")
	private UserBean user;

	/**
	 * holds the current trump
	 */
	private String trump = null;

	/**
	 * the setter for the injection
	 * 
	 * @param u
	 */
	public void setUser(UserBean u) {
		user = u;
	}

	/**
	 * @return the current gameid, reads it from the session 
	 * when its not set.
	 */
	private long getGameId() {
		if (gameId == null) {
			gameId = (Long) FacesContext.getCurrentInstance()
					.getExternalContext().getSessionMap()
					.get(GameManager.GAME_ID_KEY);
		}
		return gameId;
	}

	/**
	 * returns a list of players from the game service
	 * 
	 * @return list of players
	 */
	public List<User> getPlayers() {
		return GameManager.getInstance().getGameService(getGameId())
				.getAllSpieler();
	}

	/**
	 * returns a list of the teams from the game service
	 * 
	 * @return list of teams
	 */
	public List<Team> getTeams() {
		return GameManager.getInstance().getGameService(getGameId()).getTeams();
	}

	/**
	 * returns a list of the users card
	 * 
	 * @return list of cards
	 */
	public List<Card> getCards() {
		return user.getUser().getCards();
	}

	
	/**
	 *  starts a play action, reads the user input and determine 
	 *  the action
	 */
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
			new ActionLayCard(user.getUser(), found).doAction(GameManager
					.getInstance().getGameService(getGameId()));
		}
	}

	/**
	 * returns the played cards on the player table
	 * 
	 * @return list of cards
	 */
	public List<Card> getCardsOnTable() {

		GameService gs = GameManager.getInstance().getGameService(getGameId());
		// if the last for cards changed, display them...
		List<Card> lastCards = gs.getLastCards();
		if (lastCards != null && !lastCards.equals(this.lastCards)) {
			this.lastCards = lastCards;
			return lastCards;

		}

		User beginner = gs.getCurrentRound().getBeginner();
		List<User> sortedUsers = gs.getAllSpielerSorted(beginner);
		myIndex = sortedUsers.indexOf(user.getUser());

		return gs.getCurrentRound().getCards();
	}

	public GameKind[] getGameKinds() {
		return GameKind.values();
	}

	/**
	 * @return return if user is the current announcer (Ansager)
	 */
	public boolean isAnnouncer() {
		GameService gs = GameManager.getInstance().getGameService(getGameId());
		if (gs.getState().equals(Game.GameState.Ansage)) {
			return gs.getAnsager().equals(user.getUser());
		}
		return false;
	}

	/**
	 * @return the current trump, determined by the game service
	 */
	public String getTrump() {
		GameService gs = GameManager.getInstance().getGameService(getGameId());
		if (gs.getState().equals(GameState.Play)) {
			return gs.getCurrentRound().getGameKind().toString();
		}
		return null;
	}

	public void setTrump(String trump) {
		this.trump = trump;
	}

	/**
	 * announce the current trump
	 */
	public void announce() {
		GameKind gk = GameKind.valueOf(trump);
		GameService gs = GameManager.getInstance().getGameService(getGameId());
		new ActionAnnounce(user.getUser(), gk).doAction(gs);
	}

	public List<String> getLog() {
		GameService gs = GameManager.getInstance().getGameService(getGameId());
		return gs.getLog();
	}

	/**
	 *  push the announcement (Schieben)
	 */
	public void push() {
		GameService gs = GameManager.getInstance().getGameService(getGameId());
		gs.pushGame();
	}

	/**
	 * returns a list of gui cards with the right player index
	 * 
	 * @return list of cards
	 */
	public List<GuiCard> getGuiCards() {
		//System.out.println("index: " + myIndex);
		List<GuiCard> deckCards = new ArrayList<GuiCard>();
		int i = 4 - myIndex;
		for (Card c : getCardsOnTable()) {

			deckCards.add(new GuiCard(c, i % 4));
			i++;
		}
		return deckCards;
	}

	/**
	 * @author seed
	 *
	 * inner class for adding player index to the card
	 */
	public class GuiCard {
		
		
		/**
		 * holds the player indey
		 */
		private int position;
		
		
		/**
		 * holds the card entity
		 */
		private Card card;

		public GuiCard(Card c, int p) {
			card = c;
			position = p;
		}

		public int getPosition() {
			return position;
		}

		public CardValue getValue() {
			return card.getValue();
		}

		public CardFamily getFamily() {
			return card.getFamily();

		}

	}

}
