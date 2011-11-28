package ch.frickler.jass.logic;

import java.util.List;

import ch.frickler.jass.logic.definitions.IUserAction;


public class Server {
	
	public List<Spiel> games;
	
	public List<Client> connectedClients;
	
	public void AddClient(int spielId,Client connectedClients){

	}
	
	public Spiel receiveAction(IUserAction action){
		
		action.doAction(null);
		return null;
	}
	
	public void kartenVerteilen(Spiel spiel){
		
		IUserAction action = new KartenVerteilAction(null);
		action.doAction(spiel);
	}
}
