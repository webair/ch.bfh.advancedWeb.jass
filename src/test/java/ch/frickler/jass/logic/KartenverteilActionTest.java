package ch.frickler.jass.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import ch.frickler.jass.logic.definitions.ISpieler;

public class KartenverteilActionTest {

	@Test
	public void CheckAnzahl() {

		Spiel p = new Spiel();

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
			fail();
		}

		KartenVerteilAction kva = new KartenVerteilAction();

		kva.doAction(p);
		assertEquals(p.getAllSpieler().size(), 4);
		for (ISpieler player : p.getAllSpieler()) {
			assertEquals(9, player.getCards().size());
			System.out.println(player.getCards());
		}

	}

}
