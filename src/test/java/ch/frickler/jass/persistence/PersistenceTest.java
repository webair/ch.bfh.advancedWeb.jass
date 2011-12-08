package ch.frickler.jass.persistence;

import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import ch.frickler.jass.db.entity.Card;
import ch.frickler.jass.db.entity.Game;
import ch.frickler.jass.db.entity.GameType;
import ch.frickler.jass.db.entity.Round;
import ch.frickler.jass.db.entity.RoundCard;
import ch.frickler.jass.db.entity.Team;
import ch.frickler.jass.db.entity.User;
import ch.frickler.jass.db.enums.CardFamily;
import ch.frickler.jass.db.enums.CardValue;
import ch.frickler.jass.db.enums.GameKind;



public class PersistenceTest extends AbstractEntityTest {
	
	@Test
	public void fricklerTest() {
		assertTrue("We are fricklers", true);
	}

	/**
	 * Assert that the EntityManager can open the connection
	 */
	@Test
	public void connectionTest() {
		assertTrue("Can setup connection", getEm().isOpen());
	}

	@Test
	public void cardTest() {
		Card c = new Card(CardFamily.HERZ, CardValue.BAUER);

		// Write card in Databonk
		c = mergeObject(c);

		// Check if card is in Databonk
		assertInDb(Card.class, c.getId());
	}

	@Test
	public void gameTypeTest() {
		GameType c = new GameType(GameKind.TOPDOWN, 3);

		// Write gametype in Databonk
		c = mergeObject(c);

		// Check if card is in Databonk
		assertInDb(GameType.class, c.getId());
	}

	@Test
	public void userTest() {
		User u = new User();
		u.setName("Fridu Frickler");
		u.setUserName("Fred");
		u.setPassword("superSecret");

		// Write card in Databonk
		u = mergeObject(u);

		// Check if card is in Databonk
		assertInDb(User.class, u.getId());
	}

	@Test
	public void teamTest() {
		User u1 = new User();
		u1.setName("Fridu Frickler");
		u1.setUserName("Fred");
		u1.setPassword("superSecret");

		User u2 = new User();
		u2.setName("Franz Frickler");
		u2.setUserName("Franz");
		u2.setPassword("superSecret");

		// Write card in Databonk
		u1 = mergeObject(u1);
		u2 = mergeObject(u2);

		// Check if users are in Databonk
		assertInDb(User.class, u1.getId());
		assertInDb(User.class, u2.getId());

		Team t = new Team(u1, u2);
		t.setPoints(1500);
		t = mergeObject(t);
		assertInDb(Team.class, t.getId());

	}

	/**
	 * Create a complete game
	 */
	@Test
	public void gameTest() {
		// Create Team 1
		User u1 = mergeObject(new User("fred", "Fred Frickler", "pw1"));
		User u2 = mergeObject(new User("franz", "Franz Frickler", "pw2"));

		assertInDb(User.class, u1.getId());
		assertInDb(User.class, u2.getId());

		Team t1 = new Team(u1, u2);
		t1.setPoints(1500);
		t1 = mergeObject(t1);

		// Create Team 2
		User u3 = mergeObject(new User("florian", "Florian Frickler", "pw3"));
		User u4 = mergeObject(new User("fabian", "Fabian Frickler", "pw4"));

		assertInDb(User.class, u3.getId());
		assertInDb(User.class, u4.getId());

		Team t2 = new Team(u3, u4);
		t2.setPoints(1500);
		t2 = mergeObject(t2);

		// Create game
		Game g = new Game("Nömber ön",u1, t1, t2, 0);
		g.setStartDate(new Date());
		g.setWinPoints(2500);
		g = mergeObject(g);

		// Create round
		//GameType gt = mergeObject(new GameType(GameKind.TOPDOWN, 12));
		Round r = mergeObject(new Round(g, GameKind.TOPDOWN));

		// Create card set
		Set<Card> cards = new HashSet<Card>();
		cards.add(new Card(CardFamily.HERZ, CardValue.SECHS));
		cards.add(new Card(CardFamily.HERZ, CardValue.SIEBEN));
		cards.add(new Card(CardFamily.HERZ, CardValue.ACHT));
		cards.add(new Card(CardFamily.HERZ, CardValue.NEUN));
		cards.add(new Card(CardFamily.HERZ, CardValue.ZEHN));
		cards.add(new Card(CardFamily.HERZ, CardValue.BAUER));
		cards.add(new Card(CardFamily.HERZ, CardValue.DAME));
		cards.add(new Card(CardFamily.HERZ, CardValue.KOENIG));
		cards.add(new Card(CardFamily.HERZ, CardValue.ASS));

		// Cards for user 1
		RoundCard rc = new RoundCard(u1, r, cards);
		rc = mergeObject(rc);

	}
}
