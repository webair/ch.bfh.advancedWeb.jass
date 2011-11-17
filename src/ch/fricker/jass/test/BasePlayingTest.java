package ch.fricker.jass.test;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import ch.frickler.jass.*;
import ch.frickler.jass.definitions.ISpielart;
import ch.frickler.jass.logic.*;
import ch.frickler.jass.logic.Card.CardFamily;
import ch.frickler.jass.logic.Card.CardValue;


public class BasePlayingTest {

	@Test
	public void EveryPlayerOneCard() throws Exception {
	
		Spiel p = GetVerteiltesSpiel(new Obenabe());
		Spielfeld f = p.getSpielfeld();
		
		for(Spieler spl : p.getAllSpieler()){
			Card c = spl.forcePlay(f);
			f.addCard(c);
		}
		
		for(Spieler spl : p.getAllSpieler()){
			Assert.assertEquals(8,spl.getCards().size());
		}
		
		Assert.assertEquals(4,f.getCards().size());

	}
	
	@Test
	public void cardsAfterPlayingOneRound() throws Exception {
	
		Spiel p = GetVerteiltesSpiel(new Obenabe());
		
		Spielfeld f = p.getSpielfeld();
		
		for(int i = 0;i < 9;i++){
			for(Spieler spl : p.getAllSpieler()){
				Card c = spl.forcePlay(f);
				f.addCard(c);
			}
			p.placeStich(f.getCards());
		}
		
		
		for(Spieler spl : p.getAllSpieler()){
			Assert.assertEquals(0,spl.getCards().size());
		}
		
		Assert.assertEquals(36,f.getCards().size());
		
	}

	@Test
	public void StichAfterOneRound1() throws Exception{
		Spiel p = GetSpiel(new Obenabe());
		Spielfeld f = p.getSpielfeld();
		
		List<Spieler> sp =	p.getAllSpieler();
		sp.get(0).addCard(new Card(CardValue.Neun,CardFamily.Herz));
		sp.get(1).addCard(new Card(CardValue.Sechs,CardFamily.Herz));
		sp.get(2).addCard(new Card(CardValue.Koenig,CardFamily.Herz));
		sp.get(3).addCard(new Card(CardValue.Ass,CardFamily.Herz));
		
		for(Spieler spl : p.getAllSpieler()){
			Card c = spl.forcePlay(f);
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
		Spielfeld f = p.getSpielfeld();
		
		List<Spieler> sp =	p.getAllSpieler();
		sp.get(0).addCard(new Card(CardValue.Sieben,CardFamily.Herz));
		sp.get(1).addCard(new Card(CardValue.Ass,CardFamily.Herz));
		sp.get(2).addCard(new Card(CardValue.Sechs,CardFamily.Herz));
		sp.get(3).addCard(new Card(CardValue.Bauer,CardFamily.Herz));
		
		for(Spieler spl : p.getAllSpieler()){
			Card c = spl.forcePlay(f);
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
		Runde round = new Runde();
		round.SetSpielart(spielart);
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
		KartenVerteilAction kva = new KartenVerteilAction();
		kva.doAction(p);
		return p;
	}
	
	@Test
	public void AusspielerTest() throws Exception{
		Spiel p = GetSpiel(new Obenabe());
		Spielfeld f = p.getSpielfeld();
		
		List<Spieler> sp =	p.getAllSpieler();
		sp.get(0).addCard(new Card(CardValue.Neun,CardFamily.Herz));
		sp.get(1).addCard(new Card(CardValue.Sechs,CardFamily.Herz));
		sp.get(2).addCard(new Card(CardValue.Koenig,CardFamily.Herz));
		sp.get(3).addCard(new Card(CardValue.Ass,CardFamily.Herz));

		sp.get(0).addCard(new Card(CardValue.Acht,CardFamily.Herz));
		sp.get(1).addCard(new Card(CardValue.Neun,CardFamily.Egge));
		sp.get(2).addCard(new Card(CardValue.Koenig,CardFamily.Egge));
		sp.get(3).addCard(new Card(CardValue.Sechs,CardFamily.Egge));
		
		for(Spieler spl : p.getAllSpielerSorted(null)){
			Card c = spl.forcePlay(f);
			f.addCard(c);
		}
		
		Spieler pSticher = p.placeStich(f.getCards());
		f.removeCards();
		Assert.assertEquals(sp.get(3), pSticher);
		
		for(Spieler spl : p.getAllSpielerSorted(pSticher)){
			Card c = spl.forcePlay(f);
			f.addCard(c);
		}
		
		Spieler pSticher2 = p.placeStich(f.getCards());
		f.removeCards();
		
		Assert.assertEquals(sp.get(2), pSticher2);
		
		
		
	}
}
