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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;



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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_GAMETYPE", nullable = false)
	private GameType gameType;

	// also known as gschobe
	@Column(name = "PUSHED", nullable = false)
	private Boolean pushed;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BEGINNER", nullable = false)
	private User beginner;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CURRENTPLAYER", nullable = false)
	private User currentplayer;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PLAYERWITHSTOECK", nullable = false)
	private User playerwithstoeck;
	
    @ManyToMany
    @JoinTable(name="LAYING_CARDS")
    public List<Card> getLayingCards() { return cards; }
	
    private List<Card> cards;

	
	public Round() {
		super();
	}

	public Round(Game game, GameType gameType) {
		super();
		this.game = game;
		this.gameType = gameType;
	}

	public GameType getGameType() {
		return gameType;
	}

	public void setGameType(GameType gameType) {
		this.gameType = gameType;
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
