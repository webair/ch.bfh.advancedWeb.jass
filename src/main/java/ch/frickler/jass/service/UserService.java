package ch.frickler.jass.service;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.persistence.Query;

import ch.frickler.jass.db.entity.User;

public class UserService extends PersistanceService {

	/**
	 * creates a hash of the given password
	 * 
	 * @param the
	 *            password
	 * @return a sha256 hex string
	 */
	private String cryptPw(String pw) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(pw.getBytes("UTF-8"));
			byte[] digest = md.digest();
			BigInteger bigInt = new BigInteger(1, digest);
			return bigInt.toString(16);
		} catch (NoSuchAlgorithmException e) {
			// wont happen
		} catch (UnsupportedEncodingException e) {
			// wont happen either
		}
		return null;
	}

	public User createSpieler(String userName, String password, String name,
			boolean b) {
		User u = new User(userName, password, name, b);
		u = mergeObject(u);
		return u;
	}

	public User loadUser(Long userId) {
		return loadObject(User.class, userId);
	}

	/**
	 * Checks if the username is already in the databonk
	 * 
	 * @param username
	 * @return
	 */
	public boolean isUsernameUnused(String username) {

		Query q = getEm().createQuery(
				"select count(*) from User where userName=?1");
		q.setParameter(1, username);

		Long num = (Long) q.getResultList().get(0);

		return (num == 0);
	}

	/**
	 * 
	 * @param username
	 * @param password
	 *            Uncrypted password
	 * @return Returns id if user is in databaes, otherweise null
	 */
	@SuppressWarnings("unchecked")
	public Long checkUserNameAndPassword(String username, String password) {

		Query q = getEm().createQuery(
				"from User where userName=?1 and password=?2", User.class);
		q.setParameter(1, username).setParameter(2, cryptPw(password));

		List<User> users = q.getResultList();
		if (users.size() == 1) {

			return users.get(0).getId();
		} else {
			return null;
		}
	}

	public User createSpieler(String string) {
		return createSpieler(string, string, string, true);
	}
}
