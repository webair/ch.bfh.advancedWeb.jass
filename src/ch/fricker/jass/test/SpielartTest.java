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

import org.junit.Test;


public class SpielartTest {

	
	@Test
	public void Trumpf_isSecondCardHigher(){
		
	
		ISpielart sart = new Trumpf(Card.CardFamily.Herz);
		
		Card trumpf8 = new Card(CardValue.Acht,CardFamily.Herz);
		Card trumpfass = new Card(CardValue.Ass,CardFamily.Herz);
		Card trumpfbauer = new Card(CardValue.Bauer,CardFamily.Herz);
		Card trumpfnaeuu = new Card(CardValue.Neun,CardFamily.Herz);
		
		Card egge6 = new Card(CardValue.Sechs,CardFamily.Egge);
		Card egge9 = new Card(CardValue.Neun,CardFamily.Egge);
		
		Card schaufelbauer = new Card(CardValue.Bauer,CardFamily.Schaufel);
		Card schaufelacht = new Card(CardValue.Acht,CardFamily.Schaufel);
		Card schaufeldame = new Card(CardValue.Dame,CardFamily.Schaufel);
		
		// trumpf only
		Assert.assertEquals(false,sart.isSecondCardHigher(trumpfass,trumpf8));
		Assert.assertEquals(true,sart.isSecondCardHigher(trumpf8, trumpfass));
		
		Assert.assertEquals(false,sart.isSecondCardHigher(trumpfbauer, trumpfnaeuu));
		Assert.assertEquals(false,sart.isSecondCardHigher(trumpfnaeuu, trumpfass));
		Assert.assertEquals(true,sart.isSecondCardHigher(trumpf8,trumpfbauer));
		Assert.assertEquals(false,sart.isSecondCardHigher(trumpfbauer, trumpfass));
		
		// kein trumpf
		Assert.assertEquals(false,sart.isSecondCardHigher(egge9, schaufelbauer));
		Assert.assertEquals(false,sart.isSecondCardHigher(egge9, egge6));
		Assert.assertEquals(true,sart.isSecondCardHigher(egge6, egge9));
		Assert.assertEquals(false,sart.isSecondCardHigher(schaufeldame, schaufelbauer));
		Assert.assertEquals(false,sart.isSecondCardHigher(schaufelbauer, egge9));
		Assert.assertEquals(true,sart.isSecondCardHigher(schaufelacht, schaufelbauer));
		
		//ein trumpf
		Assert.assertEquals(false,sart.isSecondCardHigher(trumpf8, egge9));
		Assert.assertEquals(true,sart.isSecondCardHigher(egge9, trumpf8));
		Assert.assertEquals(true,sart.isSecondCardHigher(schaufelacht, trumpfass));
		Assert.assertEquals(false,sart.isSecondCardHigher(trumpfass, schaufelbauer));
		Assert.assertEquals(true,sart.isSecondCardHigher(egge9, trumpfbauer));
		Assert.assertEquals(false,sart.isSecondCardHigher(trumpfnaeuu, schaufelacht));
	
		
	}
	
	@Test
	public void Obenabe_isSecondCardHigher(){	
	
		ISpielart sart = new Obenabe();
		
		Card herz8 = new Card(CardValue.Acht,CardFamily.Herz);
		Card herzass = new Card(CardValue.Ass,CardFamily.Herz);
		Card herzbauer = new Card(CardValue.Bauer,CardFamily.Herz);
		Card herz9 = new Card(CardValue.Neun,CardFamily.Herz);
		
		Card egge6 = new Card(CardValue.Sechs,CardFamily.Egge);
		Card egge9 = new Card(CardValue.Neun,CardFamily.Egge);
		
		Card schaufelbauer = new Card(CardValue.Bauer,CardFamily.Schaufel);
		Card schaufelacht = new Card(CardValue.Acht,CardFamily.Schaufel);
		Card schaufeldame = new Card(CardValue.Dame,CardFamily.Schaufel);
		
	
		Assert.assertEquals(false,sart.isSecondCardHigher(herzass,herz8));
		Assert.assertEquals(true,sart.isSecondCardHigher(herz8, herzass));
		
		Assert.assertEquals(false,sart.isSecondCardHigher(herzbauer, herz9));
		Assert.assertEquals(true,sart.isSecondCardHigher(herz9, herzass));
		Assert.assertEquals(true,sart.isSecondCardHigher(herz8,herzbauer));
		Assert.assertEquals(true,sart.isSecondCardHigher(herzbauer, herzass));
		

		Assert.assertEquals(false,sart.isSecondCardHigher(egge9, schaufelbauer));
		Assert.assertEquals(false,sart.isSecondCardHigher(egge9, egge6));
		Assert.assertEquals(true,sart.isSecondCardHigher(egge6, egge9));
		Assert.assertEquals(false,sart.isSecondCardHigher(schaufeldame, schaufelbauer));
		Assert.assertEquals(false,sart.isSecondCardHigher(schaufelbauer, egge9));
		Assert.assertEquals(true,sart.isSecondCardHigher(schaufelacht, schaufelbauer));
		

		Assert.assertEquals(false,sart.isSecondCardHigher(herz8, egge9));
		Assert.assertEquals(false,sart.isSecondCardHigher(egge9, herz8));
		Assert.assertEquals(false,sart.isSecondCardHigher(schaufelacht, herzass));
		Assert.assertEquals(false,sart.isSecondCardHigher(herzass, schaufelbauer));
		Assert.assertEquals(false,sart.isSecondCardHigher(egge9, herzbauer));
		Assert.assertEquals(false,sart.isSecondCardHigher(herz9, schaufelacht));
	
		
	}

	
	@Test
	public void Ungenufe_isSecondCardHigher(){
		
	
		ISpielart sart = new Ungeufe();
		
		Card herz8 = new Card(CardValue.Acht,CardFamily.Herz);
		Card herzass = new Card(CardValue.Ass,CardFamily.Herz);
		Card herzbauer = new Card(CardValue.Bauer,CardFamily.Herz);
		Card herz9 = new Card(CardValue.Neun,CardFamily.Herz);
		
		Card egge6 = new Card(CardValue.Sechs,CardFamily.Egge);
		Card egge9 = new Card(CardValue.Neun,CardFamily.Egge);
		
		Card schaufelbauer = new Card(CardValue.Bauer,CardFamily.Schaufel);
		Card schaufelacht = new Card(CardValue.Acht,CardFamily.Schaufel);
		Card schaufeldame = new Card(CardValue.Dame,CardFamily.Schaufel);
		
	
		Assert.assertEquals(true,sart.isSecondCardHigher(herzass,herz8));
		Assert.assertEquals(false,sart.isSecondCardHigher(herz8, herzass));
		
		Assert.assertEquals(true,sart.isSecondCardHigher(herzbauer, herz9));
		Assert.assertEquals(false,sart.isSecondCardHigher(herz9, herzass));
		Assert.assertEquals(false,sart.isSecondCardHigher(herz8,herzbauer));
		Assert.assertEquals(false,sart.isSecondCardHigher(herzbauer, herzass));
		

		Assert.assertEquals(false,sart.isSecondCardHigher(egge9, schaufelbauer));
		Assert.assertEquals(true,sart.isSecondCardHigher(egge9, egge6));
		Assert.assertEquals(false,sart.isSecondCardHigher(egge6, egge9));
		Assert.assertEquals(true,sart.isSecondCardHigher(schaufeldame, schaufelbauer));
		Assert.assertEquals(false,sart.isSecondCardHigher(schaufelbauer, egge9));
		Assert.assertEquals(false,sart.isSecondCardHigher(schaufelacht, schaufelbauer));
		

		Assert.assertEquals(false,sart.isSecondCardHigher(herz8, egge9));
		Assert.assertEquals(false,sart.isSecondCardHigher(egge9, herz8));
		Assert.assertEquals(false,sart.isSecondCardHigher(schaufelacht, herzass));
		Assert.assertEquals(false,sart.isSecondCardHigher(herzass, schaufelbauer));
		Assert.assertEquals(false,sart.isSecondCardHigher(egge9, herzbauer));
		Assert.assertEquals(false,sart.isSecondCardHigher(herz9, schaufelacht));
	
		
	}

}
