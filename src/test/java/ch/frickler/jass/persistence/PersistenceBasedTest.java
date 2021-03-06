package ch.frickler.jass.persistence;



import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
 

public abstract class PersistenceBasedTest {

	private EntityManager em;

	@Before
	public void setUp() throws Exception {
		// Create EntityManagerFactory
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("MyBuzzwordJass");

		// Create new EntityManager
		em = emf.createEntityManager();
	}

	@After
	public void tearDown() throws Exception {
		em.close();
	}

	public EntityManager getEm() {
		return em;
	}

}
