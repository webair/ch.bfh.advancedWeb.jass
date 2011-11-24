package ch.frickler.jass.logic;


import java.util.List;

import ch.frickler.jass.logic.definitions.ISpielart;
import ch.frickler.jass.logic.definitions.ISpieler;

public class Ungeufe extends ISpielart {

	@Override
	protected int getPointsOfSpielart(List<Card> cards) {
		int points = 0;
		for(Card c  : cards) points += getPoint(c);	
		return points;
	}
	
	@Override
	protected int getPoint(Card card) {
		switch(card.getCardValue()){
		case Sechs:
			return 11;
		case Sieben:
			return 0;
		case Acht:
			return 8;
		case Neun:
			return 0;
		case Zehn:
			return 10;
		case Bauer:
			return 2;
		case Dame:
			return 3;
		case Koenig:
			return 4;
		case Ass:
			return 0;
		}
		return 0;
	}

	@Override
	public boolean isSecondCardHigher(Card highestCard, Card card) {
		
		if(highestCard.getCardFamily() == card.getCardFamily()){
			if(highestCard.getOrderValue() > card.getOrderValue())
				return true;
		}
		return false;
	}

	@Override
	public boolean isPlayedCardVaild(ISpieler spl, Card layedCard, Round r) {
		
		if(r.getCards().size() == 0)
			return true;
		
		Card firstcard = r.getCards().get(0);
		
		if(firstcard.getCardFamily() == layedCard.getCardFamily())
			return true;
		
		
		if(spl.hasCardOfFamily(firstcard.getCardFamily()))
				return false;
		
		return true;
	}
	
	
	public String toString(){
		 return "Spielart: Ungeufe";
		}

	@Override
	public int getQualifier() {
		return 3; 
	}
}
	
