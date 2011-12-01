package ch.frickler.jass.logic.definitions;

import ch.frickler.jass.db.entity.User;
import ch.frickler.jass.logic.Spiel;

public abstract class IUserAction {
	
	protected ISpieler user;
	
	public IUserAction(ISpieler user){
		this.user = user;
	}
	
	public abstract boolean doAction(Spiel spiel);

	public abstract boolean isActionPossible(Spiel game);

}
