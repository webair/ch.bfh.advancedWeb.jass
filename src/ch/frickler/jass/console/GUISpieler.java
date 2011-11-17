package ch.frickler.jass.console;

import javax.swing.JOptionPane;

import ch.frickler.jass.definitions.IUserAction;
import ch.frickler.jass.logic.Card;
import ch.frickler.jass.logic.JUALayCard;
import ch.frickler.jass.logic.JUAQuit;
import ch.frickler.jass.logic.JUASchieben;
import ch.frickler.jass.logic.JUAStoeck;
import ch.frickler.jass.logic.JUAWies;
import ch.frickler.jass.logic.Round;
import ch.frickler.jass.logic.Spieler;
import ch.frickler.jass.logic.Spielfeld;

public class GUISpieler extends Spieler {

	public GUISpieler(String pname) {
		String dialogname = JOptionPane.showInputDialog("Enter your Name:",pname);
		if(dialogname == null)
			pname = dialogname;
		this.name = pname;
	}

	public IUserAction forcePlay(Round spielfeld) throws Exception{
			int amountCards = this.getCards().size();
			System.out.println("laying card: "+spielfeld.getCards().toString());
			String[] options =  { "Wies" , "Stöck", "Schieben" , "Quit"  };
			String[] cards = new String[amountCards+options.length];
			int i = 0;
			for(Card c : this.getCards()){
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
				System.out.println("Manually selected card: (id:"+response+") "+this.getCards().get(response));
				return new JUALayCard(this.getCards().remove(response));
			}else if(response == amountCards+1){
				
				return new JUAWies();
			}else if(response == amountCards+2){
				
				return new JUAStoeck();
			}else if(response == amountCards+3){
				return new JUASchieben();
			}else if(response == amountCards+4){
				return new JUAQuit();
			}
			
			throw new Exception("Unimplemented response"+response);

	}
	
}
