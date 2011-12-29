package ch.frickler.jass.gametype;


import java.util.List;

import ch.frickler.jass.db.entity.Card;
import ch.frickler.jass.db.entity.Round;
import ch.frickler.jass.db.entity.User;
import ch.frickler.jass.definitions.JassGameType;


public class TopDown extends JassGameType {

	@Override
	protected int getPointsOfSpielart(List<Card> cards) {
		int points = 0;
		for(Card c  : cards) points += getPoint(c);	
		return points;
	}
	
	@Override
	protected int getPoint(Card card) {
		switch(card.getValue()){
		case SECHS:
			return 0;
		case SIEBEN:
			return 0;
		case ACHT:
			return 8;
		case NEUN:
			return 0;
		case ZEHN:
			return 10;
		case BAUER:
			return 2;
		case DAME:
			return 3;
		case KOENIG:
			return 4;
		case ASS:
			return 11;
		}
		return 0;
	}

	@Override
	public boolean isSecondCardHigher(Card highestCard, Card card) {
		
		if(isSameFamily(highestCard,card.getFamily())){
			if(highestCard.getOrderValue() < card.getOrderValue())
				return true;
		}
		return false;
	}
	
	public String toString(){
		 return "Spielart: Obename";
		}

	@Override
	public boolean isPlayedCardVaild(User spl, Card layedCard, Round r) {
		
		if(r.getCards().size() == 0)
			return true;
		
		Card firstcard = r.getCards().get(0);
		
		if(isSameFamily(firstcard,layedCard.getFamily()))
			return true;
		
		
		if(hasCardOfFamily(spl.getCards(),firstcard.getFamily()))
				return false;
		
		return true;
	}
	


	@Override
	public int getQualifier() {
		return 3; 
	}

}
