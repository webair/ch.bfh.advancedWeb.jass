package ch.frickler.jass;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;


import ch.frickler.jass.db.entity.Card;
import ch.frickler.jass.db.entity.Game;
import ch.frickler.jass.db.entity.Game.GameState;
import ch.frickler.jass.db.entity.Team;
import ch.frickler.jass.db.entity.User;
import ch.frickler.jass.db.enums.CardFamily;
import ch.frickler.jass.db.enums.CardValue;
import ch.frickler.jass.db.enums.GameKind;
import ch.frickler.jass.service.GameService;
import ch.frickler.jass.service.JUAAnsagen;
import ch.frickler.jass.service.JUALayCard;

@ManagedBean
@SessionScoped
public class GameBean {

	private Long gameId = null;
	
	// we store the four last cards to redisplay them 
	// (always the last round)
	private List<Card> lastCards;
	
	private int myIndex;

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
	
	public List<Team> getTeams() {
		return GameManager.getInstance().getGameService(getGameId())
				.getTeams();
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
		
		GameService gs = GameManager.getInstance().getGameService(getGameId());
		// if the last for cards changed, display them... 
		List<Card> lastCards = gs.getLastCards();
		if(lastCards!=null && !lastCards.equals(this.lastCards)){
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

	public boolean isAnsager() {
		GameService gs = GameManager.getInstance().getGameService(getGameId());
		if (gs.getState().equals(Game.GameState.Ansage)) {
			return gs.getAnsager().equals(user.getUser());
		}
		return false;
	}

	public String getTrump() {
		GameService gs = GameManager.getInstance().getGameService(getGameId());
		if(gs.getState().equals(GameState.Play)){
			return gs.getCurrentRound().getGameKind().toString();
		} 
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

	public List<String> getLog(){
		GameService gs = GameManager.getInstance().getGameService(getGameId());
		return gs.getLog();
	}
	
	public void schieben() {
		GameService gs = GameManager.getInstance().getGameService(getGameId());
		gs.pushGame();
	}
	
	public List<GuiCard> getCardsSorted() {
		System.out.println("index: " + myIndex);
		List<GuiCard> deckCards = new ArrayList<GuiCard>();
		int i = 4-myIndex;
		for (Card c : getDeck()) {
			
			deckCards.add(new GuiCard(c,i%4));
			i++;
		}
		return deckCards;
	}
	
	public class GuiCard {
		
		private int position;
		private Card card;
		
		public GuiCard(Card c,int p) {
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
