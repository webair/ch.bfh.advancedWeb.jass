package ch.frickler.jass.logic;

import java.util.List;

import ch.frickler.jass.definitions.ISpielart;
import ch.frickler.jass.definitions.ISpieler;
import ch.frickler.jass.logic.Card.CardFamily;

public class Trumpf extends ISpielart {

	private CardFamily trumpf;

	public Trumpf(CardFamily family) {
		this.trumpf = family;
	}

	@Override
	public int getPoints(List<Card> cards) {
		int points = 0;
		for (Card c : cards)
			points += getPoint(c);
		return points;
	}

	@Override
	protected int getPoint(Card card) {
		switch (card.getCardValue()) {
		case Sechs:
			return 0;
		case Sieben:
			return 0;
		case Acht:
			return 0;
		case Neun:
			// is trumpf neun
			if (card.isSameFamily(this.trumpf)) {
				return 14;
			}
			return 0;
		case Zehn:
			return 10;
		case Bauer:
			// is trumpf bauer
			if (card.isSameFamily(this.trumpf)) {
				return 20;
			}
			return 2;
		case Dame:
			return 3;
		case Koenig:
			return 4;
		case Ass:
			return 11;
		}
		return 0;
	}

	@Override
	public boolean isSecondCardHigher(Card highestCard, Card card) {

		if (highestCard.getCardFamily() == trumpf
				&& card.getCardFamily() == trumpf) {
			// beide karten sind trumpf
			if (getTrumpfOrderValue(highestCard) < getTrumpfOrderValue(card)) {
				return true;
			}
		} else if (!(highestCard.getCardFamily() == trumpf)
				&& card.getCardFamily() == trumpf) {
			// nur secondcard ist trumpf
			return true;
		} else if (highestCard.getCardFamily() == trumpf
				&& !(card.getCardFamily() == trumpf)) {
			// nur highestcard ist trumpf
			return false;
		} else {
			// beide kein trumpf
			if (highestCard.getCardFamily() == card.getCardFamily()) {
				// aber von der gleichen famile
				if (highestCard.getOrderValue() < card.getOrderValue()) {
					return true;
				}
			}
		}
		return false;
	}

	public int getTrumpfOrderValue(Card card) {

		switch (card.getCardValue()) {
		case Sechs:
			return 6;
		case Sieben:
			return 7;
		case Acht:
			return 8;
		case Zehn:
			return 10;
		case Dame:
			return 11;
		case Koenig:
			return 12;
		case Ass:
			return 13;
		case Neun:
			return 14;
		case Bauer:
			return 15;
		}
		return 0;
	}

	public String toString() {
		return "Spielart: Trumpf (" + trumpf.name() + ")";
	}

	@Override
	public boolean isPlayedCardVaild(ISpieler spl, Card layedCard, Round r) {

		if (r.getCards().size() == 0)
			return true;

		Card firstcard = r.getCards().get(0);

		if (firstcard.getCardFamily() == layedCard.getCardFamily())
			return true;

		// user hat nicht die gleiche karte gelegt wie ausgegben wurde
		if (firstcard.isSameFamily(this.trumpf)) {
			int anztrumpf = 0;
			boolean hasBuur = false;
			for (Card c : spl.getCards()) {
				if (c.isSameFamily(this.trumpf)) {
					anztrumpf++;
					if (c.getCardValue() == Card.CardValue.Bauer) {
						// user hat trumpfbauer
						hasBuur = true;
					}
				}
			}
			if (anztrumpf == 1 && hasBuur) {
				// user hat nur noch ein trumpf in der hand aber eine andere
				// cardfamily gelegt
				// dies ist ok, der bauer muss nicht lei gehaltet werden
				return true;
			} else if (anztrumpf > 1) {
				// nicht lei gehaltet werden
				return false;
			} else {
				return true;
			}

		}
		// firstcard is not trumpf

		// ausgespielte karte ist nicht trumpf und user hat cardfamily der
		// ersten karte in der hat
		// diese aber nicht gespielt und auch kein trumpf gespielt
		if (spl.hasCardOfFamily(firstcard.getCardFamily())
				&& !layedCard.isSameFamily(trumpf))
			return false;
		// falls er trumpf gespielt hat darf er nicht anderer spieler untertrumpfen
		if (layedCard.isSameFamily(trumpf)) {
			for (int i = 1; i < r.getCards().size(); i++) {
				if (r.getCards().get(i).isSameFamily(trumpf)
						&& getTrumpfOrderValue(r.getCards().get(i)) > getTrumpfOrderValue(layedCard)) {
					// user hat spieler i unter trumpft, nicht gültig
					return false;
				}
			}
		}

		// todo sind das alle ausspiel regeln für trumpf

		return true;
	}

}
