package ch.frickler.jass.logic;

import ch.frickler.jass.logic.definitions.ISpielart;
import ch.frickler.jass.logic.definitions.ISpieler;
import ch.frickler.jass.logic.definitions.IUserAction;

public class Spieler extends ISpieler {

	public Spieler(String string) {
		super(string);
	}

	public Spieler() {
		super();
	}

	public ISpielart sayTrumpf(boolean canSchieben){		
		return new Obenabe();
	}
	
	public IUserAction forcePlay(Round round) throws Exception{
		
		// if a card is laying 
		if(round.getCards().size() > 0){
			//play one of the same family	
			for(int i = 0;i < cards.size();i++){
				
				if(cards.get(i).getCardFamily() == round.getCards().get(0).getCardFamily()){
					return new JUALayCard(this,cards.remove(i));
				}
			}
		}
		
		// else play the first card.
		return  new JUALayCard(this,cards.remove(0));
	}
	

	
}
