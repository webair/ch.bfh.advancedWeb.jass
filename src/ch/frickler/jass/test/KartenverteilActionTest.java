package ch.frickler.jass.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import ch.frickler.jass.*;
import ch.frickler.jass.definitions.ISpielart;
import ch.frickler.jass.definitions.ISpieler;
import ch.frickler.jass.logic.*;
import ch.frickler.jass.logic.Card.CardFamily;
import ch.frickler.jass.logic.Card.CardValue;

import org.junit.Test;
import org.junit.internal.runners.statements.Fail;

import ch.frickler.jass.*;

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
