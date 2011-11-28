package ch.frickler.jass;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class CreateGame {
	
	private String name;
	
	private int winPoints;

	
	public String create() {
		
		return null;
	}
	
	
	//Getters & Setters
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getWinPoints() {
		return winPoints;
	}

	public void setWinPoints(int winPoints) {
		this.winPoints = winPoints;
	}
	
	
	
}
