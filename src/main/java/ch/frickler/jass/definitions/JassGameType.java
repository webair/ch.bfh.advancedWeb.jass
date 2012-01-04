package ch.frickler.jass.definitions;

import java.util.List;

import ch.frickler.jass.db.entity.Card;
import ch.frickler.jass.db.entity.Round;
import ch.frickler.jass.db.entity.User;
import ch.frickler.jass.db.enums.CardFamily;

/**
 * this abstract class defines witch methods a game type must implement.
 * 
 * @author kaeserst
 * 
 */
public abstract class JassGameType {

	private static int MATCHPOINTS = 100;
	protected static int BOTTOMUPQUALIFIER = 3;
	protected static int TOPDOWNQUALIFIER = 3;
	protected static int TRUMPBLACKQUALIFIER = 2;
	protected static int TRUMPREDQUALIFIER = 1;

	/**
	 * Computes the points of the cards, (not in every gametype a card has the
	 * same count) if all cards are made we add additional 100 match points
	 * 
	 * @param cards
	 * @return get total points of a round.
	 */
	public int getPoints(List<Card> cards) {
		int points = getMatchPoints(cards);
		return points + getPointsOfSpielart(cards);
	}

	/**
	 * Computes the points of the cards, (not in every gametype a card has the
	 * same count)
	 * 
	 * @param cards
	 * @return total points
	 */
	protected abstract int getPointsOfSpielart(List<Card> cards);

	/**
	 * Computes the points of the card, (not in every gametype a card has the
	 * same count)
	 * 
	 * @param cards
	 * @return points
	 */
	protected abstract int getPoint(Card cards);

	/**
	 * Compare the two cards given as parameter
	 * 
	 * @param currentlyHighestCard
	 * @param card
	 * @return true if second card is higher.
	 */
	public abstract boolean isSecondCardHigher(Card currentlyHighestCard,
			Card card);

	/**
	 * Checks if the card witch the user played is at the current game state
	 * (including laying card) valid
	 * 
	 * @param user
	 * @param layedCard
	 * @param r
	 * @return
	 */
	public abstract boolean isPlayedCardVaild(User user, Card layedCard, Round r);

	/**
	 * gets the qualifer of the game type.
	 * 
	 * @return
	 */
	public abstract int getQualifier();

	/**
	 * if the List contains all card of a round we return the additional
	 * MATCHPOINTS
	 * 
	 * @param cards
	 * @return MATCHPOINTS if all cards
	 */
	public int getMatchPoints(List<Card> cards) {
		return (cards.size() == Card.TOTALCARD) ? MATCHPOINTS : 0;
	}

	/**
	 * check if the card is of the family type given by the family parameter
	 * 
	 * @param card
	 * @param family
	 * @return true if same family
	 */
	protected boolean isSameFamily(Card card, CardFamily family) {
		return card.getFamily().equals(family);
	}

	/**
	 * Checks if in the list of cards is a card with the family type given by
	 * the parameter
	 * 
	 * @param cards
	 * @param family
	 * @return true if it has a card.
	 */
	protected boolean hasCardOfFamily(List<Card> cards, CardFamily family) {

		if (cards == null || cards.size() == 0)
			return false;

		for (Card c : cards) {
			if (isSameFamily(c, family))
				return true;
		}
		return false;
	}
}
