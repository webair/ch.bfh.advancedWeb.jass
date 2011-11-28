package ch.frickler.jass.logic;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.frickler.jass.db.entity.User;
import ch.frickler.jass.logic.definitions.ISpieler;
import ch.frickler.jass.logic.definitions.IUserAction;


public class KartenVerteilAction extends IUserAction {

	public KartenVerteilAction(ISpieler user) {
		super(user);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean doAction(Spiel spiel) {

		List<Card> allCards = new ArrayList<Card>();

		for (Card.CardFamily family : Card.CardFamily.values()) {
			for (Card.CardValue value : Card.CardValue.values()) {
				allCards.add(new Card(value, family));
			}
		}
		Collections.shuffle(allCards);
		
		while(!allCards.isEmpty()){
			for(ISpieler spieler : spiel.getAllSpieler()){
				spieler.addCard(allCards.remove(0));
			}
		}
		return true;	
	}

	@Override
	public boolean isActionPossible(Spiel game) {
		// TODO Auto-generated method stub
		return false;
	}
	

}
