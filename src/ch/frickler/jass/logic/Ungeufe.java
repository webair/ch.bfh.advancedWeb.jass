package ch.frickler.jass.logic;


import java.util.List;

import ch.frickler.jass.*;
import ch.frickler.jass.definitions.ISpielart;

public class Ungeufe extends ISpielart {

	@Override
	public int getPoints(List<Card> cards) {
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
	
	public String toString(){
		 return "Spielart: Ungeufe";
		}
}
	
