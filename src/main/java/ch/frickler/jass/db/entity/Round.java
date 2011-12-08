package ch.frickler.jass.db.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import ch.frickler.jass.db.enums.GameKind;



/**
 * Entity implementation class for Entity: Round
 * 
 */
@Entity
@Table(name = "ROUND")
public class Round implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID_ROUND")
	private long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_GAME", nullable = false)
	private Game game;


	@Column(name = "ID_GAMETYPE", nullable = true)
	private GameKind gameKind;

	// also known as gschobe
	@Column(name = "PUSHED", nullable = false)
	private Boolean pushed = false;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BEGINNER", nullable = true)
	private User beginner;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CURRENTPLAYER", nullable = true)
	private User currentplayer;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PLAYERWITHSTOECK", nullable = true)
	private User playerwithstoeck;
	
	@Transient
    private List<Card> cards = new ArrayList<Card>();

	
	public Round() {
		super();
	}

	public Round(Game game, GameKind gameKind) {
		super();
		this.game = game;
		this.gameKind = gameKind;
	}

	public GameKind getGameKind() {
		return gameKind;
	}

	public void setGameKind(GameKind type) {
		this.gameKind = type;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public Boolean isPushed() {
		return pushed;
	}

	public void setPushed() {
		this.pushed = true;
	}

	public User getPlayerWithStoeck() {
		return playerwithstoeck;
	}

	public void setPlayerWithStoeck(User playerwithstoeck) {
		this.playerwithstoeck = playerwithstoeck;
	}

	public User getCurrentPlayer() {
		return currentplayer;
	}

	public void setCurrentPlayer(User currentplayer) {
		this.currentplayer = currentplayer;
	}

	public User getBeginner() {
		return beginner;
	}

	public void setBeginner(User beginner) {
		this.beginner = beginner;
	}
	
	public List<Card> getCards() {
		return this.cards;
	}
	
	public void removeCards() {
		this.cards  = new ArrayList<Card>();
	} 
	
	public void addCard(Card c) {
		this.cards.add(c);
	}




}
