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

/**
 * this class is the implementation for the Entity: Team
 * the team contains of a team name, two users, the current points, and the cards made at the current round.
 */
@Entity
@Table(name = "TEAM")
public class Team implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID_GAME")
	private long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_USER1", nullable = false)
	private User user1;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_USER2", nullable = true)
	private User user2;

	@Column(name = "POINTS", nullable = false)
	private Integer points;

	@Column(name = "NAME", nullable = false)
	private String name = "";

	@Transient
	private List<Card> woncards = new ArrayList<Card>();

	public Team() {
		super();
	}

	public Team(User user1, User user2) {
		super();
		this.user1 = user1;
		this.user2 = user2;
	}

	public Team(User user1) {
		super();
		this.user1 = user1;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser1() {
		return user1;
	}

	public void setUser1(User user1) {
		this.user1 = user1;
	}

	public User getUser2() {
		return user2;
	}

	public void setUser2(User user2) {
		this.user2 = user2;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public List<User> getUsers() {
		List<User> users = new ArrayList<User>();
		if (user1 != null)
			users.add(user1);
		if (user2 != null)
			users.add(user2);
		return users;
	}

	public boolean isUserInTeam(User user) {
		return getUsers().contains(user);
	}

	public void addUser(User newuser) {
		if (user1 == null)
			user1 = newuser;
		if (user2 == null)
			user2 = newuser;
		else
			throw new RuntimeException(
					"tried to add a third user for this team");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addPoints(int i) {
		this.points += i;
	}

	public void addCard(List<Card> cards) {
		woncards.addAll(cards);
	}

	public void clearCards() {
		woncards = new ArrayList<Card>();

	}

	public List<Card> getCards() {
		return woncards;
	}

}
