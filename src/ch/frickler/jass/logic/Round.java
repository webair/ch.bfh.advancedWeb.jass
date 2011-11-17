package ch.frickler.jass.logic;


import java.util.ArrayList;
import java.util.List;

import ch.frickler.jass.definitions.ISpielart;


public class Round {

	private List<Card> layingCards = new ArrayList<Card>();
	private ISpielart spielart;
	private Spieler currentSpieler;
	private Spieler ausSpieler = null;
	
	public Round(ISpielart sa){
		this.spielart = sa;
	}
	
	public void setAusspieler(Spieler ausSpieler) {
		this.ausSpieler = ausSpieler;
	}
	public Spieler getAusspieler() {
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
