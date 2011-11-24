package ch.frickler.jass;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class GameBean {
	private String name;
	private int id;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
	
	
}
