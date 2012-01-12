package ch.frickler.jass.action;

import ch.frickler.jass.db.entity.Game;
import ch.frickler.jass.db.entity.User;
import ch.frickler.jass.db.enums.GameKind;
import ch.frickler.jass.definitions.JassAction;
import ch.frickler.jass.service.GameService;

/**
 * This action is used for anouncing the next trump
 * @author kaeserst
 *
 */
public class ActionAnnounce extends JassAction {

	private GameKind type;

	public ActionAnnounce(User user) {
		super(user);
	}

	public ActionAnnounce(User user, GameKind type) {
		super(user);
		this.type = type;
	}

	@Override
	public boolean doAction(GameService gs) {
		if (isActionPossible(gs)) {
			gs.setTrump(type, user);
			
			return true;
		}
		return false;
	}

	@Override
	public boolean isActionPossible(GameService game) {

		if (game == null || user == null || type == null) {
			return false;
		}
		return game.isValidAnncouncer(user);

	}

}
