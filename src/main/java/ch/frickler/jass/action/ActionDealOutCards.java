package ch.frickler.jass.action;


import ch.frickler.jass.db.entity.User;
import ch.frickler.jass.db.enums.GameState;
import ch.frickler.jass.definitions.JassAction;
import ch.frickler.jass.service.GameService;


/**
 * This action handles the dealing out of the cards.
 * @author kaeserst
 *
 */
public class ActionDealOutCards extends JassAction {

	public ActionDealOutCards() {
		super(null);
	}

	@Override
	public boolean doAction(GameService spiel) {

		spiel.arrangeCards();
		return true;
	}

	@Override
	public boolean isActionPossible(GameService game) {
		return (game.getState() == GameState.WaitForCards);
	}
	

}
