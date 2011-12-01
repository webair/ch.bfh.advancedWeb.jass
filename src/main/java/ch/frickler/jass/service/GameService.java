package ch.frickler.jass.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;
import ch.frickler.jass.db.entity.Card;
import ch.frickler.jass.db.entity.GameType;
import ch.frickler.jass.db.entity.Round;
import ch.frickler.jass.db.entity.Team;
import ch.frickler.jass.db.entity.Game;
import ch.frickler.jass.db.entity.Game.GameState;
import ch.frickler.jass.db.entity.User;
import ch.frickler.jass.db.enums.CardFamily;
import ch.frickler.jass.db.enums.CardValue;


import ch.frickler.jass.logic.definitions.BaseAction;
import ch.frickler.jass.logic.definitions.ISpielart;


public class GameService extends PersistanceService {
	
	private Game _game;
	private Round round;
	private GameTypeService gametypeService;
	
	public Game createGame(String name, Integer winPoints) {
		Game g = new Game();
		g.setWinPoints(winPoints);
		g.setName(name);
		g.setGameState(Game.GameState.WaitForPlayers);
		g = mergeObject(g);
		return g;
	}

	public Game loadGame(Long userId) {
		return loadObject(Game.class, userId);
	}
	
	/***
	 * 
	 * @return all spieler of the game unsorted
	 */
	private List<User> getAllSpieler() {
		List<User> alleISpieler = new ArrayList<User>();
		alleISpieler.addAll(_game.getTeam(0).getUsers());
		alleISpieler.addAll(_game.getTeam(1).getUsers());
		return alleISpieler;
	}

	/***
	 * 
	 * @return all spieler of the game sorted by the ausspieler
	 */
	private List<User> getAllSpielerSorted(User beginner) {
		List<User> alleISpieler = getAllSpieler();

		if (alleISpieler.size() == 0)
			return alleISpieler;

		List<User> tempISpieler = new ArrayList<User>();

		int totalISpieler = alleISpieler.size();
		int ISpielerproteam = totalISpieler / _game.TEAMAMOUNT;
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
			_game.addTeam(team);
		} else {
			if (getTeam(0).getUsers().size() < 2) {
				getTeam(0).addUser(spieler);
			} else {
				getTeam(1).addUser(spieler);
			}
		}
		if(getAllSpieler().size() == _game.MAXUSER){
			initGame();
			setGameState(Game.GameState.WaitForCards);		
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

		// der ISpieler der die hoechste karte hatte darf als naechstes auspielen.
		getCurrentRound().setBeginner(spl);

		return spl;
	}

	private boolean isSecondCardHigher(Card card, Card card2) {
		return getGameTypeService().isSecondCardHigher(card,card2);
	}

	private GameType getSpielart() {
		return getCurrentRound().getGameType();
	}

	protected Round getCurrentRound() {
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
		
		
		
		if(!(getGameTypeService().isTrumpf()))
			return null;
		
		CardFamily trumpfCardFamily = getGameTypeService().getTrumpfCardFamily();
		for(User s : getAllSpieler()){
			boolean hasTrumpfSchnegge = false;
			boolean hasTrumpfKing = false;
			for(Card c : s.getCards()){
				if(c.getFamily().equals(trumpfCardFamily)){
						
						if(c.getValue() == CardValue.DAME){
					hasTrumpfSchnegge = true;
				}else if(c.getValue() == CardValue.KOENIG){
					hasTrumpfSchnegge = true;
				}
			}
		}
		if(hasTrumpfKing && hasTrumpfSchnegge)
			return s;
		}
		return null;
	}
/*
	private RoundResult playRound() throws Exception {

		kartenVerteilen();
		User spAnsager = getAnsager();

		ISpielart spielart = spAnsager.sayTrumpf(true);
		if (spielart == null) // todo nicht so schoen
		{
			int nextansager = (getTeamOf(spAnsager).getSpieler().indexOf(
					spAnsager) + 1)
					% getTeamOf(spAnsager).getSpieler().size();

			User spAnsagerGschobe = getTeamOf(spAnsager).getSpieler().get(
					nextansager);
			spielart = spAnsagerGschobe.sayTrumpf(false);
			if (spielart == null) {
				throw new Exception(
						"Spielart null, darf nicht moeglich sein. Nachdem geschoben wurde");
			}
		}
		this.SetRound(new Round(spielart));
		
		// wer trumpf sagt spielt aus, auch wenn geschoben.
		Round r = getCurrentRound();
		
		r.setAusspieler(spAnsager);
		System.out.println("Begin round spielart is: "+r.getSpielart().toString());
		try {
		
			for (int i = 0; i < 9; i++) {
				for (User spl : this.getAllSpielerSorted(r
						.getAusspieler())) {
					boolean nextISpielersTurn = false;

					while (!nextISpielersTurn) {
						IUserAction uc = spl.forcePlay(r);
						// user played a card
						if (uc instanceof JUALayCard) {
							Card layedCard = ((JUALayCard) uc).getCard();
							if (isPlayedCardValid(spl, layedCard, r)) {
								nextISpielersTurn = true;
								playCard(spl, layedCard);
							} else {
								// not valid card at it to the users hand again
								//spl.addCard(layedCard);
							}
						} else if (uc instanceof JUAQuit) {
							terminate(spl);
							return RoundResult.QuitGame;

						} else if (uc instanceof JUAAnsagen) {
							// todo schieben action

						} else if (uc instanceof JUAStoeck) {
							// todo stoeck action

						} else if (uc instanceof JUAWies) {
							// todo wies action

						} else {
							throw new Exception("not handled user action "
									+ uc.getClass());
						}
					}

				}
				finishStich();
			}
			finishRound();

		} catch (Exception ex) {
			System.out.print(ex.getMessage());

		}
		return RoundResult.Finshed;

	}
*/
	private void finishRound() {
		doAbrechnung();
		int caller = _game.getCallerId();
		_game.setCallerId(++caller % this.getAllSpieler().size());
		setGameState(GameState.RediForPlay);
	}
	
	private void doAbrechnung() {
		Round r = getCurrentRound();
		//round finished place points
		for (Team t : _game.getTeams()) {
			int pointsTeam = getGameTypeService().countPoints(t.getWonCards());
			// team hat den letzten Stich gemacht
			if(getTeamOf(r.getCurrentPlayer()).equals(t)){
				pointsTeam += 5;
			}
			System.out.println("Punkte diese Runde fuer " + t.getName()
					+ ": " + pointsTeam);
			t.addPoints(pointsTeam*getGameTypeService().getQualifier());
			t.clearCards();
		}

		for (Team t : _game.getTeams()) {
			System.out.println("Punktestand " + t.getName() + ": "
					+ t.getPoints());
		}
	}

	protected void playCard(User spl, Card layedCard) {
		System.out.println("User "+spl.getName()+" layed card"+layedCard.toString());
		Round r = getCurrentRound();
		r.addCard(layedCard);
		spl.removeCard(layedCard);
		r.setCurrentPlayer(getAllSpielerSorted(spl).get(0));
		if(allSpielerPlayed()){
			finishStich();
			if(r.getCurrentPlayer().getCards().size() == 0){
				finishRound();
			}
		}
	}

	private boolean allSpielerPlayed() {
		// TODO Auto-generated method stub
		return round.getCards().size() == 4;
	}

	private void finishStich() {
		Round r = getCurrentRound();
		User spAnsager;
		spAnsager = this.placeStich(r.getCards());
		r.setBeginner(spAnsager);
		r.removeCards();
	}

	private void initGame(){
		initNewRound();
		Round r = getCurrentRound();
		r.setBeginner(getAllSpieler().get(0));
		r.setCurrentPlayer(getAllSpieler().get(0));
	}
	
	private void initNewRound() {
		setGameState(Game.GameState.WaitForCards);
		_game.setCurrentRound(null);
	 } 

	 protected void kartenVerteilen(){
		 List<Card> allCards = new ArrayList<Card>();

			if(getAllSpieler().size() == 0){
				new RuntimeException("cannot card verteilen, no players found");
			}
		 
			for (CardFamily family : CardFamily.values()) {
				for (CardValue value : CardValue.values()) {
					allCards.add(new Card(family, value));
				}
			}
			Collections.shuffle(allCards);
			
		
			while(!allCards.isEmpty()){
				for(User spieler : getAllSpieler()){
					spieler.addCard(allCards.remove(0));
				}
			}
			setGameState(Game.GameState.Ansage);
	 }
	 
	 private User getAnsager() {
		int delta = getCurrentRound().isPushed() ? 2 : 0;
		this.getAllSpielerSorted(null).get(_game.getCallerId()+delta);
		return null;
	}


	private boolean isPlayedCardValid(User spl, Card layedCard, Round r) {

		return getGameTypeService().isPlayedCardVaild(spl, layedCard, r);
	}

	private int getWinPoints() {

		return _game.getWinPoints();
	}

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


	protected void terminate(User terminateUser) {
		Team team = getTeamOf(terminateUser);
		if(team == null){
			team = getLoosingTeam();
		}
		for(Team winnerTeam : _game.getTeams()){
			if(!team.equals(winnerTeam)){
				writeStatistic(winnerTeam,team);
				break;
			}
		}
		setGameState(GameState.Terminated);
	}

	protected void setGameState(ch.frickler.jass.db.entity.Game.GameState play) {
		//_game.setGameState(newState);
		//todo use correct gamestate
	}

	private void writeStatistic(Team winnerTeam, Team team) {
		// TODO Auto-generated method stub
	}

	public GameState getState() {
		return _game.getGameState();
	}

	protected boolean addStoeck(User user) {
		Team t = getTeamOf(user);
		t.addPoints(Trumpf.ValueOfStoeck*getGameTypeService().getQualifier());
		return false;
	}
	
	private GameTypeService getGameTypeService() {
		return gametypeService;
	}

	public boolean doAction(BaseAction action){
		
		if(action.isActionPossible(this)){
			return action.doAction(this);
		}
		return false;
	}

	protected boolean isValidAnsager(User user) {
		if (_game.getGameState() == GameState.Ansage
				&& _game.getCurrentRound().getBeginner().equals(user)) {
			return true;
		} else if (_game.getGameState() == GameState.AnsageGschobe
				&& getAnsager().equals(user)) {
			return true;
		}
		return false;
	}

	public boolean canPlayCard(Card layedCard,User user) {
		Round r = getCurrentRound();
		if(layedCard == null)
			return false;
		
		if(!r.getCurrentPlayer().equals(user)){
			return false;
		}
		
		return isPlayedCardVaild(user, layedCard,r);
	}
	
	public boolean isPlayedCardVaild(User user, Card layedCard, Round r) {
		//todo logic is in spielart
		return false;
	}

	public boolean hasUserStoeck(User user) {
		if(user == null || getCurrentRound().getPlayerWithStoeck() == null)
			return false;
		
		return getCurrentRound().getPlayerWithStoeck().equals(user);
	}

	public void setGameType(GameType type) {
		getCurrentRound().setGameType(type);
	}
	


}
