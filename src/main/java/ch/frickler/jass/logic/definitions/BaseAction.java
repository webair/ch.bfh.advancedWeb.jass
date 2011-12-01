package ch.frickler.jass.logic.definitions;

import ch.frickler.jass.db.entity.Game;
import ch.frickler.jass.db.entity.User;
import ch.frickler.jass.service.GameService;

public abstract class BaseAction {
		
		protected User user;
		
		public BaseAction(User user){
			this.user = user;
		}
		
		public abstract boolean doAction(GameService spiel);

		public abstract boolean isActionPossible(GameService game);

	}

