package ch.frickler.jass.logic;

import ch.frickler.jass.entity.User;
import ch.frickler.jass.logic.definitions.ISpieler;
import ch.frickler.jass.logic.definitions.IUserAction;

public class JUAQuit extends IUserAction {

	public JUAQuit(ISpieler user) {
		super(user);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean doAction(Spiel spiel) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isActionPossible(Spiel game) {
		// TODO Auto-generated method stub
		return false;
	}

}
