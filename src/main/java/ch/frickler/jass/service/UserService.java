package ch.frickler.jass.service;

import ch.frickler.jass.db.entity.User;

public class UserService extends PersistanceService {

	public User createSpieler(String userName, String password, String name, boolean b) {
		User u = new User(userName, password, name,b);
		u = mergeObject(u);
		return u;
	}

	public User loadSpieler(Long userId) {
		return loadObject(User.class, userId);
	}

	public User createSpieler(String name) {
		return createSpieler(name,name,name,true);
	}

}
