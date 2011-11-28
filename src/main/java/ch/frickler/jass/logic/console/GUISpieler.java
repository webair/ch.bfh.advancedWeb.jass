package ch.frickler.jass.logic.console;

import javax.swing.JOptionPane;

import ch.frickler.jass.logic.Card;
import ch.frickler.jass.logic.JUALayCard;
import ch.frickler.jass.logic.JUAQuit;
import ch.frickler.jass.logic.JUASchieben;
import ch.frickler.jass.logic.JUAStoeck;
import ch.frickler.jass.logic.JUAWies;
import ch.frickler.jass.logic.Obenabe;
import ch.frickler.jass.logic.Round;
import ch.frickler.jass.logic.Spieler;
import ch.frickler.jass.logic.Spielfeld;
import ch.frickler.jass.logic.Trumpf;
import ch.frickler.jass.logic.Ungeufe;
import ch.frickler.jass.logic.Card.CardFamily;
import ch.frickler.jass.logic.definitions.ISpielart;
import ch.frickler.jass.logic.definitions.ISpieler;
import ch.frickler.jass.logic.definitions.IUserAction;

public class GUISpieler  extends ISpieler {

	public GUISpieler(String pname) {
		String dialogname = JOptionPane.showInputDialog("Enter your Name:",pname);
		if(dialogname == null)
			pname = dialogname;
		this.name = pname;
	}

	public IUserAction forcePlay(Round spielfeld) throws Exception{
			int amountCards = this.getCards().size();
			System.out.println("laying card: "+spielfeld.getCards().toString());
			String[] options =  { "Wies" , "Stoeck", "Schieben" , "Quit"  };
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
				return new JUALayCard(this,this.getCards().remove(response));
			}else if(response == amountCards+1){
				
				return new JUAWies(this);
			}else if(response == amountCards+2){
				return new JUAStoeck(this);
			}else if(response == amountCards+3){
				return new JUASchieben(this);
			}else if(response == amountCards+4){
				return new JUAQuit(this);
			}
			
			throw new Exception("Unimplemented response"+response);

	}

	@Override
	public ISpielart sayTrumpf(boolean canSchieben) {
		String[] options =  { "Obeabe" , "Ungeufe", "Egge" , "Schaufel","Herz","Kreuz"  };
	
		int response = -1;
		do{
			response = JOptionPane.showOptionDialog(null // Center in
															// window.
				, "Select your Card to Play" // Message
				, "CardPlayingDialog" // Title in titlebar
				, JOptionPane.OK_OPTION // Option type
				, JOptionPane.PLAIN_MESSAGE // messageType
				, null // Icon (none)
				, options // Button text as above.
				, null // Default button's label
		);
		}while(response == -1);
		
		if(response == 0){
			return new Obenabe();
		}else if(response == 1){
			
			return new Ungeufe();
		}else if(response == 2){
			return new Trumpf(CardFamily.Egge);
		}else if(response == 3){
			return new  Trumpf(CardFamily.Schaufel);
		}else if(response == 4){
			return new Trumpf(CardFamily.Herz);
		}else if(response == 4){
			return new Trumpf(CardFamily.Kreuz);
		}
		//todo 
		return null;
		
	}
	
}
