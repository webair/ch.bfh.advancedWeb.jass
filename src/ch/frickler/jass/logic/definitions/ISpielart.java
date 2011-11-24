package ch.frickler.jass.logic.definitions;

import java.util.List;

import ch.frickler.jass.logic.*;

public abstract class ISpielart {

	public int getPoints(List<Card> cards){
		int points = getMatchPoints(cards);
		return points + getPointsOfSpielart(cards);
	}
	protected abstract int getPointsOfSpielart(List<Card> cards);
	protected abstract int getPoint(Card cards);
	public abstract boolean isSecondCardHigher(Card highestCard, Card card);
	public abstract boolean isPlayedCardVaild(ISpieler spl, Card layedCard, Round r);
	public abstract int getQualifier();
	public int getMatchPoints(List<Card> cards){
		if(cards.size() == Card.getTotalCards()){
			return 100;
		}
		return 0;
	}
	
		
}
