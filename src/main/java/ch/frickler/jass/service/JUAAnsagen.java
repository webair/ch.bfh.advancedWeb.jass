package ch.frickler.jass.service;

import ch.frickler.jass.db.entity.Game;
import ch.frickler.jass.db.entity.User;
import ch.frickler.jass.db.enums.GameKind;
import ch.frickler.jass.logic.definitions.BaseAction;

public class JUAAnsagen extends BaseAction {

	private GameKind type;

	public JUAAnsagen(User user) {
		super(user);
	}

	public JUAAnsagen(User user, GameKind type) {
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
		return game.isValidAnsager(user);

	}

}
