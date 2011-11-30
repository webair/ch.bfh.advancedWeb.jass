package ch.frickler.jass.logic;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import ch.frickler.jass.logic.Card.CardFamily;
import ch.frickler.jass.logic.Card.CardValue;
import ch.frickler.jass.logic.definitions.ISpielart;
import ch.frickler.jass.logic.definitions.ISpieler;


public class BasePlayingTest {

	@Test
	public void EveryPlayerOneCard() throws Exception {
	
		Spiel p = GetVerteiltesSpiel(new Obenabe());
		Round f = p.getRound();
		
		for(ISpieler spl : p.getAllSpieler()){
			Card c =((JUALayCard)spl.forcePlay(f)).getCard();
			p.playCard(spl, c);
		}
		
		for(ISpieler spl : p.getAllSpieler()){
			Assert.assertEquals(8,spl.getCards().size());
		}
		int doneCards = p.getTeams().get(0).getCards().size() +p.getTeams().get(1).getCards().size();
		Assert.assertEquals(4,doneCards);

	}
	
	@Test
	public void cardsAfterPlayingOneRound() throws Exception {
		ISpielart sart = new Obenabe();
		Spiel p = GetVerteiltesSpiel(sart);
		
		Round f = p.getRound();
		
		for(int i = 0;i < 9;i++){
			for(ISpieler spl : p.getAllSpieler()){
				Card c = ((JUALayCard)spl.forcePlay(f)).getCard();
				p.playCard(spl, c);
			}
			if(i != 8){
				int doneCards = p.getTeams().get(0).getCards().size() +p.getTeams().get(1).getCards().size();
				Assert.assertEquals((i+1)*4,doneCards);
			}
		}
		
		
		for(ISpieler spl : p.getAllSpieler()){
			Assert.assertEquals(0,spl.getCards().size());
		}
		
		int donePoints = p.getTeams().get(0).getPoints() +p.getTeams().get(1).getPoints();
		Assert.assertEquals(donePoints,157*sart.getQualifier());
		
	}

	@Test
	public void StichAfterOneRound1() throws Exception{
		Spiel p = GetSpiel(new Obenabe());
		Round f = p.getRound();
		
		List<ISpieler> sp =	p.getAllSpieler();
		sp.get(0).addCard(new Card(CardValue.Neun,CardFamily.Herz));
		sp.get(1).addCard(new Card(CardValue.Sechs,CardFamily.Herz));
		sp.get(2).addCard(new Card(CardValue.Koenig,CardFamily.Herz));
		sp.get(3).addCard(new Card(CardValue.Ass,CardFamily.Herz));
		
		for(ISpieler spl : p.getAllSpieler()){
			Card c = ((JUALayCard)spl.forcePlay(f)).getCard();
			f.addCard(c);
		}
		
		p.placeStich(f.getCards());
		// stich geht ans 2te team
		Assert.assertEquals(0,p.getTeams().get(0).getCards().size());
		// stich geht ans 2te team
		Assert.assertEquals(4,p.getTeams().get(1).getCards().size());
	
		Assert.assertEquals(15,p.getRound().getSpielart().getPoints(p.getTeams().get(1).getCards()));
	}
	
	@Test
	public void StichAfterOneRound2() throws Exception{
		Spiel p = GetSpiel(new Trumpf(CardFamily.Herz));
		Round f = p.getRound();
		
		List<ISpieler> sp =	p.getAllSpieler();
		sp.get(0).addCard(new Card(CardValue.Sieben,CardFamily.Herz));
		sp.get(1).addCard(new Card(CardValue.Ass,CardFamily.Herz));
		sp.get(2).addCard(new Card(CardValue.Sechs,CardFamily.Herz));
		sp.get(3).addCard(new Card(CardValue.Bauer,CardFamily.Herz));
		
		for(ISpieler spl : p.getAllSpieler()){
			Card c = ((JUALayCard)spl.forcePlay(f)).getCard();
			f.addCard(c);
		}
		
		p.placeStich(f.getCards());
		// stich geht ans 2te team
		Assert.assertEquals(0,p.getTeams().get(0).getCards().size());
		// stich geht ans 2te team
		Assert.assertEquals(4,p.getTeams().get(1).getCards().size());
	
		Assert.assertEquals(31,p.getRound().getSpielart().getPoints(p.getTeams().get(1).getCards()));
		Assert.assertEquals(0,p.getRound().getSpielart().getPoints(p.getTeams().get(0).getCards()));
	}
	
	
	public Spiel GetSpiel(ISpielart spielart) throws Exception{
		Spiel p = new Spiel();
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
		return p;
	}

	public Spiel GetVerteiltesSpiel(ISpielart spielart) throws Exception{
		Spiel p = GetSpiel(spielart);
		KartenVerteilAction kva = new KartenVerteilAction(null);
		kva.doAction(p);
		return p;
	}
	
	@Test
	public void AusspielerTest() throws Exception{
		Spiel p = GetSpiel(new Obenabe());
		Round f = p.getRound();
		
		List<ISpieler> sp =	p.getAllSpieler();
		sp.get(0).addCard(new Card(CardValue.Neun,CardFamily.Herz));
		sp.get(1).addCard(new Card(CardValue.Sechs,CardFamily.Herz));
		sp.get(2).addCard(new Card(CardValue.Koenig,CardFamily.Herz));
		sp.get(3).addCard(new Card(CardValue.Ass,CardFamily.Herz));

		sp.get(0).addCard(new Card(CardValue.Acht,CardFamily.Herz));
		sp.get(1).addCard(new Card(CardValue.Neun,CardFamily.Egge));
		sp.get(2).addCard(new Card(CardValue.Koenig,CardFamily.Egge));
		sp.get(3).addCard(new Card(CardValue.Sechs,CardFamily.Egge));
		
		for(ISpieler spl : p.getAllSpielerSorted(null)){
			Card c = ((JUALayCard)spl.forcePlay(f)).getCard();
			p.playCard(spl, c);
		}
		
		ISpieler pSticher = p.placeStich(f.getCards());
		f.removeCards();
		Assert.assertEquals(sp.get(3), pSticher);
		
		for(ISpieler spl : p.getAllSpielerSorted(pSticher)){
			Card c = ((JUALayCard)spl.forcePlay(f)).getCard();
			f.addCard(c);
		}
		
		ISpieler pSticher2 = p.placeStich(f.getCards());
		f.removeCards();
		
		Assert.assertEquals(sp.get(2), pSticher2);
		
		
		
	}
}
