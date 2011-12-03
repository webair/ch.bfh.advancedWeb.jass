package ch.frickler.jass.logic;

public class Bot implements Player {

	private static int botNum=0;
	
	public Bot(){
		botNum++;
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
