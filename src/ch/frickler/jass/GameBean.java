package ch.frickler.jass;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class GameBean {
	private String name;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	
}
