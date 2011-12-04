package ch.frickler.jass.logic;

public class Bot implements Player {

	private static int num_of_bots=0;
	
	private int botNum; 
	
	public Bot(){
		num_of_bots++;
		botNum = num_of_bots;
	}
	
	@Override
	public String getName() {
		return "Bot Nr. " + botNum;
	}

	@Override
	public boolean autoPlay() {
		return true;
	}
	
}
