package ch.frickler.jass.db.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import ch.frickler.jass.db.enums.CardFamily;
import ch.frickler.jass.db.enums.CardValue;

/**
 * With this class it is possible to wies the hand ward
 * @author kaeserst
 *
 */
public class Wies implements Serializable, Comparable<Wies> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	List<Card> cards = new ArrayList<Card>();
	User user;

	public Wies(List<Card> c, User u) {
		this.user = u;
		this.cards.addAll(c);
	}

	@Override
	public int compareTo(Wies o) {

		return this.getWiesCompareValue() - o.getWiesCompareValue();

	}

	private int getWiesCompareValue() {
		int sameFourOfValuePoints = 0;
		int sameFamilyInRow = 0;
		if (isSameValue()) {
			sameFourOfValuePoints = 100 * cards.get(0)
					.getFourOfValueWiesPoints();
		} else {
			sameFamilyInRow = getPointsInRow(cards.size());
		}
		sameFamilyInRow *= 100;

		sameFamilyInRow += cards.get(0).getOrderValue();

		return sameFamilyInRow > sameFourOfValuePoints ? sameFamilyInRow
				: sameFourOfValuePoints;
	}

	private int getPointsInRow(int size) {

		switch (size) {
		case 3:
			return 20;
		case 4:
			return 50;
		case 5:
			return 100;
		case 6:
			return 150;
		case 7:
			return 200;
		case 8:
			return 250;
		case 9:
			return 300;
		}
		return 0;
	}

	private boolean isSameValue() {
		if (cards.size() != 4)
			return false;
		CardValue v = cards.get(0).getValue();
		for (int i = 1; i < cards.size(); i++) {
			if (!cards.get(i).getValue().equals(v)) {
				return false;
			}
		}
		return true;
	}

	public boolean isTrumpf(CardFamily trumpfCardFamily) {
		if (!isSameValue()) {
			return cards.get(0).getFamily().equals(trumpfCardFamily);
		}
		return false;
	}

	public User getUser() {
		return this.user;
	}

	public int getPoints() {
		if (isSameValue()) {
			return cards.get(0).getFourOfValueWiesPoints();
		} else {
			return getPointsInRow(cards.size());
		}
	}

	public boolean isCorrect() {
		if (isSameValue())
			return true;

		if (cards.size() >= 3)
			return true;

		// TODO check if in order
		return false;
	}

	public static List<Wies> getPossibleWies(User u) {
		List<Card> allCards = u.getCards();
		
		//fill the array for searching a wies
		int[][] cardArray = new int[CardValue.values().length][CardFamily
				.values().length];
		List<Wies> wies = new ArrayList<Wies>();
		for (Card c : allCards) {
			//System.out.println((c.getOrderValue() - 6) + " : "
			//		+ (c.getFamilyOrder() - 1) + " card: " + c.toString());
			cardArray[c.getOrderValue() - 6][c.getFamilyOrder() - 1] = 1;
		}
		
		
		// get for of same value
		for (int i = 0; i < CardValue.values().length; i++) {
			if (cardArray[i][0] + cardArray[i][1] + cardArray[i][2]
					+ cardArray[i][3] == CardFamily.values().length) {
				wies.add(getWiesOfSameValue(i + 6, allCards, u));
			}
		}
		// get for same family in a row
		for (int f = 0; f < CardFamily.values().length; f++) {
			int rowCount = 0;
			for (int v = 0; v < CardValue.values().length; v++) {
				if (cardArray[v][f] == 1) {
					rowCount++;
				} else {
					rowCount = 0;
				}
				if (rowCount >= 3) {
					wies.add(getWiesOfSameFamilyInRow(allCards, u, f + 1, v + 6
							- rowCount, rowCount));
				}

			}
		}
		return wies;
	}

	private static Wies getWiesOfSameFamilyInRow(List<Card> allCards, User u,
			int family, int firstValue, int rowCount) {
		List<Card> wiesCard = new ArrayList<Card>();
		for (Card c : allCards) {
			if (c.getOrderValue() >= firstValue
					&& c.getOrderValue() <= firstValue + rowCount
					&& c.getFamilyOrder() == family) {
				wiesCard.add(c);
			}
		}
		return new Wies(wiesCard, u);
	}

	private static Wies getWiesOfSameValue(int i, List<Card> allCards, User u) {
		List<Card> wiesCard = new ArrayList<Card>();
		for (Card c : allCards) {
			if (c.getOrderValue() == i) {
				wiesCard.add(c);
			}
		}
		return new Wies(wiesCard, u);
	}

	public String getKey() {
		String key = "";
		for (Card c : cards) {
			key += key.length() > 0 ? ";" : "";
			key += c.getFamily() + "," + c.getValue();
		}
		return key;
	}

	public String getName() {
		// TODO key and translation
		return getKey();
	}
}
