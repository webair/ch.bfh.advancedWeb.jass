package ch.frickler.jass.db.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import ch.frickler.jass.logic.Player;
import ch.frickler.jass.service.UserService;

/**
 * Entity implementation class for Entity: User TODO crypt password
 */
@Entity
@Table(name = "USER")
public class User implements Serializable, Player {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID_USER")
	private long id;

	@Column(name = "USERNAME", nullable = false)
	private String userName;

	@Column(name = "PASSWORD", nullable = false)
	private String password;

	@Column(name = "NAME", nullable = false)
	private String name;
	
	public User(){
		super();
	}
	
	public User(String userName, String password, String name) {
		super();
		this.userName = userName;
		this.password = UserService.cryptPw(password);
		this.name = name;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = UserService.cryptPw(password);
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean autoPlay() {
		/*
		 * hey, I'm human! you cannot force me!
		 */
		return false;
	}
	
}
