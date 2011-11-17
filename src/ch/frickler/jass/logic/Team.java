package ch.frickler.jass.logic;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ch.frickler.jass.definitions.ISpieler;

public class Team {

	private String teamname;
	private List<ISpieler> spieler = new ArrayList<ISpieler>();
	private List<Card> stiche = new ArrayList<Card>();
	private int totalpoints = 0;
	public Team(ISpieler spieler2) {
		 this(spieler2,"Team "+spieler2.getName());
		}
	public Team(ISpieler spieler,String teamname) {
		this.teamname = teamname;
		this.spieler.add(spieler);
		}
	public List<ISpieler> getSpieler() {
		return this.spieler;
	}
	public void addSpieler(ISpieler spieler2) {
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
	public int getPoints() {
		// TODO Auto-generated method stub
		return totalpoints;
	}
	public void addPoints(int points){
		this.totalpoints += points;
	}
}
