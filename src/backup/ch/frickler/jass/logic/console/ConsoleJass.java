package ch.frickler.jass.logic.console;


import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.hibernate.mapping.List;

import ch.frickler.jass.db.entity.Card;
import ch.frickler.jass.db.entity.GameType;
import ch.frickler.jass.db.entity.User;
import ch.frickler.jass.db.enums.GameKind;
import ch.frickler.jass.logic.definitions.BaseAction;
import ch.frickler.jass.service.GameService;
import ch.frickler.jass.service.JUAAnsagen;
import ch.frickler.jass.service.JUABoardCastCard;
import ch.frickler.jass.service.JUALayCard;
import ch.frickler.jass.service.JUAQuit;
import ch.frickler.jass.service.JUAStoeck;
import ch.frickler.jass.service.JUAWies;
import ch.frickler.jass.service.Trumpf;
import ch.frickler.jass.service.UserService;

public class ConsoleJass {

	static public void main(String[] argv) {
		GameKind spielart = GameKind.TRUMPEdge;
	GameService game = new GameService();
		game.createGame("mygame",1000);

		UserService service = new UserService();
		User p1 = service.createSpieler("Tom Chiller");
		User p2 = service.createSpieler("Krugi");
		User p3 = service.createSpieler("Aschax","d","d",false);
		User p4 =service.createSpieler("Bloeffer aka Webair");

		ArrayList<User> users = new ArrayList<User>();
		
		users.add(p1);
		users.add(p2);
		users.add(p3);
		users.add(p4);
		
		try {
			game.addSpieler(p1);
			game.addSpieler(p2);
			game.addSpieler(p3);
			game.addSpieler(p4);

		
			game.doAction(new JUABoardCastCard(p1));
			
			while(p1.getCards().size() > 0){	
				for(User u : users){
					BaseAction ba;				
					int i = 0;
					boolean vaild = false;
					do{
						if(u.isRobot()){
							ba = showGUI(u);
						}else{						
						 Card c = u.getCards().get(i);
						 ba = new JUALayCard(u,c);
						}
						vaild =  game.doAction(ba);
						i++;
					}
					while(!vaild);
					}
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static BaseAction showGUI(User u) {
		int amountCards = u.getCards().size();
		//System.out.println("laying card: "+spielfeld.getCards().toString());
		String[] options =  { "Wies" , "Stoeck", "Schieben" , "Quit"  };
		String[] cards = new String[amountCards+options.length];
		int i = 0;
		for(Card c : u.getCards()){
			cards[i++] = c.toString();
		}
		for(String o : options){
			cards[i++] = o;
		}
		
		int response = -1;
		do{
			response = JOptionPane.showOptionDialog(null // Center in
															// window.
				, "Select your Card to Play" // Message
				, "CardPlayingDialog" // Title in titlebar
				, JOptionPane.OK_OPTION // Option type
				, JOptionPane.PLAIN_MESSAGE // messageType
				, null // Icon (none)
				, cards // Button text as above.
				, null // Default button's label
		);
		}while(response == -1);
			
		
		if(response < amountCards){
			System.out.println("Manually selected card: (id:"+response+") "+u.getCards().get(response));
			return new JUALayCard(u,u.getCards().remove(response));
		}else if(response == amountCards+1){
			
			return new JUAWies(u);
		}else if(response == amountCards+2){
			return new JUAStoeck(u);
		}else if(response == amountCards+3){
			return new JUAAnsagen(u);
		}else if(response == amountCards+4){
			return new JUAQuit(u);
		}
		
		throw new RuntimeException("Unimplemented response"+response);

	}
}
