package ch.frickler.jass.logic;

import javax.swing.JOptionPane;

public class GUISpieler extends Spieler {

	public GUISpieler(String pname) {
		String dialogname = JOptionPane.showInputDialog("Enter your Name:",pname);
		if(dialogname == null)
			pname = dialogname;
		this.name = pname;
	}

	public Card forcePlay(Spielfeld spielfeld){
			System.out.println("laying card: "+spielfeld.getCards().toString());
			String[] choices = new String[this.getCards().size()];
			int i = 0;
			for(Card c : this.getCards()){
				choices[i++] = c.toString();
			}

			int response = JOptionPane.showOptionDialog(null // Center in
																// window.
					, "Select your Card to Play" // Message
					, "CardPlayingDialog" // Title in titlebar
					, JOptionPane.OK_OPTION // Option type
					, JOptionPane.PLAIN_MESSAGE // messageType
					, null // Icon (none)
					, choices // Button text as above.
					, null // Default button's label
			);
			
			if(response == -1)
				return null;
			
			System.out.println("Manually selected card: (id:"+response+") "+this.getCards().get(response));
		
		return this.getCards().remove(response);
		
		
	}
	
}
