package ch.frickler.jass.logic;

import java.util.ArrayList;
import java.util.List;
import ch.frickler.jass.definitions.*;
import ch.frickler.jass.logic.Round.RoundResult;

public class Spiel {

	private int spielNr;
	private List<Team> teams = new ArrayList<Team>();
	private Round currentRound;
	private int gewinnPunkte = 500;
	private int maxISpieler = 4;
	private int ansager;

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
			throw new Exception("wft zu viele ISpieler");

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

	}

	/*
	 * Der Stich geht zum Team mit der höchsten Karte die Methode gibt den
	 * ISpieler zurück der den Stich gemacht hat.
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

		// der ISpieler der die höchste karte hatte darf als nächstes auspielen.
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
		this.currentRound = round;

	}

	public Round getRound() {
		return this.currentRound;
	}

	public Spielfeld getSpielfeld_Todel() {
		return null; // new Spielfeld(currentRound.getSpielart());
	}

	public RoundResult playRound() {
		
		this.getAllSpielerSorted(null).get(ansager);
		
		KartenVerteilAction kva = new KartenVerteilAction();
		kva.doAction(this);
		for(ISpieler spl : this.getAllSpieler()){
			System.out.println("Karten ISpieler "+spl.getName()+" : "+spl.getCards());
		}
		
		try {
			Round r = this.getRound();

			for (int i = 0; i < 9; i++) {
				for (ISpieler spl : this.getAllSpielerSorted(this.getRound()
						.getAusspieler())) {
					boolean nextISpielersTurn = true;

					while (!nextISpielersTurn) {
						IUserAction uc = spl.forcePlay(r);
						// user played a card
						if (uc instanceof JUALayCard) {
							Card layedCard = ((JUALayCard) uc).getCard();
							if (isPlayedCardValid(spl, layedCard, r)) {
								nextISpielersTurn = true;
								r.addCard(layedCard);
							} else {
								// not valid card at it to the users hand again
								spl.addCard(layedCard);
							}
						}else if (uc instanceof JUAQuit) {
							// todo quit action 
							return RoundResult.QuitGame;
							
						}else if (uc instanceof JUASchieben) {
							// todo schieben action 
							
						}else if (uc instanceof JUAStoeck) {
							// todo stoeck action 
							
						}else if (uc instanceof JUAWies) {
							// todo wies action 
							
						}else{
							throw new Exception("not handled user action "+uc.getClass());
						}
					}

				}
			}
			ISpieler stichMaker = this.placeStich(r.getCards());
			this.getRound().setAusspieler(stichMaker);
			r.removeCards();
			int pointsTeam1 = currentRound.getSpielart().getPoints(
					this.getTeams().get(0).getCards());
			int pointsTeam2 = currentRound.getSpielart().getPoints(
					this.getTeams().get(1).getCards());
			System.out.println("Punkte Team 1 " + pointsTeam1);
			System.out.println("Punkte Team 2 " + pointsTeam2);
			System.out.println("Total " + (pointsTeam2 + pointsTeam1));
		} catch (Exception ex) {
			System.out.print(ex.getMessage());

		}
		this.ansager = ++this.ansager % this.getAllSpieler().size();
		return RoundResult.Finshed;
	}

	private boolean isPlayedCardValid(ISpieler spl, Card layedCard, Round r) {
		
		
		return false;
	}

	public int getWinPoints() {
		
		return gewinnPunkte;
	}

	public Team getLeadingTeam() {
		// TODO Auto-generated method stub
		Team leading = null;
		for(Team t : teams){
			if(leading == null || leading.getPoints() < t.getPoints()){
				leading = t;
			}
		}
		return leading;
	}
}
