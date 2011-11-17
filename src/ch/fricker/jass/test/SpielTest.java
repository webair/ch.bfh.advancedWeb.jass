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



public class SpielTest {

	

	@Test
	public void getAllSpielerSortedTest() throws Exception {
	
		Spiel p = new Spiel();
		Spieler t1p2 = new Spieler("T1 P2");
		Spieler t2p2 = new Spieler("T2 P2");
		p.addSpieler(new Spieler("T1 P1"));
		p.addSpieler(new Spieler("T2 P1"));
		p.addSpieler(t1p2);
		p.addSpieler(t2p2);
		for(Team t : p.getTeams()){
			System.out.println(t.toString());
		}
		
		Assert.assertEquals("T1 P1",p.getAllSpielerSorted(null).get(0).getName());
		Assert.assertEquals("T2 P1",p.getAllSpielerSorted(null).get(1).getName());
		Assert.assertEquals("T1 P2",p.getAllSpielerSorted(null).get(2).getName());
		Assert.assertEquals("T2 P2",p.getAllSpielerSorted(null).get(3).getName());
		
		
		Assert.assertEquals("T1 P2",p.getAllSpielerSorted(t1p2).get(0).getName());
		Assert.assertEquals("T2 P2",p.getAllSpielerSorted(t1p2).get(1).getName());
		Assert.assertEquals("T1 P1",p.getAllSpielerSorted(t1p2).get(2).getName());
		Assert.assertEquals("T2 P1",p.getAllSpielerSorted(t1p2).get(3).getName());
		
		Assert.assertEquals("T2 P2",p.getAllSpielerSorted(t2p2).get(0).getName());
		Assert.assertEquals("T1 P1",p.getAllSpielerSorted(t2p2).get(1).getName());
		Assert.assertEquals("T2 P1",p.getAllSpielerSorted(t2p2).get(2).getName());
		Assert.assertEquals("T1 P2",p.getAllSpielerSorted(t2p2).get(3).getName());
	}

}
