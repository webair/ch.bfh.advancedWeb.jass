package ch.frickler.jass.db.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.String;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: User TODO crypt password
 */
@Entity
@Table(name = "USER")
public class User implements Serializable {

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

	@Column(name = "LOCALE")
	private String locale;

	public User() {
		super();
	}

	public User(String userName, String password, String name, String locale) {
		super();
		this.userName = userName;
		this.password = password;
		this.name = name;
		this.locale = locale;
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
		this.password = password;
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

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

}
