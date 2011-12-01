package ch.frickler.jass.logic.console;

import ch.frickler.jass.logic.Card;
import ch.frickler.jass.logic.KartenVerteilAction;
import ch.frickler.jass.logic.Round;
import ch.frickler.jass.logic.Spiel;
import ch.frickler.jass.logic.Spieler;
import ch.frickler.jass.logic.Spielfeld;
import ch.frickler.jass.logic.Trumpf;
import ch.frickler.jass.logic.Card.CardFamily;
import ch.frickler.jass.logic.Round.RoundResult;
import ch.frickler.jass.logic.definitions.ISpielart;
import ch.frickler.jass.logic.definitions.ISpieler;

public class ConsoleJass {

	static public void main(String[] argv) {
		ISpielart spielart = new Trumpf(Card.CardFamily.Egge);
		Spiel game = new Spiel("u");
		game.setWinPoints(1000);
		Round round = new Round(spielart);
		game.SetRound(round);
		Spieler p1 = new Spieler("Tom Chiller");
		Spieler p2 = new Spieler("Krugi");
		ISpieler p3 = new GUISpieler("Aschax");
		Spieler p4 = new Spieler("Bloeffer aka Webair");

		try {
			game.addSpieler(p1);
			game.addSpieler(p2);
			game.addSpieler(p3);
			game.addSpieler(p4);

			boolean gamefinished = false;

			while (!gamefinished) {
				game.initNewRound();
				RoundResult r = game.playRound();
				if (r == RoundResult.QuitGame)
					gamefinished = true;

				if (game.getLeadingTeam().getPoints() > game.getWinPoints()) {
					System.out.print("Spiel beendet winPoints "+game.getWinPoints()+" von team "+game.getLeadingTeam().getName()+" erreicht.("+game.getLeadingTeam().getPoints()+"). ");
					gamefinished = true;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
