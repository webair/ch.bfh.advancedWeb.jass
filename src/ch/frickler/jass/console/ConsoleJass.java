package ch.frickler.jass.console;

import ch.frickler.jass.definitions.ISpielart;
import ch.frickler.jass.logic.Card;
import ch.frickler.jass.logic.KartenVerteilAction;
import ch.frickler.jass.logic.Runde;
import ch.frickler.jass.logic.Spiel;
import ch.frickler.jass.logic.Spieler;
import ch.frickler.jass.logic.Spielfeld;
import ch.frickler.jass.logic.Trumpf;
import ch.frickler.jass.logic.Card.CardFamily;

public class ConsoleJass {

	static public void main(String [] argv){
		ISpielart spielart = new Trumpf(Card.CardFamily.Egge);
		Spiel game = new Spiel();
		Runde round = new Runde();
		round.SetSpielart(spielart);
		game.SetRound(round);
		Spieler p1 = new Spieler("Tom Chiller");
		Spieler p2 = new Spieler("Krugi");
		Spieler p3 = new GUISpieler("Aschax");
		Spieler p4 = new Spieler("Blöffer aka Webair");

		try {
			game.addSpieler(p1);
			game.addSpieler(p2);
			game.addSpieler(p3);
			game.addSpieler(p4);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		KartenVerteilAction kva = new KartenVerteilAction();
		kva.doAction(game);
		for(Spieler spl : game.getAllSpieler()){
			System.out.println("Karten Spieler "+spl.getName()+" : "+spl.getCards());
		}
		Spielfeld f = game.getSpielfeld();
		for(int i = 0;i<9;i++){
			for(Spieler spl : game.getAllSpielerSorted(game.getRound().getAusspieler())){
				Card c = spl.forcePlay(f);
				f.addCard(c);
			}
			Spieler stichMaker = game.placeStich(f.getCards());
			game.getRound().setAusspieler(stichMaker);
			f.removeCards();
			int pointsTeam1 = spielart.getPoints(game.getTeams().get(0).getCards());
		    int pointsTeam2 = spielart.getPoints(game.getTeams().get(1).getCards());
			System.out.println("Punkte Team 1 "+pointsTeam1);
			System.out.println("Punkte Team 2 "+pointsTeam2);
			System.out.println("Total "+(pointsTeam2+pointsTeam1));
			
		}
		
		
		
		
	}
	
}
