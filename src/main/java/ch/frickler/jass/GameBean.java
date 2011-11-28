package ch.frickler.jass;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class GameBean {

	private int id;
	private String name;
	
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
