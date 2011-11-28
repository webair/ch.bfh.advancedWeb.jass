package ch.frickler.jass.db.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Player
 * 
 */
@Entity
@Table(name = "ROUND_CARD")
public class RoundCard implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID_ROUND_CARD")
	private long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_USER", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ROUND", nullable = false)
	private Round round;

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinTable(name = "PLAYER_CARD", joinColumns = { @JoinColumn(name = "ID_ROUND_CARD", referencedColumnName = "ID_ROUND_CARD") }, inverseJoinColumns = { @JoinColumn(name = "ID_CARD", referencedColumnName = "ID_CARD") })
	private Set<Card> cards;

	public RoundCard() {
		super();
	}

	public RoundCard(User user, Round round, Set<Card> cards) {
		super();
		this.user = user;
		this.round = round;
		this.cards = cards;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<Card> getCards() {
		return cards;
	}

	public void setCards(Set<Card> cards) {
		this.cards = cards;
	}

	public Round getRound() {
		return round;
	}

	public void setRound(Round round) {
		this.round = round;
	}

}
