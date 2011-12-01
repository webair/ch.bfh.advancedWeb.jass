package ch.frickler.jass.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ch.frickler.jass.logic.Round.RoundResult;
import ch.frickler.jass.logic.Spiel.GameState;
import ch.frickler.jass.logic.definitions.*;

public class Spiel {

	public static enum GameState  {WaitForPlayers, Play, Ansage, AnsageGschobe, Terminated, RediForPlay, WaitForCards}
	private static boolean gschobe = false;
	private int spielNr;
	private List<Team> teams = new ArrayList<Team>();
	private Round currentRound;
	private int gewinnPunkte = 500;
	private int maxISpieler = 4;
	private int ansager;
	private GameState gameState = Spiel.GameState.WaitForPlayers;
	
	
	public Spiel() {

	}

	public List<Team> getTeams() {
		return teams;
	}

	public List<ISpieler> getAllSpieler() {
		List<ISpieler> alleISpieler = new ArrayList<ISpieler>();
		for (Team t : teams) {
			alleISpieler.addAll(t.getSpieler());
		}
		return alleISpieler;
	}

	/*
	 * 
	 */
	public List<ISpieler> getAllSpielerSorted(ISpieler beginner) {
		List<ISpieler> alleISpieler = getAllSpieler();

		if (alleISpieler.size() == 0)
			return alleISpieler;

		List<ISpieler> tempISpieler = new ArrayList<ISpieler>();

		int totalISpieler = alleISpieler.size();
		int ISpielerproteam = totalISpieler / teams.size();
		for (int i = 0; i < ISpielerproteam; i++) {
			for (Team t : teams) {
				tempISpieler.add(t.getSpieler().get(i));
			}
		}

		int beginId = 0;
		if (beginner != null) {
			beginId = tempISpieler.indexOf(beginner);
			if (beginId < 0)
				beginId = 0;
		}
		List<ISpieler> tempISpieler2 = new ArrayList<ISpieler>();
		for (int i = beginId; i < beginId + totalISpieler; i++) {
			tempISpieler2.add(tempISpieler.get(i % totalISpieler));
		}

		return tempISpieler2;
	}

	

	public void addSpieler(ISpieler spieler) throws Exception {
		if (getAllSpieler().size() >= maxISpieler)
			throw new Exception("wft! zu viele ISpieler");

		if (teams.size() < 2) {
			Team team = new Team(spieler);
			teams.add(team);
		} else {
			if (teams.get(0).getSpieler().size() < 2) {
				teams.get(0).addSpieler(spieler);
			} else {
				teams.get(1).addSpieler(spieler);
			}
		}
		if(getAllSpieler().size() == maxISpieler){
			initGame();
			setGameState(Spiel.GameState.WaitForCards);		
		}
	}

	

	/*
	 * Der Stich geht zum Team mit der hoechsten Karte die Methode gibt den
	 * ISpieler zurueck der den Stich gemacht hat.
	 */
	public ISpieler placeStich(List<Card> cards) {

		int highestCard = 0;
		for (int i = 1; i < cards.size(); i++) {
			if (currentRound.getSpielart().isSecondCardHigher(
					cards.get(highestCard), cards.get(i))) {
				highestCard = i;
			}

		}
		List<ISpieler> spieler = getAllSpielerSorted(this.getRound()
				.getAusspieler());
		ISpieler spl = spieler.get(highestCard % spieler.size());
		addCardsToTeam(spl, cards);

		// der ISpieler der die hoechste karte hatte darf als naechstes auspielen.
		currentRound.setAusspieler(spl);

		return spl;
	}

	private void addCardsToTeam(ISpieler spieler, List<Card> cards) {

		Team t = getTeamOf(spieler);
		t.addCard(cards);
		System.out
				.println("Round " + cards.toString() + " goes to Team: "
						+ t.getName() + " highest Card of Player: "
						+ spieler.getName());
	}

	private Team getTeamOf(ISpieler ISpieler) {
		for (Team t : teams) {
			if (t.getSpieler().contains(ISpieler)) {
				return t;
			}
		}
		return null;
	}

	public void SetRound(Round round) {	
		round.setSpielerWithStoeck(getSpielerWithStoeck(round.getSpielart()));
		this.currentRound = round;
		
	}

	private ISpieler getSpielerWithStoeck(ISpielart spielart) {
		// stöck nur möglich falls trumpf angesagt ist.
		if(!(spielart instanceof Trumpf))
			return null;
		
		Trumpf pf = (Trumpf)spielart;
		for(ISpieler s : getAllSpieler()){
			boolean hasTrumpfSchnegge = false;
			boolean hasTrumpfKing = false;
			for(Card c : s.getCards()){
				if(c.getCardFamily().equals(pf.getCardFamily())){
						
						if(c.getCardValue() == Card.CardValue.Dame){
					hasTrumpfSchnegge = true;
				}else if(c.getCardValue() == Card.CardValue.Koenig){
					hasTrumpfSchnegge = true;
				}
			}
		}
		if(hasTrumpfKing && hasTrumpfSchnegge)
			return s;
		}
		return null;
	}

	public Round getRound() {
		return this.currentRound;
	}

	public RoundResult playRound() throws Exception {

		kartenVerteilen();
		ISpieler spAnsager = getAnsager();

		ISpielart spielart = spAnsager.sayTrumpf(true);
		if (spielart == null) // todo nicht so schoen
		{
			int nextansager = (getTeamOf(spAnsager).getSpieler().indexOf(
					spAnsager) + 1)
					% getTeamOf(spAnsager).getSpieler().size();

			ISpieler spAnsagerGschobe = getTeamOf(spAnsager).getSpieler().get(
					nextansager);
			spielart = spAnsagerGschobe.sayTrumpf(false);
			if (spielart == null) {
				throw new Exception(
						"Spielart null, darf nicht moeglich sein. Nachdem geschoben wurde");
			}
		}
		this.SetRound(new Round(spielart));
		
		// wer trumpf sagt spielt aus, auch wenn geschoben.
		this.currentRound.setAusspieler(spAnsager);
		System.out.println("Begin round spielart is: "+currentRound.getSpielart().toString());
		try {
			Round r = this.getRound();

			for (int i = 0; i < 9; i++) {
				for (ISpieler spl : this.getAllSpielerSorted(this.getRound()
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
			doAbrechnungForRound(r);

		} catch (Exception ex) {
			System.out.print(ex.getMessage());

		}
		this.ansager = ++this.ansager % this.getAllSpieler().size();
		return RoundResult.Finshed;
	}

	private void doAbrechnungForRound(Round r) {
		//round finished place points
		for (Team t : this.getTeams()) {
			int pointsTeam = currentRound.getSpielart().getPoints(
					t.getCards());
			// team hat den letzten Stich gemacht
			if(getTeamOf(r.getCurrentSpieler()).equals(t)){
				pointsTeam += 5;
			}
			System.out.println("Punkte diese Runde fuer " + t.getName()
					+ ": " + pointsTeam);
			t.addPoints(pointsTeam*currentRound.getSpielart().getQualifier());
			t.clearCards();
		}

		for (Team t : this.getTeams()) {
			System.out.println("Punktestand " + t.getName() + ": "
					+ t.getPoints());
		}
	}

	void playCard(ISpieler spl, Card layedCard) {
		getRound().addCard(layedCard);
		spl.removeCard(layedCard);
		getRound().nextPlayer(getAllSpielerSorted(spl).get(0));
		if(getRound().allSpielerPlayed()){
			finishStich();
			if(getRound().getCurrentSpieler().getCards().size() == 0){
				doAbrechnungForRound(getRound());
			}
		}
	}

	private void finishStich() {
		Round r = getRound();
		ISpieler spAnsager;
		spAnsager = this.placeStich(r.getCards());
		this.getRound().setAusspieler(spAnsager);
		r.removeCards();
	}

	public void initGame(){
		initNewRound();
		this.currentRound.setAusspieler(getAllSpieler().get(0));
	}
	
	 public void initNewRound() {
		this.gschobe = false;
		setGameState(Spiel.GameState.WaitForCards);
		this.currentRound = new Round(null);
	 } 

	 public void kartenVerteilen(){
		 List<Card> allCards = new ArrayList<Card>();

			if(getAllSpieler().size() == 0){
				new RuntimeException("cannot card verteilen, no players found");
			}
		 
			for (Card.CardFamily family : Card.CardFamily.values()) {
				for (Card.CardValue value : Card.CardValue.values()) {
					allCards.add(new Card(value, family));
				}
			}
			Collections.shuffle(allCards);
			

			
			while(!allCards.isEmpty()){
				for(ISpieler spieler : getAllSpieler()){
					spieler.addCard(allCards.remove(0));
				}
			}
			setGameState(Spiel.GameState.Ansage);
	 }
	 
	ISpieler getAnsager() {
		int delta = isGschobe() ? 2 : 0;
		this.getAllSpielerSorted(null).get(ansager+delta);
		return null;
	}

	private boolean isGschobe() {
		return this.gschobe;
	}
	
	private void setGschobe() {
		this.gschobe = false;
	}

	private boolean isPlayedCardValid(ISpieler spl, Card layedCard, Round r) {

		return r.getSpielart().isPlayedCardVaild(spl, layedCard, r);
	}

	public int getWinPoints() {

		return gewinnPunkte;
	}

	public Team getLeadingTeam() {
		Team leading = null;
		for (Team t : teams) {
			if (leading == null || leading.getPoints() < t.getPoints()) {
				leading = t;
			}
		}
		return leading;
	}
	
	private Team getLoosingTeam() {
		Team loosing = null;
		for (Team t : teams) {
			if (loosing == null || loosing.getPoints() > t.getPoints()) {
				loosing = t;
			}
		}
		return loosing;
	}

	public void setWinPoints(int winPoints) {
		this.gewinnPunkte = winPoints;
		
	}

	public void terminate(ISpieler terminateUser) {
	
		Team team = getTeamOf(terminateUser);
		
		if(team == null){
			team = getLoosingTeam();
		}
		
		for(Team winnerTeam : getTeams()){
			if(!team.equals(winnerTeam)){
				writeStatistic(winnerTeam,team);
				break;
			}
		}
		setGameState(GameState.Terminated);
	}

	void setGameState(GameState newState) {
		this.gameState = newState;
		
	}

	private void writeStatistic(Team winnerTeam, Team team) {
		// TODO Auto-generated method stub
	}

	public GameState getState() {
		return this.gameState;
	}

	public boolean isPlayedCardValid(ISpieler user,Card card) {
		return isPlayedCardValid(user, card, getRound());
	}

	public boolean addStoeck(ISpieler user) {
		Team t = getTeamOf(user);
		t.addPoints(Trumpf.ValueOfStoeck*getRound().getSpielart().getQualifier());
		return false;
	}


	
	
}
