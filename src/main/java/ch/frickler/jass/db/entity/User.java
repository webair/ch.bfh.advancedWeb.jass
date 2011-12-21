package ch.frickler.jass.db.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import ch.frickler.jass.service.UserService;

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

	@Transient
	private List<Card> cards = new ArrayList<Card>();

	@Column(name = "BOT")
	private boolean isABot = false;

	@Transient
	private static int numOfBots=0;
	@Transient
	private int botNum=0;
	
	@Transient
	private boolean playing=false;
	
	public User() {
		super();
	}

	public User(String name){
		this();
		this.isABot = true;
		numOfBots++;
		botNum = numOfBots;
		this.name = name + botNum;
		this.password = name;
		this.userName = name;
	}
	
	public User(String userName, String password, String name) {
		super();
		this.userName = userName;
		this.password = UserService.cryptPw(password);
		this.name = name;
		this.isABot = false;
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

	public boolean isABot() {
		return isABot;
	}

	public List<Card> getCards() {
		// TODO Auto-generated method stub
		return cards;
	}

	public void removeCard(Card layedCard) {
		cards.remove(layedCard);
	}

	public void addCard(Card card) {
		cards.add(card);

	}
	
	public void setPlaying(boolean playing){
		this.playing = playing;
	}
	
	public boolean isPlaying(){
		return playing;
	}
}
