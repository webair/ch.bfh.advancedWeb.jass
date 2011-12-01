package ch.frickler.jass.persistence;

import static org.junit.Assert.fail;

public abstract class AbstractEntityTest extends PersistenceBasedTest {

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
			fail(e.getMessage());
		}

		return t;
	}

	/**
	 * 
	 * @param c
	 *            Class to find
	 * @param primaryKeyValue
	 */
	protected void assertInDb(Class<?> c, long primaryKeyValue) {
		if (getEm().find(c, primaryKeyValue) == null) {
			fail("Object " + c.getName() + " with id " + primaryKeyValue
					+ " is not in the Databonk");
		}
	}
	
	protected <T> T assertInDb(Class<?> c, long primaryKeyValue) {
		return (T) getEm().find(c, primaryKeyValue);
		
	}
	
}
