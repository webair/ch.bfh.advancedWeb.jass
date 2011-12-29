package ch.frickler.jass.definitions;

import ch.frickler.jass.db.entity.User;
import ch.frickler.jass.service.GameService;

public abstract class JassAction {
		
		protected User user;
		
		public JassAction(User user){
			this.user = user;
		}
		
		public abstract boolean doAction(GameService spiel);

		public abstract boolean isActionPossible(GameService game);

	}

