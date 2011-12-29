package ch.frickler.jass.definitions;

import ch.frickler.jass.db.entity.User;
import ch.frickler.jass.service.GameService;

/**
 * this is the abstract class for the action a user can do during the game
 * @author kaeserst
 *
 */
public abstract class JassAction {
		
		protected User user;
		
		/**
		 * Constructor. we need the users witch want to do the action.
		 * @param user
		 */
		public JassAction(User user){
			this.user = user;
		}
		
		/**
		 * 
		 * @param the game for witch the action is.
		 * @return
		 */
		public abstract boolean doAction(GameService game);

		/**
		 * 
		 * @param check if the action  the user wants to proceed is possible or not.
		 * @return
		 */
		public abstract boolean isActionPossible(GameService game);

	}

