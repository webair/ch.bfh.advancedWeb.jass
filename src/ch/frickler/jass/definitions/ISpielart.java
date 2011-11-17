package ch.frickler.jass.definitions;

import java.util.List;

import ch.frickler.jass.logic.*;

public abstract class ISpielart {

	public abstract int getPoints(List<Card> cards);
	protected abstract int getPoint(Card cards);
	private String name;
	public abstract boolean isSecondCardHigher(Card highestCard, Card card);
	
		
}
