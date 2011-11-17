package ch.frickler.jass.logic;

import java.util.ArrayList;
import java.util.List;
import ch.frickler.jass.definitions.*;
public class Spiel {

	private int spielNr;
	private List<Team> teams = new ArrayList<Team>();
	private Runde currentRound;
	private int gewinnPunkte = 500;
	private int maxSpieler = 4;

	public Spiel() {
		currentRound = new Runde();
	}

	public List<Team> getTeams() {
		return teams;
	}

	public List<Spieler> getAllSpieler() {
		List<Spieler> alleSpieler = new ArrayList<Spieler>();
		for (Team t : teams) {
			alleSpieler.addAll(t.getSpieler());
		}
		return alleSpieler;
	}

	/*
	 * 
	 */
	public List<Spieler> getAllSpielerSorted(Spieler beginner) {
		List<Spieler> alleSpieler = getAllSpieler();
		
		if (alleSpieler.size() == 0)
			return alleSpieler;
		
		List<Spieler> tempSpieler = new ArrayList<Spieler>();
		
		int totalspieler = alleSpieler.size();
		int spielerproteam = totalspieler / teams.size();
		for (int i = 0; i < spielerproteam; i++) {
			for(Team t : teams){
				tempSpieler.add(t.getSpieler().get(i));
			}
		}

		int beginId = 0;
		if (beginner != null) {
			beginId = tempSpieler.indexOf(beginner);
			if(beginId < 0)
				beginId = 0;
		}
		List<Spieler> tempSpieler2 = new ArrayList<Spieler>();
		for(int i = beginId; i < beginId+totalspieler;i++){
			tempSpieler2.add(tempSpieler.get(i%totalspieler));
		}
		
		return tempSpieler2;
	}

	public void addSpieler(Spieler spieler) throws Exception {
		if (getAllSpieler().size() >= maxSpieler)
			throw new Exception("wft zu viele Spieler");

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
	 * Spieler zurück der den Stich gemacht hat.
	 */
	public Spieler placeStich(List<Card> cards) {

		int highestCard = 0;
		for (int i = 1; i < cards.size(); i++) {
			if (currentRound.getSpielart().isSecondCardHigher(
					cards.get(highestCard), cards.get(i))) {
				highestCard = i;
			}

		}
		List<Spieler> spielers = getAllSpielerSorted(this.getRound()
				.getAusspieler());
		Spieler spieler = spielers.get(highestCard % spielers.size());
		addCardsToTeam(spieler, cards);

		// der spieler der die höchste karte hatte darf als nächstes auspielen.
		currentRound.setAusspieler(spieler);

		return spieler;
	}

	private void addCardsToTeam(Spieler spieler, List<Card> cards) {

		Team t = getTeamOf(spieler);
		t.addCard(cards);
		System.out
				.println("Round " + cards.toString() + " goes to Team: "
						+ t.getName() + " highest Card of Player: "
						+ spieler.getName());
	}

	private Team getTeamOf(Spieler spieler) {
		for (Team t : teams) {
			if (t.getSpieler().contains(spieler)) {
				return t;
			}
		}
		return null;
	}

	public void SetRound(Runde round) {
		this.currentRound = round;

	}

	public Runde getRound() {
		return this.currentRound;
	}

	public Spielfeld getSpielfeld() {
		return new Spielfeld(currentRound.getSpielart());
	}
}
