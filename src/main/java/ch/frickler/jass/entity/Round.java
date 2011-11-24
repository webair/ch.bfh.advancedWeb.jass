package ch.frickler.jass.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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

}
