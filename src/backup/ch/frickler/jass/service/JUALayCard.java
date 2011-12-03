package ch.frickler.jass.service;

import ch.frickler.jass.db.entity.Card;
import ch.frickler.jass.db.entity.User;
import ch.frickler.jass.logic.definitions.*;

public class JUALayCard extends BaseAction {

	public JUALayCard(User user) {
		super(user);
	}

	private Card card;
	
	public JUALayCard(User u,Card c){
		super(u);
		this.card = c;
	}
	

	public Card getCard(){
		return card;
	}

	@Override
	public boolean doAction(GameService game) {
		game.playCard(user,card);
		return true;
	}

	@Override
	public boolean isActionPossible(GameService game) {
		return game.canPlayCard(card, user);
	}
	
}
