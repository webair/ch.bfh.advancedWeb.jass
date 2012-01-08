package ch.frickler.jass.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.faces.context.FacesContext;

import ch.frickler.jass.action.ActionAnnounce;
import ch.frickler.jass.action.ActionDealOutCards;
import ch.frickler.jass.action.ActionLayCard;
import ch.frickler.jass.db.entity.Card;
import ch.frickler.jass.db.entity.Game;
import ch.frickler.jass.db.entity.Round;
import ch.frickler.jass.db.entity.Team;
import ch.frickler.jass.db.entity.User;
import ch.frickler.jass.db.entity.Wies;
import ch.frickler.jass.db.enums.CardFamily;
import ch.frickler.jass.db.enums.CardValue;
import ch.frickler.jass.db.enums.GameKind;
import ch.frickler.jass.db.enums.GameState;
import ch.frickler.jass.gametype.Trump;
import ch.frickler.jass.helper.Translator;
import ch.frickler.jass.definitions.JassAction;

/**
 * @author seed
 *
 * Class for connecting the game logic with the persistance store
 */
public class GameService extends PersistanceService {

	private Game _game;
	private GameTypeService gametypeService;
	private List<String> log = new LinkedList<String>();
	private List<Card> lastCards = null;

	public GameService(Game g) {
		this._game = g;
		if (g != null && g.getCurrentRound() != null
				&& g.getCurrentRound().getGameKind() != null)
			gametypeService = new GameTypeService(g.getCurrentRound()
					.getGameKind());
	}


	private void log(String msg) {
		log.add(0, msg);
		while (log.size() > 10)
			log.remove(log.size() - 1);
	}

	/**
	 * @return List of log messages
	 */
	public List<String> getLog() {
		return log;
	}

	/**
	 * game factory method, creates a new game with the given params
	 * 
	 * @param name
	 * @param winPoints
	 * @return the created game
	 */
	public Game createGame(String name, Integer winPoints) {
		Game g = new Game();
		g.setWinPoints(winPoints);
		g.setName(name);
		g.setGameState(GameState.WaitForPlayers);
		g.setStartDate(new Date());
		g = mergeObject(g);
		_game = g;
		return g;
	}

	/**
	 * loads serialized game from the database
	 * @param gameId
	 * @return a game instance
	 */
	public Game loadGame(Long gameId) {
		_game = loadObject(Game.class, gameId);
		return _game;
	}

	/***
	 * method to return all players
	 * @return List of players
	 */
	public List<User> getAllPlayers() {
		List<User> alleISpieler = new ArrayList<User>();
		for (Team t : _game.getTeams()) {
			alleISpieler.addAll(t.getUsers());
		}
		return alleISpieler;
	}

	/**
	 * method to return a list of all players sorted with the given user
	 * @param beginner
	 * @return list of players
	 */
	public List<User> getAllPlayersSorted(User beginner) {
		List<User> alleISpieler = getAllPlayers();

		if (alleISpieler.size() == 0)
			return alleISpieler;

		List<User> tempISpieler = new ArrayList<User>();

		int totalISpieler = alleISpieler.size();
		int ISpielerproteam = totalISpieler / Game.TEAMAMOUNT;
		for (int i = 0; i < ISpielerproteam; i++) {

			tempISpieler.add(_game.getTeam(0).getUsers().get(i));
			tempISpieler.add(_game.getTeam(1).getUsers().get(i));
		}
		int beginId = 0;
		if (beginner != null) {
			beginId = tempISpieler.indexOf(beginner);
			if (beginId < 0)
				beginId = 0;
		}
		List<User> tempISpieler2 = new ArrayList<User>();
		for (int i = beginId; i < beginId + totalISpieler; i++) {
			tempISpieler2.add(tempISpieler.get(i % totalISpieler));
		}

		return tempISpieler2;
	}

	/**
	 * add a player to the player list
	 * 
	 * @param player
	 * @throws RuntimeException
	 */
	public void addSpieler(User player) throws RuntimeException {
		if (getAllPlayers().size() >= Game.MAXUSER)
			throw new RuntimeException("wft! zu viele ISpieler");

		if (_game.getTeamAmount() < 2) {
			Team team = new Team(player);
			team.setPoints(0);
			team.setName("Team of " + player.getName());
			_game.addTeam(team);
		} else {
			if (getTeam(0).getUsers().size() < 2) {
				getTeam(0).addUser(player);
			} else {
				getTeam(1).addUser(player);
			}
		}
		mergeObject(_game); // store the created teams
		if (getAllPlayers().size() == Game.MAXUSER) {
			initGame();
			new ActionDealOutCards().doAction(this);
		}
	}

	/**
	 * method to return a team (0 or 1)
	 * 
	 * @param i
	 * @return instance of Team
	 */
	private Team getTeam(int i) {
		return _game.getTeam(i);
	}
	
	/**
	 * method to determine the "Stich". the team with the highest card wins the round
	 * @param cards
	 * @return player with the highest card
	 */
	private User placeStich(List<Card> cards) {

		int highestCard = 0;
		for (int i = 1; i < cards.size(); i++) {
			if (isSecondCardHigher(cards.get(highestCard), cards.get(i))) {
				highestCard = i;
			}

		}
		List<User> spieler = getAllPlayersSorted(getCurrentRound()
				.getBeginner());
		User spl = spieler.get(highestCard % spieler.size());
		addCardsToTeam(spl, cards);

		getCurrentRound().setBeginner(spl);

		return spl;
	}

	/**
	 * helper method to determine if card ist higher
	 * @param card
	 * @param card2
	 * @return true is second card is higher
	 */
	private boolean isSecondCardHigher(Card card, Card card2) {
		return getGameTypeService().isSecondCardHigher(card, card2);
	}

	/**
	 * method to get the current round
	 * @return instance of Round
	 */
	public Round getCurrentRound() {
		return _game.getCurrentRound();
	}

	/**
	 * method to add cards to a player
	 * 
	 * @param player
	 * @param cards
	 */
	private void addCardsToTeam(User player, List<Card> cards) {

		Team t = getTeamOf(player);
		t.addCard(cards);
		System.out
				.println("Round " + cards.toString() + " goes to Team: "
						+ t.getName() + " highest Card of Player: "
						+ player.getName());
	}

	/**
	 * helper method to get the team by user
	 * @param user
	 * @return instance of Team, if no team is found null
	 */
	private Team getTeamOf(User user) {
		for (Team t : _game.getTeams()) {
			if (t.isUserInTeam(user)) {
				return t;
			}
		}
		return null;
	}

	/**
	 * method to set the current Round
	 * @param round
	 */
	public void setRound(Round round) {
		round.setPlayerWithStoeck(getSpielerWithStoeck());
		_game.setCurrentRound(round);

	}

	/**
	 * method to get the player who has the stoeck
	 * @return player which have the stoeck, or null if noone has
	 */
	private User getSpielerWithStoeck() {

		if (!(getGameTypeService().isTrumpf()))
			return null;

		CardFamily trumpfCardFamily = getGameTypeService()
				.getTrumpfCardFamily();
		for (User s : getAllPlayers()) {
			boolean hasTrumpfSchnegge = false;
			boolean hasTrumpfKing = false;
			for (Card c : s.getCards()) {
				if (c.getFamily().equals(trumpfCardFamily)) {

					if (c.getValue() == CardValue.DAME) {
						hasTrumpfSchnegge = true;
					} else if (c.getValue() == CardValue.KOENIG) {
						hasTrumpfSchnegge = true;
					}
				}
			}
			if (hasTrumpfKing && hasTrumpfSchnegge)
				return s;
		}
		return null;
	}

	/**
	 * method to finish round, and create the new round or finish the game
	 */
	private void finishRound() {
		System.out.println("Finished Round");
		calculateScore();

		// check wether the game has ended
		int winPoints = _game.getWinPoints();
		for (Team t : getTeams()) {
			if (t.getPoints() >= winPoints) {
				finishGame();
				return;
			}
		}

		int caller = _game.getCallerId();
		_game.setCallerId(++caller % this.getAllPlayers().size());
		setGameState(GameState.WaitForCards);
		// neue karten verteilen
		new ActionDealOutCards().doAction(this);
		// when we start a new round, the beginner is the ansager
		// and not the one that placed the last stich!
		getCurrentRound().setBeginner(getAnnouncer());
		getCurrentRound().setCurrentPlayer(getAnnouncer());
		forceBotTrump();
		
		
	}

	/**
	 * method to finish the game
	 */
	private void finishGame() {
		System.out.println("finish game");
		setGameState(GameState.Terminated);
		for (User u : getAllPlayers()) {
			u.setPlaying(false);
		}
	}

	/**
	 * helper method for force the bot to set the trump
	 */
	private void forceBotTrump() {
		User u = getAnnouncer();
		if (u.isABot()) {
			int num = GameKind.values().length;
			GameKind k = GameKind.values()[new Random().nextInt(num)];
			new ActionAnnounce(u, k).doAction(this);
			System.out.println("New Trump is (by bot) " + k);
		}
		// now that we have a trump, let the first bot plays (if it's his turn)
		forceBotPlay();
	}

	/**
	 * method to calculate the score
	 * 
	 */
	private void calculateScore() {
		Round r = getCurrentRound();
		// round finished place points
		for (Team t : _game.getTeams()) {
			int pointsTeam = getGameTypeService().countPoints(t.getCards());
			// team hat den letzten Stich gemacht
			if (getTeamOf(r.getCurrentPlayer()).equals(t)) {
				pointsTeam += 5;
			}
			String msg = translateAndFormat("teammakespoint",
					new String[] { t.getName(), pointsTeam + "" });
			log(msg);
			System.out.println(msg);
			t.addPoints(pointsTeam * getGameTypeService().getQualifier());
			t.clearCards();
		}
		// console ounly
		for (Team t : _game.getTeams()) {
			System.out.println("Punktestand " + t.getName() + ": "
					+ t.getPoints());
		}
	}

	/**
	 * helper method to format strings
	 * @param key
	 * @param args
	 * @return formatted & translated key
	 */
	public String translateAndFormat(String key, String[] args) {
		String res = Translator.getString(FacesContext.getCurrentInstance(),
				key);
		if (res.length() == 0)
			return "Translation Key " + key + " not found";

		return String.format(res, args);
	}

	
	/**
	 * method to save state of played card
	 * @param player who plays a card
	 * @param layedCard card which player played
	 */
	public void playCard(User player, Card layedCard) {
		String card = translateCard(layedCard);
		String log = translateAndFormat("playerplayed",
				new String[] { player.getName(), card });
		System.out.println(log);
		log(log);
		Round r = getCurrentRound();
		r.addCard(layedCard);
		player.removeCard(layedCard);
		r.setCurrentPlayer(getAllPlayersSorted(player).get(1));
		if (allPlayerPlayed()) {
			finishHand();
			if (r.getCurrentPlayer().getCards().size() == 0) {
				finishRound();
			}
		}
		forceBotPlay();
	}

	/**
	 * helper method to translate the cards
	 * @param layedCard
	 * @return translated card
	 */
	private String translateCard(Card layedCard) {
		String resFamily = Translator.getString(FacesContext
				.getCurrentInstance(), layedCard.getFamily().toString());
		String resValue = Translator.getString(FacesContext
				.getCurrentInstance(), layedCard.getValue().toString());
		return resFamily + " " + resValue;
	}

	/**
	 * method to let the bot play
	 */
	public void forceBotPlay() {
		Round r = getCurrentRound();
		User u = r.getCurrentPlayer();
		boolean isCardValid = false;
		if (u.isABot() && u.getCards().size() > 0) {
			int i = 0;
			do {
				Card c = u.getCards().get(i);
				isCardValid = new ActionLayCard(u, c).doAction(this);
				i++;
			} while (!isCardValid);
		}
	}

	/**
	 * @return true if all player had played
	 */
	private boolean allPlayerPlayed() {
		return getCurrentRound().getCards().size() == 4;
	}

	/**
	 * method to finish the current hand
	 */
	private void finishHand() {
		System.out.println("finish hand");
		Round r = getCurrentRound();
		User spAnsager;
		spAnsager = this.placeStich(r.getCards());
		log(translateAndFormat("stichgoesto",
				new String[] { spAnsager.getName() }));
		r.setBeginner(spAnsager);
		r.setCurrentPlayer(spAnsager);
		lastCards = new ArrayList<Card>(r.getCards());
		r.removeCards();

		if (spAnsager.getCards().size() == 8) {
			placeWies(r.getAnnouncedWies());
			r.clearWies();
		}
		
	}

	/**
	 * method to set the "wies" from a player
	 * @param list
	 */
	private void placeWies(List<Wies> list) {
		if (list.size() == 0)
			return;
		Wies highest = list.get(0);

		for (int i = 1; i < list.size(); i++) {
			int compare = highest.compareTo(list.get(i));
			if (compare == 0) {
				if (getGameTypeService().isTrumpf()) {
					if (list.get(i).isTrumpf(
							getGameTypeService().getTrumpfCardFamily())) {
						highest = list.get(i);
					}
				}
			} else if (compare < 0)
				highest = list.get(i);
		}

		// all wies counts for the team with the highest wies.
		User user = highest.getUser();
		Team t = getTeamOf(user);
		int points = 0;
		for (User u : t.getUsers()) {
			for (Wies w : list) {
				if (w.getUser().equals(u)) {
					points += w.getPoints();
				}
			}
		}
		t.addPoints(points * gametypeService.getQualifier());
		String log = translateAndFormat(
				"andthewiesgoesto",
				new String[] { t.getName(),
						points * gametypeService.getQualifier() + "" });
		log(log);
		System.out.println(log);
	}

	private void initGame() {
		initNewRound();
		Round r = getCurrentRound();
		r.setBeginner(getAllPlayers().get(0));
		r.setCurrentPlayer(getAllPlayers().get(0));

		for (User u : getAllPlayers()) {
			u.setPlaying(true);
		}
	}

	private void initNewRound() {
		setGameState(GameState.WaitForCards);
		_game.setCurrentRound(new Round());
	}

	/**
	 * give the cards from the deck to the players
	 */
	public void arrangeCards() {
		List<Card> allCards = new ArrayList<Card>();

		if (getAllPlayers().size() == 0) {
			new RuntimeException("cannot card verteilen, no players found");
		}

		for (CardFamily family : CardFamily.values()) {
			for (CardValue value : CardValue.values()) {
				allCards.add(new Card(family, value));
			}
		}
		Collections.shuffle(allCards);

		while (!allCards.isEmpty()) {
			for (User spieler : getAllPlayers()) {
				spieler.addCard(allCards.remove(0));
			}
		}

		// sorting cards if user, shuffle if it is a roboter
		for (User u : getAllPlayers()) {
			if (u.isABot()) {
				Collections.shuffle(u.getCards());
			} else {
				Collections.sort(u.getCards());
			}
		}
		setGameState(GameState.Ansage);
	}

	/**
	 * @return instance of Player who is the announcer for the current round
	 */
	public User getAnnouncer() {
		int delta = getCurrentRound().isPushed() ? 2 : 0;
		return this.getAllPlayersSorted(null).get(
				(_game.getCallerId() + delta) % 4);
	}

	@SuppressWarnings("unused")
	private boolean isPlayedCardValid(User spl, Card layedCard, Round r) {

		return getGameTypeService().isPlayedCardVaild(spl, layedCard, r);
	}

	@SuppressWarnings("unused")
	private int getWinPoints() {

		return _game.getWinPoints();
	}

	@SuppressWarnings("unused")
	private Team getLeadingTeam() {
		Team leading = null;
		for (Team t : _game.getTeams()) {
			if (leading == null || leading.getPoints() < t.getPoints()) {
				leading = t;
			}
		}
		return leading;
	}

	private Team getLoosingTeam() {
		Team loosing = null;
		for (Team t : _game.getTeams()) {
			if (loosing == null || loosing.getPoints() > t.getPoints()) {
				loosing = t;
			}
		}
		return loosing;
	}

	/**
	 * the team witch doesn't terminate the game gets the points.
	 * 
	 * @param terminateUser
	 */
	public void cancelGame(User terminateUser) {
		String log = translateAndFormat("usercanceledgame",
				new String[] { terminateUser.getName() });
		// log("Trumpf is now: " + type + " says " + user.getName());
		log(log);

		Team teminateTeam = getTeamOf(terminateUser);
		for (Team winnerTeam : _game.getTeams()) {
			if (!teminateTeam.equals(winnerTeam)) {
				winnerTeam.addPoints(_game.getWinPoints()
						- winnerTeam.getPoints());
				break;
			}
		}
		finishGame();
	}

	protected void setGameState(GameState state) {
		_game.setGameState(state);
	}

	private void writeStatistic(Team winnerTeam, Team team) {
		// TODO Auto-generated method stub
	}

	/**
	 * @return instance of the GameState
	 */
	public GameState getState() {
		return _game.getGameState();
	}

	/**
	 * @param user
	 * @return true if usr has "Stoeck"
	 */
	public boolean addStoeck(User user) {
		Team t = getTeamOf(user);
		t.addPoints(Trump.VALUEOFSTOECK * getGameTypeService().getQualifier());
		return false;
	}

	private GameTypeService getGameTypeService() {
		return gametypeService;
	}

	/**
	 * command pattern method, to process a command.
	 * 
	 * @param action
	 * @return true if action is possible
	 */
	public boolean doAction(JassAction action) {

		if (action.isActionPossible(this)) {
			return action.doAction(this);
		}
		return false;
	}

	/**
	 * @param user
	 * @return true if user is the announcer
	 */
	public boolean isValidAnncouncer(User user) {
		if ((_game.getGameState() == GameState.Ansage || _game.getGameState() == GameState.AnsageGschobe)
				&& getAnnouncer().equals(user)) {
			return true;
		}
		return false;
	}

	/**
	 * @param layedCard
	 * @param user
	 * @return true if user can play the given card
	 */
	public boolean canPlayCard(Card layedCard, User user) {
		Round r = getCurrentRound();
		if (layedCard == null)
			return false;

		if (!r.getCurrentPlayer().equals(user)) {
			return false;
		}

		if (!_game.getGameState().equals(GameState.Play))
			return false;

		if (getGameTypeService() == null)
			return false; // no trump yet
		
		if(user.getCards().size() == 1)
			return true; // it was the last card.

		return isPlayedCardVaild(user, layedCard, r);
	}

	/**
	 * @param user
	 * @param layedCard
	 * @param r
	 * @return true if played card is valid
	 */
	public boolean isPlayedCardVaild(User user, Card layedCard, Round r) {
		return getGameTypeService().isPlayedCardVaild(user, layedCard, r);
	}

	/**
	 * @param user
	 * @return true if user has the "stoeck"
	 */
	public boolean hasUserStoeck(User user) {
		if (user == null || getCurrentRound().getPlayerWithStoeck() == null)
			return false;

		return getCurrentRound().getPlayerWithStoeck().equals(user);
	}

	/**
	 * method to set the current trump
	 * @param type
	 * @param user
	 */
	public void setTrump(GameKind type, User user) {
		setGameType(type);
		setGameState(GameState.Play);
		// remove pushed status if existent
		getCurrentRound().setPushed(false);

		String typTranslated = Translator.getString(
				FacesContext.getCurrentInstance(), type.toString());
		String log = translateAndFormat("trumpfisnow", new String[] {
				typTranslated, user.getName() });
		// log("Trumpf is now: " + type + " says " + user.getName());
		log(log);
	}

	/** method to set the current GameKind, 
	 * @param type
	 */
	public void setGameType(GameKind type) {
		getCurrentRound().setGameKind(type);
		gametypeService = new GameTypeService(type);

	}

	/**
	 * push the Announcer to opposite player
	 */
	public void pushGame() {
		getCurrentRound().setPushed(true);
		// wenn der andere spieler ein bot ist, soll er ansagen
		forceBotTrump();
	}

	/**
	 * method to get the last played cards
	 * @return a list of cards
	 */
	public List<Card> getLastCards() {
		return this.lastCards;
	}

	/**
	 * @return list of all teams
	 */
	public List<Team> getTeams() {

		return _game.getTeams();
	}

	/** 
	 * adds "Wies" points to the team
	 * @param wies
	 */
	public void addWies(Wies wies) {
		getCurrentRound().addWies(wies);
		String log = translateAndFormat("userannouceswies", new String[] { wies
				.getUser().getName() });
		log(log);
		System.out.println(log);
	}

	/**
	 * @param user
	 * @return true if user has won
	 */
	public boolean hasUserWon(User user) {
		if (getState() == GameState.Terminated) {
			return getLeadingTeam().getUsers().contains(user);
		}
		return false;
	}

	/**
	 * creates the twitter share message
	 * @param user
	 * @return twitter message
	 */
	public String getTwitterText(User user) {
		User partner = getTeamOf(user).getUser1().equals(user) ? getTeamOf(user).getUser2() : getTeamOf(user).getUser1();
		Team otherTeam = getTeamOf(user).equals(getTeams().get(0)) ? getTeams().get(1) : getTeams().get(0);
		String tweettext = "";
		if(GameState.Terminated.equals(getState())){
			if(hasUserWon(user)){
				tweettext = "tweet_userwinsagainst";
			}else{
			tweettext = "tweet_userlosesagainst";
			}
		}else{
		//is currently playing Jass with %s in team %s Score: %s:%s
			tweettext = "tweet_userplayingstadings";		
		}
		return "&text="+translateAndFormat(tweettext, new String[]{ partner.getName(), getTeamOf(user).getName(),otherTeam.getName(), getTeamOf(user).getPoints()+"",otherTeam.getPoints()+"" });
	}
}
