package ch.frickler.jass.action;

import ch.frickler.jass.db.entity.Card;
import ch.frickler.jass.db.entity.User;
import ch.frickler.jass.logic.definitions.JassAction;
import ch.frickler.jass.service.GameService;

public class ActionLayCard extends JassAction {

	public ActionLayCard(User user) {
		super(user);
	}

	private Card card;

	public ActionLayCard(User u, Card c) {
		super(u);
		this.card = c;
	}

	public Card getCard() {
		return card;
	}

	@Override
	public boolean doAction(GameService game) {
		if (isActionPossible(game)) {
			game.playCard(user, card);
			return true;
		}
		return false;
	}

	@Override
	public boolean isActionPossible(GameService game) {
		return game.canPlayCard(card, user);
	}

}
