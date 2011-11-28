package ch.frickler.jass.db.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entity implementation class for Entity: Game
 * 
 */
@Entity
@Table(name = "GAME")
public class Game implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID_GAME")
	private long id;
	
	@Column(name = "NAME", nullable = false)
	private String name;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TEAM1")
	private Team team1;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TEAM2")
	private Team team2;

	@OneToOne
	@JoinColumn(name = "CURRENT_ROUND")
	private Round currentRound;

	@Column(name = "WINPOINTS", nullable = false)
	private Integer winPoints;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_DATE", nullable = false)
	private Date startDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_USER", nullable = false)
	private User nextAnnouncer;
	

	public Game() {
		super();
	}

	public Game(String name, Team team1, Team team2, User nextAnnouncer) {
		super();
		this.name = name;
		this.team1 = team1;
		this.team2 = team2;
		this.nextAnnouncer = nextAnnouncer;
	}
	
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Team getTeam1() {
		return team1;
	}

	public void setTeam1(Team team1) {
		this.team1 = team1;
	}

	public Team getTeam2() {
		return team2;
	}

	public void setTeam2(Team team2) {
		this.team2 = team2;
	}

	public Integer getWinPoints() {
		return winPoints;
	}

	public void setWinPoints(Integer winPoints) {
		this.winPoints = winPoints;
	}

	public Round getCurrentRound() {
		return currentRound;
	}

	public void setCurrentRound(Round currentRound) {
		this.currentRound = currentRound;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public User getNextAnnouncer() {
		return nextAnnouncer;
	}

	public void setNextAnnouncer(User nextAnnouncer) {
		this.nextAnnouncer = nextAnnouncer;
	}

}
