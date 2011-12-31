package ch.frickler.jass;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import ch.frickler.jass.helper.Translator;
import ch.frickler.jass.service.UserService;

@ManagedBean
@RequestScoped
public class RegisterBean {

	/**
	 * holds the user name for the registrating user
	 */
	private String username;

	/**
	 * holds the passwords (1 & 2 for checking typing failures)
	 */
	private String pw1;
	private String pw2;

	private String nick;

	/**
	 * 
	 * action for registrating user. user will be registrated if the inputs are
	 * valid
	 * 
	 * @return resdirect to login page, or if inputs are not valid to
	 *         registration page again
	 */
	public String register() {

		String nextPage = null;
		FacesContext ctx = FacesContext.getCurrentInstance();

		if (!pw1.equals(pw2)) {

			ctx.addMessage(null,
					Translator.getMessage(ctx, "register_no_pw_match"));
		} else if (username.equals(pw1)) {

			ctx.addMessage(null, Translator.getMessage(ctx, "user_pw_same"));
		} else if (!checkUsername()) {

			ctx.addMessage(null,
					Translator.getMessage(ctx, "register_username_not_free"));
		} else {

			saveUser();
			ctx.addMessage(null, Translator.getMessage(ctx, "user_created"));
			nextPage = "login";
		}

		return nextPage;
	}

	/**
	 * validate user name (valid when username isn't taken yet)
	 * 
	 * @return true if username is not taken yet
	 */
	private boolean checkUsername() {
		return new UserService().isUsernameUnused(username);
	}

	/**
	 * action for saving user to the database
	 */
	private void saveUser() {
		UserService u = new UserService();

		if (nick == null || nick.length() == 0)
			nick = username;

		u.createSpieler(username, pw1, nick);
	}

	// getters & setters

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPw1() {
		return pw1;
	}

	public void setPw1(String passwordOne) {
		this.pw1 = passwordOne;
	}

	public String getPw2() {
		return pw2;
	}

	public void setPw2(String passwordTwo) {
		this.pw2 = passwordTwo;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

}
