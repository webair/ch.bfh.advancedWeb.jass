package ch.frickler.jass.service;

import ch.frickler.jass.db.entity.User;

public class UserService extends PersistanceService {

	public User createSpieler(String userName, String password, String name) {
		User u = new User(userName, password, name);
		u = mergeObject(u);
		return u;
	}

	public User loadSpieler(Long userId) {
		return loadObject(User.class, userId);
	}

}
