package ch.frickler.jass;

import java.util.Locale;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import ch.frickler.jass.entity.User;
import ch.frickler.jass.helper.MessageHelper;

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

	private boolean checkUserAndPw(){
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("MyBuzzwordJass");

		// TODO sekiurity??? sql injection
		EntityManager em = emf.createEntityManager();
		Query q = em.createQuery("select count(*) from User where userName=?1 and password=?2");
		q.setParameter(1, username).setParameter(2, User.cryptPw(password));
		
		Long num = (Long)q.getResultList().get(0);
		return num == 1; // there must be a match
	}
	
	public String logout() {
		// TODO this message is not displayed
		FacesContext ctx = FacesContext.getCurrentInstance();
		ctx.addMessage(null, MessageHelper.getMessage(ctx, "login_goodbye"));

		setPassword(null);
		setUsername(null);
		setLocale(null);
		
		loggedIn = false;
		return "login";
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