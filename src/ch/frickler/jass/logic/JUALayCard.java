package ch.frickler.jass.logic;

import ch.frickler.jass.logic.definitions.*;

public class JUALayCard implements IUserAction {

	private Card card;
	
	public JUALayCard(Card c){
		this.card = c;
	}
	
	public Card getCard(){
		return card;
	}
	
}
