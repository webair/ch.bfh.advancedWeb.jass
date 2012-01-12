package ch.frickler.jass.service;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.persistence.Query;

import ch.frickler.jass.db.entity.User;
import ch.frickler.jass.helper.Translator;

/**
 * Class to connect the user entity with the user session
 * @author seed
 *
 */
public class UserService extends PersistanceService {

	/**
	 * creates a hash of the given password
	 * 
	 * @param the
	 *            password
	 * @return a sha256 hex string
	 */
	public static String cryptPw(String pw) {
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

	/**
	 * method to create a new player
	 * 
	 * @param userName
	 * @param password
	 * @param name
	 * @return User instance
	 */
	public User createPlayer(String userName, String password, String name) {
		User u = new User(userName, password, name);
		u = mergeObject(u);
		System.out.println("player created sucessful");
		return u;
	}

	/**
	 * method to load User from the database
	 * @param userId
	 * @return User instance
	 */
	public User loadUser(Long userId) {
		return loadObject(User.class, userId);
	}

	/**
	 * Checks if the username is already in the databonk
	 * 
	 * @param username
	 * @return true if user name is not in use yet
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
	 * @return Returns the user otherwise null
	 */
	@SuppressWarnings("unchecked")
	public User checkUserNameAndPassword(String username, String password) {

		Query q = getEm().createQuery(
				"from User where userName=?1 and password=?2", User.class);
		q.setParameter(1, username).setParameter(2, cryptPw(password));

		List<User> users = q.getResultList();
		if (users.size() == 1) {

			return users.get(0);
		} else {
			return null;
		}
	}

	/**
	 * method to create a bot
	 * @return User instance
	 */
	public User createBot() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		String cName = Translator.getString(ctx, "computername");
		User u = new User(cName);
		u = mergeObject(u);
		return u;
	}
}
