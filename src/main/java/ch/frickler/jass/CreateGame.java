package ch.frickler.jass;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

@ManagedBean
public class CreateGame {
	
	private String name;
	
	@ManagedProperty(value="#{userBean}")
	private UserBean userBean;
	
	private int winPoints;
	
	
	public String create() {
		GameManager gm = GameManager.getInstance();
		Long gameId = gm.createGame(getName(), getWinPoints());
		FacesContext ctx = FacesContext.getCurrentInstance();
		ctx.getExternalContext().getSessionMap().put(GameManager.GAME_ID_KEY, gameId);
		
		
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
