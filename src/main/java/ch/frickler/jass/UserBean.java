package ch.frickler.jass;

import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class UserBean {

	private String locale;

	private String username;

	private String password;

	private boolean loggedIn = false;

	private static final String UI_PROPERTY="ch.frickler.jass.res.UI";
	
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

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getLocale() {
		if (locale == null) {
			FacesContext context = FacesContext.getCurrentInstance();
			return context.getViewRoot().getLocale().toString();
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
			ResourceBundle bundle =
	                ResourceBundle.getBundle(UI_PROPERTY,
	                    		ctx.getViewRoot().getLocale());
			ctx.addMessage(null, new FacesMessage(bundle.getString("login_failed")));
		}

		return nextPage;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

}