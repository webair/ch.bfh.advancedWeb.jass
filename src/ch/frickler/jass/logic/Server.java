package ch.frickler.jass.logic;

import java.util.List;

public class Server {
	
	public List<Spiel> games;
	
	public List<Client> connectedClients;
	
	public void AddClient(int spielId,Client connectedClients){

	}
	
	public Spiel receiveAction(SpielAction action){
		
		action.doAction(null);
		return null;
	}
	
	public void kartenVerteilen(Spiel spiel){
		
		SpielAction action = new KartenVerteilAction();
		action.doAction(spiel);
	}
}
