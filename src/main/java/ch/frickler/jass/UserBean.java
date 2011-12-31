package ch.frickler.jass;

import java.util.Locale;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import ch.frickler.jass.db.entity.User;
import ch.frickler.jass.helper.Translator;
import ch.frickler.jass.service.UserService;

/**
 * 
 * Bean to represent a user and his authentication
 * 
 */
@ManagedBean
@SessionScoped
public class UserBean {

	/**
	 * holds the locale from the user
	 */
	private String locale;

	/**
	 *  holds the username
	 */
	private String username;

	/**
	 * holds the password
	 */
	private String password;

	/**
	 * holds a boolean if the user is logged in
	 */
	private boolean loggedIn = false;

	/**
	 * holds the dataqbase representation of the user
	 */
	private User user = null;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return null; // we don't return the password
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @param newLocale
	 * 
	 * set the new locale, and changes the faces context to it
	 */
	public void setLocale(String newLocale) {
		if (newLocale != null)
			FacesContext.getCurrentInstance().getViewRoot()
					.setLocale(new Locale(newLocale));

		this.locale = newLocale;
	}

	public String getLocale() {
		if (locale == null) {
			FacesContext context = FacesContext.getCurrentInstance();
			// return only the first two chars (en) I don't care about en_US
			locale = context.getViewRoot().getLocale().toString()
					.substring(0, 2);
		}
		return locale;
	}

	/**
	 * action for letting user logging in
	 * 
	 * @return redirect to the listGames page or login page if login failed
	 */
	public String login() {
		String nextPage = null;

		if (checkUserAndPw()) {
			// yeah, the user is logged in
			loggedIn = true;
			nextPage = "restricted/listGames?faces-redirect=true";
		} else {
			// display a message
			FacesContext ctx = FacesContext.getCurrentInstance();
			ctx.addMessage(null, Translator.getMessage(ctx, "login_failed"));
		}

		return nextPage;
	}

	/**
	 * check given credentials
	 * @return true if user & password credential are right
	 */
	private boolean checkUserAndPw() {

		user = new UserService().checkUserNameAndPassword(username, password);
		if (user != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * logs out user, session gets cleared
	 * 
	 * @return redirect to login page
	 */
	public String logout() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		ctx.addMessage(null, Translator.getMessage(ctx, "login_goodbye"));

		setPassword(null);
		setUsername(null);
		setLocale(null);

		loggedIn = false;
		return "/login";
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	/**
	 * 
	 * @return a list of all available locales
	 */
	public String[] getAvailableLocales() {
		return new String[] { "de", "en", "fr" };
	}

	/**
	 * listens to the language dropdown
	 * 
	 * @param event
	 */
	public void swapLocale(ValueChangeEvent event) {
		String locale = (String) event.getNewValue();
		setLocale(locale);
	}

	public User getUser() {
		return user;
	}

	
	/**
	 * checks if the user is currently playing
	 * @return true if player is playing
	 */
	public boolean isPlaying() {
		if (user != null)
			return user.isPlaying();
		return false;
	}
}