package ch.frickler.jass;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

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

	private static final String UI_PROPERTY = "ch.frickler.jass.res.UI";

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

		if (username.equals("buzzer")) {
			// yeah, the user is logged in
			loggedIn = true;
			nextPage = "restricted/overview?faces-redirect=true";
		} else {
			//
			FacesContext ctx = FacesContext.getCurrentInstance();
			ResourceBundle bundle = ResourceBundle.getBundle(UI_PROPERTY, ctx
					.getViewRoot().getLocale());
			ctx.addMessage(null,
					new FacesMessage(bundle.getString("login_failed")));
		}

		return nextPage;
	}

	public String logout() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		ResourceBundle bundle = ResourceBundle.getBundle(UI_PROPERTY, ctx
				.getViewRoot().getLocale());
		// TODO this message is not displayed
		ctx.addMessage(null,
				new FacesMessage(bundle.getString("login_goodbye")));
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
	 * @param event
	 */
	public void swapLocale(ValueChangeEvent event) {
		String locale = (String) event.getNewValue();
		setLocale(locale);
	}
}