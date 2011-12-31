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
import ch.frickler.jass.db.enums.CardFamily;
import ch.frickler.jass.db.enums.CardValue;
import ch.frickler.jass.db.enums.GameKind;
import ch.frickler.jass.db.enums.GameState;
import ch.frickler.jass.gametype.Trump;
import ch.frickler.jass.helper.Translator;
import ch.frickler.jass.definitions.JassAction;

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

	// logs the 10 last messages
	private void log(String msg) {
		log.add(0, msg);
		while (log.size() > 10)
			log.remove(log.size() - 1);
	}

	public List<String> getLog() {
		return log;
	}

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

	public Game loadGame(Long gameId) {
		_game = loadObject(Game.class, gameId);
		return _game;
	}

	/***
	 * 
	 * @return all spieler of the game unsorted
	 */
	public List<User> getAllSpieler() {
		List<User> alleISpieler = new ArrayList<User>();
		for (Team t : _game.getTeams()) {
			alleISpieler.addAll(t.getUsers());
		}
		return alleISpieler;
	}

	/***
	 * 
	 * @return all spieler of the game sorted by the ausspieler
	 */
	public List<User> getAllSpielerSorted(User beginner) {
		List<User> alleISpieler = getAllSpieler();

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

	public void addSpieler(User spieler) throws RuntimeException {
		if (getAllSpieler().size() >= Game.MAXUSER)
			throw new RuntimeException("wft! zu viele ISpieler");

		if (_game.getTeamAmount() < 2) {
			Team team = new Team(spieler);
			team.setPoints(0);
			team.setName("Team of " + spieler.getName());
			_game.addTeam(team);
		} else {
			if (getTeam(0).getUsers().size() < 2) {
				getTeam(0).addUser(spieler);
			} else {
				getTeam(1).addUser(spieler);
			}
		}
		mergeObject(_game); // store the created teams
		if (getAllSpieler().size() == Game.MAXUSER) {
			initGame();
			new ActionDealOutCards().doAction(this);
		}
	}

	private Team getTeam(int i) {
		return _game.getTeam(i);
	}

	/*
	 * Der Stich geht zum Team mit der hoechsten Karte die Methode gibt den
	 * ISpieler zurueck der den Stich gemacht hat.
	 */
	private User placeStich(List<Card> cards) {

		int highestCard = 0;
		for (int i = 1; i < cards.size(); i++) {
			if (isSecondCardHigher(cards.get(highestCard), cards.get(i))) {
				highestCard = i;
			}

		}
		List<User> spieler = getAllSpielerSorted(getCurrentRound()
				.getBeginner());
		User spl = spieler.get(highestCard % spieler.size());
		addCardsToTeam(spl, cards);

		// der ISpieler der die hoechste karte hatte darf als naechstes
		// auspielen.
		getCurrentRound().setBeginner(spl);

		return spl;
	}

	private boolean isSecondCardHigher(Card card, Card card2) {
		return getGameTypeService().isSecondCardHigher(card, card2);
	}

	public Round getCurrentRound() {
		return _game.getCurrentRound();
	}

	private void addCardsToTeam(User spieler, List<Card> cards) {

		Team t = getTeamOf(spieler);
		t.addCard(cards);
		System.out
				.println("Round " + cards.toString() + " goes to Team: "
						+ t.getName() + " highest Card of Player: "
						+ spieler.getName());
	}

	private Team getTeamOf(User user) {
		for (Team t : _game.getTeams()) {
			if (t.isUserInTeam(user)) {
				return t;
			}
		}
		return null;
	}

	public void SetRound(Round round) {
		round.setPlayerWithStoeck(getSpielerWithStoeck());
		_game.setCurrentRound(round);

	}

	private User getSpielerWithStoeck() {

		if (!(getGameTypeService().isTrumpf()))
			return null;

		CardFamily trumpfCardFamily = getGameTypeService()
				.getTrumpfCardFamily();
		for (User s : getAllSpieler()) {
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

	private void finishRound() {
		doAbrechnung();

		// check wether the game has ended
		int winPoints = _game.getWinPoints();
		for (Team t : getTeams()) {
			if (t.getPoints() >= winPoints) {
				finishGame();
				return;
			}
		}

		int caller = _game.getCallerId();
		_game.setCallerId(++caller % this.getAllSpieler().size());
		setGameState(GameState.WaitForCards);
		// neue karten verteilen
		new ActionDealOutCards().doAction(this);
		// when we start a new round, the beginner is the ansager
		// and not the one that placed the last stich!
		getCurrentRound().setBeginner(getAnsager());
		getCurrentRound().setCurrentPlayer(getAnsager());
		forceBotTrump();
	}

	private void finishGame() {
		setGameState(GameState.Terminated);
		for (User u : getAllSpieler()) {
			u.setPlaying(false);
		}
	}

	private void forceBotTrump() {
		User u = getAnsager();
		if (u.isABot()) {
			int num = GameKind.values().length;
			GameKind k = GameKind.values()[new Random().nextInt(num)];
			new ActionAnnounce(u, k).doAction(this);
			System.out.println("New Trump is (by bot) " + k);
		}
		// now that we have a trump, let the first bot plays (if it's his turn)
		forceBotPlay();
	}

	private void doAbrechnung() {
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

	public String translateAndFormat(String key, String[] args) {
		String res = Translator.getString(FacesContext.getCurrentInstance(),
				key);
		if (res.length() == 0)
			return "Translation Key " + key + " not found";

		return String.format(res, args);
	}

	public void playCard(User spl, Card layedCard) {
		String card = translateCard(layedCard);
		String log = translateAndFormat("playerplayed",
				new String[] { spl.getName(), card });
		System.out.println(log);
		log(log);
		Round r = getCurrentRound();
		r.addCard(layedCard);
		spl.removeCard(layedCard);
		r.setCurrentPlayer(getAllSpielerSorted(spl).get(1));
		if (allSpielerPlayed()) {
			finishStich();
			if (r.getCurrentPlayer().getCards().size() == 0) {
				finishRound();
			}
		}
		forceBotPlay();
	}

	private String translateCard(Card layedCard) {
		String resFamily = Translator.getString(FacesContext
				.getCurrentInstance(), layedCard.getFamily().toString());
		String resValue = Translator.getString(FacesContext
				.getCurrentInstance(), layedCard.getValue().toString());
		return resFamily + " " + resValue;
	}

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

	private boolean allSpielerPlayed() {
		return getCurrentRound().getCards().size() == 4;
	}

	private void finishStich() {
		Round r = getCurrentRound();
		User spAnsager;
		spAnsager = this.placeStich(r.getCards());
		log("Strich geht an " + spAnsager.getName());
		r.setBeginner(spAnsager);
		r.setCurrentPlayer(spAnsager);
		lastCards = new ArrayList<Card>(r.getCards());
		r.removeCards();
	}

	private void initGame() {
		initNewRound();
		Round r = getCurrentRound();
		r.setBeginner(getAllSpieler().get(0));
		r.setCurrentPlayer(getAllSpieler().get(0));

		for (User u : getAllSpieler()) {
			u.setPlaying(true);
		}
	}

	private void initNewRound() {
		setGameState(GameState.WaitForCards);
		_game.setCurrentRound(new Round());
	}

	public void kartenVerteilen() {
		List<Card> allCards = new ArrayList<Card>();

		if (getAllSpieler().size() == 0) {
			new RuntimeException("cannot card verteilen, no players found");
		}

		for (CardFamily family : CardFamily.values()) {
			for (CardValue value : CardValue.values()) {
				allCards.add(new Card(family, value));
			}
		}
		Collections.shuffle(allCards);

		while (!allCards.isEmpty()) {
			for (User spieler : getAllSpieler()) {
				spieler.addCard(allCards.remove(0));
			}
		}

		// sorting cards if user, suffle if it is a roboter
		for (User u : getAllSpieler()) {
			if (u.isABot()) {
				Collections.shuffle(u.getCards());
			} else {
				Collections.sort(u.getCards());
			}
		}
		setGameState(GameState.Ansage);
	}

	public User getAnsager() {
		int delta = getCurrentRound().isPushed() ? 2 : 0;
		return this.getAllSpielerSorted(null).get(
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

	public void terminate(User terminateUser) {
		Team team = getTeamOf(terminateUser);
		if (team == null) {
			team = getLoosingTeam();
		}
		for (Team winnerTeam : _game.getTeams()) {
			if (!team.equals(winnerTeam)) {
				writeStatistic(winnerTeam, team);
				break;
			}
		}
		setGameState(GameState.Terminated);
	}

	protected void setGameState(GameState state) {
		_game.setGameState(state);
	}

	private void writeStatistic(Team winnerTeam, Team team) {
		// TODO Auto-generated method stub
	}

	public GameState getState() {
		return _game.getGameState();
	}

	public boolean addStoeck(User user) {
		Team t = getTeamOf(user);
		t.addPoints(Trump.VALUEOFSTOECK * getGameTypeService().getQualifier());
		return false;
	}

	private GameTypeService getGameTypeService() {
		return gametypeService;
	}

	public boolean doAction(JassAction action) {

		if (action.isActionPossible(this)) {
			return action.doAction(this);
		}
		return false;
	}

	public boolean isValidAnsager(User user) {
		if ((_game.getGameState() == GameState.Ansage || _game.getGameState() == GameState.AnsageGschobe)
				&& getAnsager().equals(user)) {
			return true;
		}
		return false;
	}

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

		return isPlayedCardVaild(user, layedCard, r);
	}

	public boolean isPlayedCardVaild(User user, Card layedCard, Round r) {
		return getGameTypeService().isPlayedCardVaild(user, layedCard, r);
	}

	public boolean hasUserStoeck(User user) {
		if (user == null || getCurrentRound().getPlayerWithStoeck() == null)
			return false;

		return getCurrentRound().getPlayerWithStoeck().equals(user);
	}

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

	public void setGameType(GameKind type) {
		getCurrentRound().setGameKind(type);
		gametypeService = new GameTypeService(type);

	}

	/**
	 * schiebt das game zu einem anderen spieler
	 */
	public void pushGame() {
		getCurrentRound().setPushed(true);
		// wenn der andere spieler ein bot ist, soll er ansagen
		forceBotTrump();
	}

	public List<Card> getLastCards() {
		return this.lastCards;
	}

	public List<Team> getTeams() {

		return _game.getTeams();
	}

}
