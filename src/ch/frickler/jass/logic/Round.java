package ch.frickler.jass.logic;


import java.util.ArrayList;
import java.util.List;

import ch.frickler.jass.definitions.ISpielart;
import ch.frickler.jass.definitions.ISpieler;


public class Round {

	public static enum RoundResult { QuitGame , Finshed };
	
	private List<Card> layingCards = new ArrayList<Card>();
	private ISpielart spielart;
	private ISpieler currentSpieler;
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
	public void SetSpielart(ISpielart spielart) {
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
	
}
