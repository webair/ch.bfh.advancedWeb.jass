package ch.frickler.jass.logic;

public class Card implements Comparable<Card> {

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
	 * Sortiert die Karten fuer die richtige reihenfolge in der Hand
	 *  Herz, Egge, Schaufel, Kreuz
	 */
	public int compareTo(Card card) {
			
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
	public static int getTotalCards() {
		return CardFamily.values().length * CardValue.values().length;
	}

}
