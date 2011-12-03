package ch.frickler.jass.service;

import java.util.List;

import ch.frickler.jass.db.entity.Card;
import ch.frickler.jass.db.entity.GameType;
import ch.frickler.jass.db.entity.Round;
import ch.frickler.jass.db.entity.User;
import ch.frickler.jass.db.enums.CardFamily;
import ch.frickler.jass.db.enums.GameKind;
import ch.frickler.jass.logic.definitions.ISpielart;

public class GameTypeService {

	private ISpielart spielart;
	private GameKind type;
	
	public GameTypeService(GameKind type){
		this.type = type;
		Init();
	}

	private void Init() {
		switch(type){
		case BOTTOMUP:
			spielart = new Ungeufe();
			break;
		case TOPDOWN:
			spielart = new Obenabe();
			break;
		case TRUMPBucket:
			spielart = new Trumpf(CardFamily.SCHAUFEL);
			break;
		case TRUMPCross:
			spielart = new Trumpf(CardFamily.SCHAUFEL);
			break;
		case TRUMPHeart:
			spielart = new Trumpf(CardFamily.HERZ);
			break;
		case TRUMPEdge:
			spielart = new Trumpf(CardFamily.ECKEN);
			break;
		}
	}

	public int getQualifier() {
		return spielart.getQualifier();
	}

	public boolean isTrumpf() {
		return spielart instanceof Trumpf;
	}

	public CardFamily getTrumpfCardFamily() {
		Trumpf pf = (Trumpf)spielart;
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
