package ch.frickler.jass.action;

import ch.frickler.jass.db.entity.User;
import ch.frickler.jass.definitions.JassAction;
import ch.frickler.jass.service.GameService;

/**
 * This action is planed for anouncing the Stoek (not implemented yet)
 * @author kaeserst
 *
 */
public class ActionAnnounceStoeck extends JassAction {

	public ActionAnnounceStoeck(User user) {
		super(user);
	}

	public boolean doAction(GameService spiel) {		
		return spiel.addStoeck(user);
	}

	@Override
	public boolean isActionPossible(GameService game) {
		return game.hasUserStoeck(user);
	}

}
