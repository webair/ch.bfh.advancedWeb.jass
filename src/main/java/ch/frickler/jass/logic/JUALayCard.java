package ch.frickler.jass.logic;

import ch.frickler.jass.db.entity.User;
import ch.frickler.jass.logic.definitions.*;

public class JUALayCard extends IUserAction {

	public JUALayCard(ISpieler user) {
		super(user);
		// TODO Auto-generated constructor stub
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isActionPossible(Spiel game) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
