package ch.frickler.jass.logic;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Team {

	private String teamname;
	private List<Spieler> spieler = new ArrayList<Spieler>();
	private List<Card> stiche = new ArrayList<Card>();
	public Team(Spieler spieler) {
		 this(spieler,"Team "+spieler.getName());
		}
	public Team(Spieler spieler,String teamname) {
		this.teamname = teamname;
		this.spieler.add(spieler);
		}
	public List<Spieler> getSpieler() {
		return this.spieler;
	}
	public void addSpieler(Spieler spieler2) {
		this.spieler.add(spieler2);
		
	}
	public void addCard(List<Card> cards) {
			this.stiche.addAll(cards);
	}
	
	public void clearCards(){
		stiche = new ArrayList<Card>();
	}
	public List<Card> getCards() {
		return stiche;
		
	}
	public String getName() {
		return this.teamname;
	}
	public String toString(){
		return "Team: "+teamname+" Spieler: "+getSpieler();
	}
}
