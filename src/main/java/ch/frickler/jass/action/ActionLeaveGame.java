package ch.frickler.jass.action;

import ch.frickler.jass.db.entity.User;
import ch.frickler.jass.definitions.JassAction;
import ch.frickler.jass.service.GameService;


public class ActionLeaveGame extends JassAction {

	public ActionLeaveGame(User user) {
		super(user);
	}

	@Override
	public boolean doAction(GameService spiel) {
		spiel.cancelGame(user);
		return true;
	}

	@Override
	public boolean isActionPossible(GameService game) {
		return true;
	}

}
