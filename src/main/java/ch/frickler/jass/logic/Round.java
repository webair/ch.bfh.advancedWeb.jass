package ch.frickler.jass.logic;


import java.util.ArrayList;
import java.util.List;

import ch.frickler.jass.logic.definitions.ISpielart;
import ch.frickler.jass.logic.definitions.ISpieler;


public class Round {

	public static enum RoundResult { QuitGame , Finshed };
	
	private List<Card> layingCards = new ArrayList<Card>();
	private ISpielart spielart;
	private ISpieler currentSpieler;
	private ISpieler spielerWithStoeck;
	private ISpieler ausSpieler = null;
	
	public Round(ISpielart sa){
		this.spielart = sa;
	}
	
	public void setAusspieler(ISpieler spl) {
		this.ausSpieler = spl;
	}
	public ISpieler getAusspieler() {
		return ausSpieler;
	}
	public void setSpielart(ISpielart spielart) {
		System.out.println("Spielart selected: "+spielart.toString());
		this.spielart = spielart;		
	}
	
	public ISpielart getSpielart(){
		return this.spielart;
	}

	public List<Card> getCards() {
		return this.layingCards;
	}
	
	public void removeCards() {
		this.layingCards  = new ArrayList<Card>();
	} 
	
	public void addCard(Card c) {
		this.layingCards.add(c);
	}

	public ISpieler getCurrentSpieler() {
		return currentSpieler;
	}
	
	public boolean allSpielerPlayed(){
		// todo generic for more than 4 players
		return this.getCards().size() == 4;
	}

	public void setSpielerWithStoeck(ISpieler spielerWithStoeck) {
		this.spielerWithStoeck = spielerWithStoeck;
	}

	public boolean hasUserStoeck(ISpieler user) {
		return user.equals(spielerWithStoeck);
	}
	
}
