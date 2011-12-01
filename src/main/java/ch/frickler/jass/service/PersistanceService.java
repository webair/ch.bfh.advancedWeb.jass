package ch.frickler.jass.service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public abstract class PersistanceService {

	private EntityManager em;

	public PersistanceService() {
		// Create EntityManagerFactory
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("MyBuzzwordJass");

		// Create new EntityManager
		em = emf.createEntityManager();
	}

	public EntityManager getEm() {
		return em;
	}

	/**
	 * Makes an object persistent and checks if its properly saved in the
	 * databonk
	 * 
	 * @param o
	 *            Entity to store in the databonk
	 * @return stored object
	 */
	protected <T> T mergeObject(T t) {

		getEm().getTransaction().begin();
		try {
			t = getEm().merge(t);
			getEm().getTransaction().commit();
		} catch (Exception e) {
			getEm().getTransaction().rollback();
		}

		return t;
	}

	/**
	 * Loads an object
	 * 
	 * @param o
	 *            Entity to store in the databonk
	 * @return stored object
	 */
	protected <T> T loadObject(Class<T> c, long primaryKeyValue) {

		return getEm().find(c, primaryKeyValue);

	}

}
