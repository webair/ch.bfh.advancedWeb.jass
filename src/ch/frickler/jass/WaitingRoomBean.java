package ch.frickler.jass;

import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class WaitingRoomBean {
	int gameId;

	public int getGameId() {
		return gameId;
	}

	public String joinGame() {
		Map<String,String> params =  FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		gameId = Integer.parseInt(params.get("gameId"));
		return "waitForPlayers?faces-redirect=true";
	}
	
	
}
