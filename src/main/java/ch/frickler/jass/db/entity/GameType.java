package ch.frickler.jass.db.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import ch.frickler.jass.db.enums.GameKind;

/**
 * Entity implementation class for Entity: GameType
 * 
 */
@Entity
@Table(name = "GAMETYPE")
public class GameType implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID_GAMETYPE")
	private long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "GAME_KIND", nullable = false)
	private GameKind gameKind;

	@Column(name = "WEIGHT", nullable = false)
	private Integer weight;

	public GameType() {
		super();
	}

	public GameType(GameKind kind, Integer weight) {
		super();
		this.gameKind = kind;
		this.weight = weight;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public GameKind getGameKind() {
		return gameKind;
	}

	public void setGameKind(GameKind gameKind) {
		this.gameKind = gameKind;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

}
