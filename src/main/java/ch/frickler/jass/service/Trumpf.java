package ch.frickler.jass.service;

import java.util.List;

import ch.frickler.jass.db.entity.Card;
import ch.frickler.jass.db.entity.Round;
import ch.frickler.jass.db.entity.User;
import ch.frickler.jass.db.enums.CardFamily;
import ch.frickler.jass.db.enums.CardValue;
import ch.frickler.jass.logic.definitions.JassGameType;


public class Trumpf extends JassGameType {

	public static final int ValueOfStoeck = 20;
	private CardFamily trumpf;

	public Trumpf(CardFamily herz) {
		this.trumpf = herz;
	}

	@Override
	protected int getPointsOfSpielart(List<Card> cards) {
		int points = 0;
		for (Card c : cards)
			points += getPoint(c);
		return points;
	}

	@Override
	protected int getPoint(Card card) {
		switch (card.getValue()) {
		case SECHS:
			return 0;
		case SIEBEN:
			return 0;
		case ACHT:
			return 0;
		case NEUN:
			// is trumpf neun
			if (isSameFamily(card,this.trumpf)) {
				return 14;
			}
			return 0;
		case ZEHN:
			return 10;
		case BAUER:
			// is trumpf bauer
			if (isSameFamily(card,this.trumpf)) {
				return 20;
			}
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

		if (highestCard.getFamily() == trumpf
				&& card.getFamily() == trumpf) {
			// beide karten sind trumpf
			if (getTrumpfOrderValue(highestCard) < getTrumpfOrderValue(card)) {
				return true;
			}
		} else if (!(highestCard.getFamily() == trumpf)
				&& card.getFamily() == trumpf) {
			// nur secondcard ist trumpf
			return true;
		} else if (highestCard.getFamily() == trumpf
				&& !(card.getFamily() == trumpf)) {
			// nur highestcard ist trumpf
			return false;
		} else {
			// beide kein trumpf
			if (highestCard.getFamily() == card.getFamily()) {
				// aber von der gleichen famile
				if (highestCard.getOrderValue() < card.getOrderValue()) {
					return true;
				}
			}
		}
		return false;
	}

	public int getTrumpfOrderValue(Card card) {

		switch (card.getValue()) {
		case SECHS:
			return 6;
		case SIEBEN:
			return 7;
		case ACHT:
			return 8;
		case ZEHN:
			return 10;
		case DAME:
			return 11;
		case KOENIG:
			return 12;
		case ASS:
			return 13;
		case NEUN:
			return 14;
		case BAUER:
			return 15;
		}
		return 0;
	}

	public String toString() {
		return "Spielart: Trumpf (" + trumpf.name() + ")";
	}

	@Override
	public boolean isPlayedCardVaild(User spl, Card layedCard, Round r) {

		if(!spl.getCards().contains(layedCard))
			return false;
		
		if (r.getCards().size() == 0)
			return true;

		Card firstcard = r.getCards().get(0);

		if (firstcard.getFamily() == layedCard.getFamily())
			return true;

		// user hat nicht die gleiche karte gelegt wie ausgegben wurde
		if (isSameFamily(firstcard,this.trumpf)) {
			int anztrumpf = 0;
			boolean hasBuur = false;
			for (Card c : spl.getCards()) {
				if (isSameFamily(c,this.trumpf)) {
					anztrumpf++;
					if (c.getValue() == CardValue.BAUER) {
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
		if (hasCardOfFamily(spl.getCards(),firstcard.getFamily())
				&& !isSameFamily(layedCard,trumpf))
			return false;
		// falls er trumpf gespielt hat darf er nicht anderer spieler untertrumpfen
		if (isSameFamily(layedCard,trumpf)) {
			for (int i = 1; i < r.getCards().size(); i++) {
				if (isSameFamily(r.getCards().get(i),trumpf)
						&& getTrumpfOrderValue(r.getCards().get(i)) > getTrumpfOrderValue(layedCard)) {
					// user hat spieler i unter trumpft, nicht gueltig
					return false;
				}
			}
		}

		// todo sind das alle ausspiel regeln fuer trumpf

		return true;
	}

	@Override
	public int getQualifier() {
		//schwarz doppelt, rot einfach
		return (this.trumpf == CardFamily.SCHAUFEL || this.trumpf == CardFamily.KREUZ) ? 2 : 1;
	}

	public CardFamily getCardFamily() {
		return this.trumpf;
	}
}
