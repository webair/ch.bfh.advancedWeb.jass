package ch.frickler.jass.logic;

import ch.frickler.jass.db.entity.User;
import ch.frickler.jass.logic.Spiel.GameState;
import ch.frickler.jass.logic.definitions.ISpielart;
import ch.frickler.jass.logic.definitions.ISpieler;
import ch.frickler.jass.logic.definitions.IUserAction;

public class JUAAnsagen extends IUserAction {
	
	private ISpielart spielart;
	
	public JUAAnsagen(ISpieler user) {
		super(user);
	}
	
	public JUAAnsagen(ISpieler user,ISpielart art) {
		super(user);
		this.spielart = art;		
	}

	@Override
	public boolean doAction(Spiel spiel) {
		spiel.getRound().setSpielart(spielart);
		return false;
	}

	@Override
	public boolean isActionPossible(Spiel game) {
		
		if(spielart == null || user == null){
			return false;
		}		
		if (game.getState() == GameState.Ansage
				&& game.getRound().getAusspieler().equals(user)) {
			return true;
		} else if (game.getState() == GameState.AnsageGschobe
				&& game.getAnsager().equals(user)) {
			return true;
		}
		return false;
	}

}
