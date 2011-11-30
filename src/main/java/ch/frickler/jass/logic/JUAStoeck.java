package ch.frickler.jass.logic;

import ch.frickler.jass.db.entity.User;
import ch.frickler.jass.logic.definitions.ISpieler;
import ch.frickler.jass.logic.definitions.IUserAction;

public class JUAStoeck extends IUserAction {

	public JUAStoeck(ISpieler user) {
		super(user);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean doAction(Spiel spiel) {		
		return spiel.addStoeck(user);
	}

	@Override
	public boolean isActionPossible(Spiel game) {
		return game.getRound().hasUserStoeck(user);
	}

}
