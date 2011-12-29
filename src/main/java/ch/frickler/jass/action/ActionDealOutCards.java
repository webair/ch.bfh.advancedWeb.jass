package ch.frickler.jass.action;


import ch.frickler.jass.db.entity.Game.GameState;
import ch.frickler.jass.db.entity.User;
import ch.frickler.jass.logic.definitions.JassAction;
import ch.frickler.jass.service.GameService;



public class ActionDealOutCards extends JassAction {

	public ActionDealOutCards() {
		super(null);
	}

	@Override
	public boolean doAction(GameService spiel) {

		spiel.kartenVerteilen();
		return true;
	}

	@Override
	public boolean isActionPossible(GameService game) {
		return (game.getState() == GameState.WaitForCards);
	}
	

}
