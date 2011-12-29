package ch.frickler.jass.action;

import ch.frickler.jass.db.entity.User;
import ch.frickler.jass.logic.definitions.JassAction;
import ch.frickler.jass.service.GameService;

public class ActionAnnounceWies extends JassAction {

	public ActionAnnounceWies(User user) {
		super(user);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean doAction(GameService spiel) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isActionPossible(GameService game) {
		// TODO Auto-generated method stub
		return false;
	}

}
