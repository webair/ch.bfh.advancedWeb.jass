package ch.frickler.jass.service;

import ch.frickler.jass.db.entity.User;
import ch.frickler.jass.logic.definitions.BaseAction;

public class JUAStoeck extends BaseAction {

	public JUAStoeck(User user) {
		super(user);
		// TODO Auto-generated constructor stub
	}

	public boolean doAction(GameService spiel) {		
		return spiel.addStoeck(user);
	}

	@Override
	public boolean isActionPossible(GameService game) {
		return game.hasUserStoeck(user);
	}

}
