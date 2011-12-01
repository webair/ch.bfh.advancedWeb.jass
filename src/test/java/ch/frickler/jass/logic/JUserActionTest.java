package ch.frickler.jass.logic;

import org.junit.Assert;
import org.junit.Test;

import ch.frickler.jass.logic.Card.CardFamily;
import ch.frickler.jass.logic.definitions.ISpieler;

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
	
	
	@Test
	public void AnsageTest() throws Exception {
		
		
		Spiel p = GetVerteiltesSpiel(null);			
		Assert.assertEquals(Spiel.GameState.Ansage,p.getState());
		
		JUAAnsagen ansagen = new JUAAnsagen(p.getAllSpieler().get(0),new Trumpf(CardFamily.Egge));
		
		Assert.assertTrue(ansagen.isActionPossible(p));
		
		ansagen.doAction(p);
		
		Assert.assertTrue(p.getRound().getSpielart() instanceof Trumpf);
		
		
		Assert.assertEquals(Spiel.GameState.Play,p.getState());
		
		ISpieler one = p.getAllSpieler().get(0);
		
		Card c = one.getCards().get(0);
		
		JUALayCard playCard = new JUALayCard(one,c);
		
		JUALayCard wrongCard = new JUALayCard(one,p.getAllSpieler().get(1).getCards().get(0));
		
		
		Assert.assertTrue(playCard.isActionPossible(p));
		Assert.assertFalse(wrongCard.isActionPossible(p));
		
		
	}
	
	
	
}
