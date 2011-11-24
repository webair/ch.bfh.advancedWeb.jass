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
 * Entity implementation class for Entity: Team
 * 
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
	@JoinColumn(name = "ID_USER2", nullable = false)
	private User user2;

	@Column(name = "POINTS", nullable = false)
	private Integer points;

	public Team() {
		super();
	}	

	public Team(User user1, User user2) {
		super();
		this.user1 = user1;
		this.user2 = user2;
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

}
