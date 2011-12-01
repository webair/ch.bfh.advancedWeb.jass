package ch.frickler.jass.logic;

import org.junit.Assert;
import org.junit.Test;

public class JUserActionTest  extends BaseTest {

	
	
	
	@Test
	public void InitTest() throws Exception {
	
		Spiel p = new Spiel();
			
		Assert.assertEquals(Spiel.GameState.WaitForPlayers,p.getState());
		
		Spieler p1 = new Spieler();
		Spieler p2 = new Spieler();
		Spieler p3 = new Spieler();
		Spieler p4 = new Spieler();

		try {
			p.addSpieler(p1);
			p.addSpieler(p2);
			p.addSpieler(p3);
			Assert.assertEquals(Spiel.GameState.WaitForPlayers,p.getState());
			p.addSpieler(p4);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		Assert.assertEquals(Spiel.GameState.WaitForCards,p.getState());
		
		KartenVerteilAction action = new KartenVerteilAction(null);
		Assert.assertTrue(action.isActionPossible(p));
	
		JUAAnsagen ansageP1 = new JUAAnsagen(p1,new Obenabe());
		JUAAnsagen ansageP1NoSpielart = new JUAAnsagen(p1);
		Assert.assertFalse(ansageP1.isActionPossible(p));
		
		action.doAction(p);
		Assert.assertEquals(Spiel.GameState.Ansage,p.getState());

		Assert.assertTrue(ansageP1.isActionPossible(p));
		Assert.assertFalse(ansageP1NoSpielart.isActionPossible(p));
		JUAAnsagen ansageP2 = new JUAAnsagen(p2,new Ungeufe());
		Assert.assertFalse(ansageP2.isActionPossible(p));
		
		
		

	}
	
	
}
