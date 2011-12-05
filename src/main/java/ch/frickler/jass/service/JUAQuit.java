package ch.frickler.jass.service;

import ch.frickler.jass.db.entity.User;
import ch.frickler.jass.logic.definitions.BaseAction;


public class JUAQuit extends BaseAction {

	public JUAQuit(User user) {
		super(user);
	}

	@Override
	public boolean doAction(GameService spiel) {
		spiel.terminate(user);
		return true;
	}

	@Override
	public boolean isActionPossible(GameService game) {
		return true;
	}

}
