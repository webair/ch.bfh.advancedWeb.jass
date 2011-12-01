package ch.frickler.jass;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import ch.frickler.jass.db.entity.User;
import ch.frickler.jass.helper.MessageHelper;
import ch.frickler.jass.service.UserService;

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

	/**
	 * registration is possible, if the username is free, and the two passwords
	 * match.
	 * 
	 * @return redirects to the login or stays on the register pages
	 */
	public String register() {

		String nextPage = null;
		FacesContext ctx = FacesContext.getCurrentInstance();

		if (!passwordOne.equals(passwordTwo)) {
			// pws do not match
			ctx.addMessage(null,
					MessageHelper.getMessage(ctx, "register_no_pw_match"));
		} else if (username.equals(passwordOne)){
			// same username and password...
			ctx.addMessage(null,
					MessageHelper.getMessage(ctx, "user_pw_same"));
		}else if (!checkUsername()) {
			// someone already has this username
			ctx.addMessage(null,
					MessageHelper.getMessage(ctx, "register_username_not_free"));
		} else {
			// everything's ok, create the user
			storeUser();
			ctx.addMessage(null, MessageHelper.getMessage(ctx, "user_created"));
			nextPage = "login";
		}

		return nextPage;
	}

	/**
	 * a username is valid if it is still free
	 * 
	 * @return true if the username is ok
	 */
	private boolean checkUsername() {
		// check the username
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("MyBuzzwordJass");

		// TODO sekiurity??? sql injection
		EntityManager em = emf.createEntityManager();
		Query q = em.createQuery("select count(*) from User where userName=?1");
		q.setParameter(1, username);

		Long num = (Long) q.getResultList().get(0);

		return num == 0;
	}

	private void storeUser() {
		// TODO create a db helper?
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("MyBuzzwordJass");
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();

		UserService  u = new UserService();
		
		if (nick == null || nick.length() == 0)
			nick = username;
		
		u.createSpieler(username, passwordOne, nick,false);
		

		em.merge(u);
		em.getTransaction().commit();

	}

}
