package ch.frickler.jass.logic;

import ch.frickler.jass.logic.Card.CardFamily;
import ch.frickler.jass.logic.Card.CardValue;

public class Card implements Comparable {

	public Card(CardValue value, CardFamily family) {
		this.value = value;
		this.family = family;
	}
	private CardFamily family;
	private CardValue  value;
	
	public enum CardFamily { Herz, Schaufel, Kreuz, Egge };
	public enum CardValue { Sechs,Sieben,Acht,Neun,Zehn, Bauer , Dame, Koenig,Ass }
	public int getOrderValue() {
		
		switch(value){
		case Sechs:
			return 6;
		case Sieben:
			return 7;
		case Acht:
			return 8;
		case Neun:
			return 9;
		case Zehn:
			return 10;
		case Bauer:
			return 11;
		case Dame:
			return 12;
		case Koenig:
			return 13;
		case Ass:
			return 14;
		}
		
		return 0;
	}
	public CardValue getCardValue() {
		return this.value;
	}
	public CardFamily getCardFamily() {
		return this.family;
	}
	public String toString(){
		return this.family.name()+"."+this.value.name();
	}
	public boolean isSameFamily(CardFamily trumpf) {
		// TODO Auto-generated method stub
		return this.getCardFamily().name().equals(trumpf.name());
	}
	@Override
	/*
	 * Sortiert die Karten für die richtige reihenfolge in der Hand
	 *  Herz, Egge, Schaufel, Kreuz
	 */
	public int compareTo(Object obj) {
		
		if(!(obj instanceof Card)){
			return 0;	
		}
		Card card = (Card)obj;
		
		int Cardid = card.getCardSortOrder();
		int Thisid = this.getCardSortOrder();
		//todo what does signum
		return (int) Math.signum(Cardid-Thisid);
		
	}
	private int getCardSortOrder() {
		int familyValue =  0;
		switch(family){
		case Herz:
			familyValue =  60;
			break;
		case Egge:
			familyValue =  40;
			break;
		case Schaufel:
			familyValue =  0;
			break;
		}
		return familyValue+getOrderValue();
	}
}
