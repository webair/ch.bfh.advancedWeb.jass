package ch.frickler.jass.logic;


import java.util.List;

import ch.frickler.jass.definitions.ISpielart;


public class Runde {

	private List<Card> gelegteKarten;
	private ISpielart spielArt;
	private Spieler currentSpieler;
	private Spieler ausSpieler = null;
	public ISpielart getSpielart() {
		return spielArt;
	}
	public void setAusspieler(Spieler ausSpieler) {
		this.ausSpieler = ausSpieler;
	}
	public Spieler getAusspieler() {
		return ausSpieler;
	}
	public void SetSpielart(ISpielart spielart) {
		System.out.println("Spielart selected: "+spielart.toString());
		this.spielArt = spielart;		
	}
	
	
}
