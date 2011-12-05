package ch.frickler.jass.logic;

public interface Player {

	String getName();

	/**
	 * returning true tells the game that it should automatically play (bots)
	 * @return whether the bot/user played a card
	 */
	boolean autoPlay();

}
