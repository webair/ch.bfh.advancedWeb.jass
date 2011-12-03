package ch.frickler.jass;

import java.util.Locale;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import ch.frickler.jass.db.entity.User;
import ch.frickler.jass.helper.MessageHelper;
import ch.frickler.jass.service.UserService;

/**
 * managed bean that represents a user. authentication is done by checking
 * isLoggedIn() of this bean. also handles the users language settings
 * 
 */
@ManagedBean
@SessionScoped
public class UserBean {

	private String locale;

	private String username;

	private String password;

	private boolean loggedIn = false;

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

	public void setLocale(String newLocale) {
		if (newLocale != null)
			FacesContext.getCurrentInstance().getViewRoot()
					.setLocale(new Locale(newLocale));

		this.locale = newLocale;
	}

	public String getLocale() {
		if (locale == null) {
			FacesContext context = FacesContext.getCurrentInstance();
			locale = context.getViewRoot().getLocale().toString();
		}
		return locale;
	}

	/**
	 * 
	 * @return null or the next page
	 */
	public String login() {
		String nextPage = null;

		if (checkUserAndPw()) {
			// yeah, the user is logged in
			loggedIn = true;
			nextPage = "restricted/overview?faces-redirect=true";
		} else {
			// display a message
			FacesContext ctx = FacesContext.getCurrentInstance();
			ctx.addMessage(null, MessageHelper.getMessage(ctx, "login_failed"));
		}

		return nextPage;
	}

	private boolean checkUserAndPw() {

		user = new UserService().checkUserNameAndPassword(username, password);
		if (user != null) {
			return true;
		} else {
			return false;
		}
	}

	public String logout() {
		// TODO this message is not displayed
		FacesContext ctx = FacesContext.getCurrentInstance();
		ctx.addMessage(null, MessageHelper.getMessage(ctx, "login_goodbye"));

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
	 * the action listener for the languages menu
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
}