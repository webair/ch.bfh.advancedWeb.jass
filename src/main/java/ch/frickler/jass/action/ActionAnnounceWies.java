package ch.frickler.jass.action;

import java.util.List;

import ch.frickler.jass.db.entity.Card;
import ch.frickler.jass.db.entity.User;
import ch.frickler.jass.db.entity.Wies;
import ch.frickler.jass.definitions.JassAction;
import ch.frickler.jass.service.GameService;

public class ActionAnnounceWies extends JassAction {

	List<Card> cards;
	
	public ActionAnnounceWies(User user) {
		super(user);
	}
	
	public ActionAnnounceWies(User user,List<Card> cs) {
		super(user);
		this.cards = cs;
	}

	@Override
	public boolean doAction(GameService game) {
		if(isActionPossible(game)){
			game.getCurrentRound().addWies(new Wies(cards,user));
			return true;
		}
		return false;
	}

	@Override
	public boolean isActionPossible(GameService game) {
		if(user.getCards().size() == 9 && game.getCurrentRound() != null && game.getCurrentRound().getCurrentPlayer().equals(user)){
			if(new Wies(cards,user).isCorrect())
			return true;
		}
		return false;
	}
}
