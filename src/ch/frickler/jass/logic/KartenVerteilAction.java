package ch.frickler.jass.logic;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KartenVerteilAction extends SpielAction {

	@Override
	public void doAction(Spiel spiel) {

		List<Card> allCards = new ArrayList<Card>();

		for (Card.CardFamily family : Card.CardFamily.values()) {
			for (Card.CardValue value : Card.CardValue.values()) {
				allCards.add(new Card(value, family));
			}
		}
		Collections.shuffle(allCards);
		
		while(!allCards.isEmpty()){
			for(Spieler spieler : spiel.getAllSpieler()){
				spieler.addCard(allCards.remove(0));
			}
		}
	}

}
