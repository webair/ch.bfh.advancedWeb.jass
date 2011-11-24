package ch.frickler.jass;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean
@RequestScoped
public class RegisterBean {

	private String username;
	private String passwordOne;
	private String passwordTwo;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPasswordOne() {
		return passwordOne;
	}

	public void setPasswordOne(String passwordOne) {
		this.passwordOne = passwordOne;
	}

	public String getPasswordTwo() {
		return passwordTwo;
	}

	public void setPasswordTwo(String passwordTwo) {
		this.passwordTwo = passwordTwo;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	private String nick;

	public String register() {

		return null;
	}

}
