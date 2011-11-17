package ch.frickler.jass.logic;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;

public class Spieler {

	protected String name;
	
	//todo make sortable and sort the card @ addCard
	private ArrayList<Card> cards;
	public Spieler(String name) {
		this.name = name;
		System.out.println("Spieler created: "+name);
	}
	public Spieler() {
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
		
	public Card forcePlay(Spielfeld spielfeld){
		
		// if a card is laying 
		if(spielfeld.getCards().size() > 0){
			//play one of the same family	
			for(int i = 0;i < cards.size();i++){
				
				if(cards.get(i).getCardFamily() == spielfeld.getCards().get(0).getCardFamily()){
					return  cards.remove(i);
				}
			}
		}
		
		// else play the first card.
		return cards.remove(0);
	}
	public String getName() {
		return this.name;
	}
	
	public String toString() {
		return this.name;
	}
	
	public boolean equals(Spieler s){
		return  s.name == this.name;
	}
	

	
}
