package ch.frickler.jass.service;

import java.util.List;

import ch.frickler.jass.db.entity.Card;
import ch.frickler.jass.db.entity.Round;
import ch.frickler.jass.db.entity.User;
import ch.frickler.jass.db.enums.CardFamily;
import ch.frickler.jass.db.enums.GameKind;
import ch.frickler.jass.gametype.TopDown;
import ch.frickler.jass.gametype.Trump;
import ch.frickler.jass.gametype.BottomUp;
import ch.frickler.jass.logic.definitions.JassGameType;

public class GameTypeService {

	private JassGameType spielart;
	private GameKind type;
	
	public GameTypeService(GameKind type){
		this.type = type;
		Init();
	}

	private void Init() {
		switch(type){
		case BOTTOMUP:
			spielart = new BottomUp();
			break;
		case TOPDOWN:
			spielart = new TopDown();
			break;
		case TRUMPCross:
			spielart = new Trump(CardFamily.KREUZ);
			break;
		case TRUMPBucket:
			spielart = new Trump(CardFamily.SCHAUFEL);
			break;
		case TRUMPHeart:
			spielart = new Trump(CardFamily.HERZ);
			break;
		case TRUMPEdge:
			spielart = new Trump(CardFamily.ECKEN);
			break;
		}
	}

	public int getQualifier() {
		return spielart.getQualifier();
	}

	public boolean isTrumpf() {
		return spielart instanceof Trump;
	}

	public CardFamily getTrumpfCardFamily() {
		Trump pf = (Trump)spielart;
		if(pf == null)
			return null;		
		return pf.getCardFamily();
	}

	public boolean isSecondCardHigher(Card card, Card card2) {
		return spielart.isSecondCardHigher(card, card2);
	}

	public int countPoints(List<Card> wonCards) {
		return spielart.getPoints(wonCards);
	}

	public boolean isPlayedCardVaild(User spl, Card layedCard, Round r) {
		return spielart.isPlayedCardVaild(spl, layedCard, r);
	}
	
}
