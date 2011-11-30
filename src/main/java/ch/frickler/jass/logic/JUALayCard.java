package ch.frickler.jass.logic;

import ch.frickler.jass.db.entity.User;
import ch.frickler.jass.logic.definitions.*;

public class JUALayCard extends IUserAction {

	public JUALayCard(ISpieler user) {
		super(user);
	}

	private Card card;
	
	public JUALayCard(ISpieler u,Card c){
		super(u);
		this.card = c;
	}
	
	
	
	public Card getCard(){
		return card;
	}

	@Override
	public boolean doAction(Spiel spiel) {
		spiel.playCard(user,card);
		return true;
	}

	@Override
	public boolean isActionPossible(Spiel game) {
		if(card == null)
			return false;
		
		if(!game.getRound().getCurrentSpieler().equals(user)){
			return false;
		}
		
		
		return game.isPlayedCardValid(user,card);
	}
	
}
