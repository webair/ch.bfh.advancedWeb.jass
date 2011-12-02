package ch.frickler.jass.logic;

import ch.frickler.jass.logic.definitions.ISpielart;
import ch.frickler.jass.service.GameService;

public class BaseTest {

	
	public GameService GetSpiel(ISpielart spielart) throws Exception{
		/*
		Spiel p = new Spiel("u");
		Round round = new Round(spielart);
		p.SetRound(round);
		
		Spieler p1 = new Spieler();
		Spieler p2 = new Spieler();
		Spieler p3 = new Spieler();
		Spieler p4 = new Spieler();

		try {
			p.addSpieler(p1);
			p.addSpieler(p2);
			p.addSpieler(p3);
			p.addSpieler(p4);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		p.getRound().setSpielart(spielart);
		return p;
		*/
		return null;
	}

	public GameService GetVerteiltesSpiel(ISpielart spielart) throws Exception{
		/*
		Spiel p = GetSpiel(spielart);
		KartenVerteilAction kva = new KartenVerteilAction(null);
		kva.doAction(p);
		p.getRound().setSpielart(spielart);
		return p;
		*/
		return null;
	}
}
