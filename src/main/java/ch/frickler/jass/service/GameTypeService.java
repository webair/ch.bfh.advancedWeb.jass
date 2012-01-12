package ch.frickler.jass.service;

import java.util.List;

import ch.frickler.jass.db.entity.Card;
import ch.frickler.jass.db.entity.Round;
import ch.frickler.jass.db.entity.User;
import ch.frickler.jass.db.enums.CardFamily;
import ch.frickler.jass.db.enums.GameKind;
import ch.frickler.jass.definitions.JassGameType;
import ch.frickler.jass.gametype.TopDown;
import ch.frickler.jass.gametype.Trump;
import ch.frickler.jass.gametype.BottomUp;

/**
 * Helper class for the game type
 * @author seed
 * 
 */
public class GameTypeService {

	private JassGameType gameType;
	private GameKind type;
	
	public GameTypeService(GameKind type){
		this.type = type;
		Init();
	}

	private void Init() {
		switch(type){
		case BOTTOMUP:
			gameType = new BottomUp();
			break;
		case TOPDOWN:
			gameType = new TopDown();
			break;
		case TRUMPCross:
			gameType = new Trump(CardFamily.KREUZ);
			break;
		case TRUMPBucket:
			gameType = new Trump(CardFamily.SCHAUFEL);
			break;
		case TRUMPHeart:
			gameType = new Trump(CardFamily.HERZ);
			break;
		case TRUMPEdge:
			gameType = new Trump(CardFamily.ECKEN);
			break;
		}
	}

	/**
	 * @return return the qualifier of the game type
	 */
	public int getQualifier() {
		return gameType.getQualifier();
	}

	/**
	 * @return true if game type has trump (all except bottom-top and top-down)
	 */
	public boolean isTrumpf() {
		return gameType instanceof Trump;
	}

	/**
	 * @return card families of the trump
	 */
	public CardFamily getTrumpfCardFamily() {
		Trump pf = (Trump)gameType;
		if(pf == null)
			return null;		
		return pf.getCardFamily();
	}

	/**
	 * @param card
	 * @param card2
	 * @return true if second card is higher
	 */
	public boolean isSecondCardHigher(Card card, Card card2) {
		return gameType.isSecondCardHigher(card, card2);
	}

	/**
	 * @param wonCards
	 * @return calculates the points of the given cards
	 */
	public int countPoints(List<Card> wonCards) {
		return gameType.getPoints(wonCards);
	}

	/**
	 *
	 * 
	 * @param spl
	 * @param layedCard
	 * @param r
	 * @return true if the played card is valid
	 */
	public boolean isPlayedCardVaild(User spl, Card layedCard, Round r) {
		return gameType.isPlayedCardVaild(spl, layedCard, r);
	}
	
}
