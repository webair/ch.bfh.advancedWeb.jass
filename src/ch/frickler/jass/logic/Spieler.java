package ch.frickler.jass.logic;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;

import ch.frickler.jass.definitions.IUserAction;

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
		
	public IUserAction forcePlay(Round round) throws Exception{
		
		// if a card is laying 
		if(round.getCards().size() > 0){
			//play one of the same family	
			for(int i = 0;i < cards.size();i++){
				
				if(cards.get(i).getCardFamily() == round.getCards().get(0).getCardFamily()){
					return new JUALayCard(cards.remove(i));
				}
			}
		}
		
		// else play the first card.
		return  new JUALayCard(cards.remove(0));
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
