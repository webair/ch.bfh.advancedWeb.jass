package ch.frickler.jass.console;

import ch.frickler.jass.definitions.ISpielart;
import ch.frickler.jass.definitions.ISpieler;
import ch.frickler.jass.logic.Card;
import ch.frickler.jass.logic.KartenVerteilAction;
import ch.frickler.jass.logic.Round;
import ch.frickler.jass.logic.Spiel;
import ch.frickler.jass.logic.Spieler;
import ch.frickler.jass.logic.Spielfeld;
import ch.frickler.jass.logic.Trumpf;
import ch.frickler.jass.logic.Card.CardFamily;
import ch.frickler.jass.logic.Round.RoundResult;

public class ConsoleJass {

	static public void main(String[] argv) {
		ISpielart spielart = new Trumpf(Card.CardFamily.Egge);
		Spiel game = new Spiel();
		Round round = new Round(spielart);
		game.SetRound(round);
		Spieler p1 = new Spieler("Tom Chiller");
		Spieler p2 = new Spieler("Krugi");
		ISpieler p3 = new GUISpieler("Aschax");
		Spieler p4 = new Spieler("Blöffer aka Webair");

		try {
			game.addSpieler(p1);
			game.addSpieler(p2);
			game.addSpieler(p3);
			game.addSpieler(p4);
		} catch (Exception e) {
			e.printStackTrace();
		}

		boolean gamefinished = false;

		while (!gamefinished) {
			RoundResult r = game.playRound();
			if (r == RoundResult.QuitGame)
				gamefinished = true;
			
			if(game.getLeadingTeam().getPoints()>game.getWinPoints()){
				
			}
			
			
		}
	}
}
