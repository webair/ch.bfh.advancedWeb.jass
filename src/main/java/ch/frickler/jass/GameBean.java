package ch.frickler.jass;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import ch.frickler.jass.action.ActionAnnounce;
import ch.frickler.jass.action.ActionAnnounceWies;
import ch.frickler.jass.action.ActionLayCard;
import ch.frickler.jass.action.ActionLeaveGame;
import ch.frickler.jass.db.entity.Card;
import ch.frickler.jass.db.entity.Team;
import ch.frickler.jass.db.entity.User;
import ch.frickler.jass.db.entity.Wies;
import ch.frickler.jass.db.enums.CardFamily;
import ch.frickler.jass.db.enums.CardValue;
import ch.frickler.jass.db.enums.GameKind;
import ch.frickler.jass.db.enums.GameState;
import ch.frickler.jass.helper.Translator;
import ch.frickler.jass.service.GameManagerService;
import ch.frickler.jass.service.GameService;

/**
 * Bean for managing the game interactions and the game logic
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
	 * holds the cards from the last round, so we can present them for some
	 * seconds to the user
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
	 * holds the current wies
	 */
	private String wies = null;

	/**
	 * the setter for the injection
	 * 
	 * @param u
	 */
	public void setUser(UserBean u) {
		user = u;
	}
	
	public int showedLastCard = 0;

	/**
	 * @return the current gameid, reads it from the session when its not set.
	 */
	private long getGameId() {
		if (gameId == null) {
			System.out.println("GameId is relaoded");
			gameId = (Long) FacesContext.getCurrentInstance()
					.getExternalContext().getSessionMap()
					.get(GameManagerService.GAME_ID_KEY);
		}
		System.out.println("GameId is "+gameId);
		return gameId;
	}

	/**
	 * returns a list of players from the game service
	 * 
	 * @return list of players
	 */
	public List<User> getPlayers() {
		return getGameService().getAllPlayers();
	}

	/**
	 * returns a list of the teams from the game service
	 * 
	 * @return list of teams
	 */
	public List<Team> getTeams() {
		return getGameService().getTeams();
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
	 * starts a play action, reads the user input and determine the action
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
			new ActionLayCard(user.getUser(), found).doAction(GameManagerService
					.getInstance().getGameService(getGameId()));
		}
	}

	/**
	 * returns the played cards on the player table
	 * 
	 * @return list of cards
	 */
	public List<Card> getCardsOnTable() {

		GameService gs = getGameService();
		// if the last for cards changed, display them...
		List<Card> lastCards = gs.getLastCards();
		if (lastCards != null && !lastCards.equals(this.lastCards) || showedLastCard > 0) {
			if (showedLastCard == 0)
				this.lastCards = lastCards;
			
			if (showedLastCard > 1) {
				showedLastCard = 0;
			} else {
				showedLastCard++;
			}
			
			return lastCards;

		}

		User beginner = gs.getCurrentRound().getBeginner();
		List<User> sortedUsers = gs.getAllPlayersSorted(beginner);
		myIndex = sortedUsers.indexOf(user.getUser());

		return gs.getCurrentRound().getCards();
	}

	public GameKind[] getGameKinds() {
		return GameKind.values();
	}

	public GameService getGameService() {

		if (_gameService == null) {
			_gameService = GameManagerService.getInstance()
					.getGameService(getGameId());
		}
		return _gameService;
	}

	GameService _gameService = null;

	/**
	 * 
	 * @return all game kinds translated in the current language
	 */
	public SelectItem[] getGameKindsTranslated() {
		SelectItem[] kinds = new SelectItem[GameKind.values().length];
		int i = 0;
		for (GameKind kind : GameKind.values()) {
			kinds[i++] = new SelectItem(kind.name(), Translator.getString(
					FacesContext.getCurrentInstance(), kind.name()));
		}
		return kinds;
	}

	/**
	 * 
	 * @return all game kinds translated in the current language
	 */
	public List<SelectItem> getPossibleWiesTranslated() {
		List<Wies> possibleWies = Wies.getPossibleWies(user.getUser());
		List<SelectItem> allWies = new ArrayList<SelectItem>();
		//int i = 0;
		for (Wies w : possibleWies) {
			// System.out.println(w.getKey());
			if(!alreadyWiesed(w))
			allWies.add(new SelectItem(w.getKey(), w.getKey()));
			// Translator.getString(
			// FacesContext.getCurrentInstance(), w.getName()));
		}
		return allWies;
	}

	private boolean alreadyWiesed(Wies w) {
		
		getGameService().isWiesAnnonced(w);
		
		return false;
	}

	/**
	 * @return return if user is the current announcer (Ansager)
	 */
	public boolean isAnnouncer() {
		GameService gs = getGameService();
		if (gs.getState().equals(GameState.Ansage)) {
			return gs.getAnnouncer().equals(user.getUser());
		}
		return false;
	}

	/**
	 * @return the current trump, determined by the game service
	 */
	public String getTrump() {
		GameService gs = getGameService();
		if (gs.getState().equals(GameState.Play)) {
			return gs.getCurrentRound().getGameKind().toString();
		}
		return null;
	}

	public void setTrump(String trump) {
		this.trump = trump;
	}

	public void setWies(String wies) {
		this.wies = wies;
	}

	public String getWies() {
		return this.wies;
	}

	public String getLangTrump() {
		GameService gs = getGameService();
		if (gs.getCurrentRound() != null) {
			GameKind k = gs.getCurrentRound().getGameKind();
			if (k != null) {
				return Translator.getString(FacesContext.getCurrentInstance(),
						k.name());
			}
		}
		return Translator.getString(FacesContext.getCurrentInstance(),
				"notyetchoosed");
	}

	/**
	 * announce the current trump
	 */
	public void announce() {
		GameKind gk = GameKind.valueOf(trump);
		GameService gs = getGameService();
		new ActionAnnounce(user.getUser(), gk).doAction(gs);
	}

	public List<String> getLog() {
		GameService gs = getGameService();
		return gs.getLog();
	}

	/**
	 * push the announcement (Schieben)
	 */
	public void push() {
		GameService gs = getGameService();
		gs.pushGame();
	}

	public String quitgame() {
		GameService gs = getGameService();
		new ActionLeaveGame(user.getUser()).doAction(gs);
		return quitgame("gameHistory?faces-redirect=true");
	}
	
	private String quitgame(String url) {
		gameId = null;
		_gameService = null;
		return url;
	}

	public String newgame(){
		return quitgame("createGame?faces-redirect=true");
	}
	
	public String showstatistic(){
		return quitgame("gameHistory?faces-redirect=true");
	}

	public boolean isFinished(){
		return getGameService().getState() == GameState.Terminated;
	}
	
	/**
	 * it must be the first round, and the user must have a wies in his hand
	 * cards.
	 * 
	 * @return true if user can wies
	 */
	public boolean isWiesPossible() {
		return user.getUser().getCards().size() == 9
				&& getPossibleWiesTranslated().size() > 0;
	}

	public void wiesen() {
		GameService gs = getGameService();
		new ActionAnnounceWies(user.getUser(), getAnouncedWies()).doAction(gs);

	}
	
	public String getTwittertext(){
		return getGameService().getTwitterText(user.getUser());
	}

	private List<Card> getAnouncedWies() {
		List<Card> cards = new ArrayList<Card>();
		try {
			String[] arr = wies.split(";");
			System.out.println("� wies isch mies: " + wies);
			for (String a : arr) {
				cards.add(new Card(CardFamily.valueOf(a.split(",")[0]),
						CardValue.valueOf(a.split(",")[1])));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return cards;
	}

	/**
	 * returns a list of gui cards with the right player index
	 * 
	 * @return list of cards
	 */
	public List<GuiCard> getGuiCards() {
		// System.out.println("index: " + myIndex);
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
	 *         inner class for adding player index to the card
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
