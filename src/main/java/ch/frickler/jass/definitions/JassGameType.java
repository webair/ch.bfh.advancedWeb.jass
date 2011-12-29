package ch.frickler.jass.definitions;

import java.util.List;

import ch.frickler.jass.db.entity.Card;
import ch.frickler.jass.db.entity.Round;
import ch.frickler.jass.db.entity.User;
import ch.frickler.jass.db.enums.CardFamily;


public abstract class JassGameType {

	public int getPoints(List<Card> cards){
		int points = getMatchPoints(cards);
		return points + getPointsOfSpielart(cards);
	}
	protected abstract int getPointsOfSpielart(List<Card> cards);
	protected abstract int getPoint(Card cards);
	public abstract boolean isSecondCardHigher(Card highestCard, Card card);
	public abstract boolean isPlayedCardVaild(User spl, Card layedCard, Round r);
	public abstract int getQualifier();
	public int getMatchPoints(List<Card> cards){
		if(cards.size() == Card.TOTALCARD){
			return 100;
		}
		return 0;
	}
	protected boolean isSameFamily(Card card, CardFamily family) {
		return card.getFamily().equals(family);
	}
	
	protected boolean hasCardOfFamily(List<Card> cards,
			CardFamily family) {
			
			if(cards == null || cards.size() == 0)
				return false;		
			
			for(Card c : cards){
				if(isSameFamily(c,family))
					return true;
			}
			return false;
		}
}
