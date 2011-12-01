package ch.frickler.jass.service;

import ch.frickler.jass.db.entity.Game;
import ch.frickler.jass.db.entity.Game.GameState;
import ch.frickler.jass.db.entity.GameType;
import ch.frickler.jass.db.entity.User;
import ch.frickler.jass.logic.definitions.BaseAction;
import ch.frickler.jass.logic.definitions.ISpielart;


public class JUAAnsagen extends BaseAction {
	
	private GameType type;
	
	public JUAAnsagen(User user) {
		super(user);
	}
	
	public JUAAnsagen(User user,GameType type) {
		super(user);
		this.type = type;		
	}

	@Override
	public boolean doAction(GameService gs) {
		gs.setGameType(type);
		gs.setGameState(Game.GameState.Play);
		return true;
	}

	@Override
	public boolean isActionPossible(GameService game) {
		
		if(game == null || user == null){
			return false;
		}		
		return game.isValidAnsager(user);

	}

}
