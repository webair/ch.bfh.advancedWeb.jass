package ch.frickler.jass.logic;

import ch.frickler.jass.db.entity.User;
import ch.frickler.jass.logic.definitions.ISpieler;
import ch.frickler.jass.logic.definitions.IUserAction;

public class JUAQuit extends IUserAction {

	public JUAQuit(ISpieler user) {
		super(user);
	}

	@Override
	public boolean doAction(Spiel spiel) {
		spiel.terminate(user);
		return true;
	}

	@Override
	public boolean isActionPossible(Spiel game) {
		return true;
	}

}
