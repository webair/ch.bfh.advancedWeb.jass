package ch.frickler.jass.service;


import ch.frickler.jass.db.entity.Game.GameState;
import ch.frickler.jass.db.entity.User;
import ch.frickler.jass.logic.definitions.BaseAction;



public class JUABoardCastCard extends BaseAction {

	public JUABoardCastCard() {
		super(null);
	}

	@Override
	public boolean doAction(GameService spiel) {

		spiel.kartenVerteilen();
		return true;
	}

	@Override
	public boolean isActionPossible(GameService game) {
		// TODO Auto-generated method stub
		return (game.getState() == GameState.WaitForCards);
	}
	

}
