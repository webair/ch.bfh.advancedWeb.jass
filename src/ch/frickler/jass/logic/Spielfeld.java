package ch.frickler.jass.logic;


import java.util.ArrayList;
import java.util.List;

import ch.frickler.jass.definitions.ISpielart;



public class Spielfeld {

	private List<Card> layingCards = new ArrayList<Card>();
	private ISpielart spielart;
	
	public Spielfeld(ISpielart spielart) {
		this.spielart = spielart;
	}

	public List<Card> getCards(){
		return layingCards;
	}

	public void addCard(Card c) {
		layingCards.add(c);
	}

	public void removeCards() {
		layingCards.clear();
	}
	
}
