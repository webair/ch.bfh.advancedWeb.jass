package ch.frickler.jass.logic.definitions;


import java.util.ArrayList;
import java.util.List;
import ch.frickler.jass.logic.Card;
import ch.frickler.jass.logic.Card.CardFamily;
import ch.frickler.jass.logic.definitions.ISpielart;
import ch.frickler.jass.logic.definitions.IUserAction;
import ch.frickler.jass.logic.Round;

public abstract class ISpieler {

	protected String name;
	
	//todo make sortable and sort the card @ addCard
	protected ArrayList<Card> cards;
	public ISpieler(String name) {
		this.name = name;
		System.out.println("Spieler created: "+name);
	}
	public ISpieler() {
		this("Fred "+(int)(Math.random()*2000)+1);
	}
		
	public void addCard(Card card) {
		if(cards == null){
			cards = new ArrayList<Card>();
		}
		
		cards.add(card);	
	}
	public List<Card> getCards() {
		return cards;
	}
	
	public abstract ISpielart sayTrumpf(boolean canSchieben);
	
	public abstract IUserAction forcePlay(Round round) throws Exception;
	
	public String getName() {
		return this.name;
	}
	
	public String toString() {
		return this.name;
	}
	
	public boolean equals(ISpieler s){
		return  s.name == this.name;
	}
	public boolean hasCardOfFamily(CardFamily cardFamily) {
		
		if(this.cards == null || this.cards.size() == 0)
			return false;		
		
		for(Card c : cards){
			if(c.getCardFamily() == cardFamily)
				return true;
		}
		return false;
	}
	public void removeCard(Card layedCard) {
		this.cards.remove(layedCard);
		
	}
	

	
}